package skytheory.example.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.IContainerFactory;
import skytheory.example.block.entity.DisplayStandEntity;
import skytheory.example.init.MenuInit;

/**
 * サーバー・クライアント両側で用いる、GUIのためのアイテムスロットなどを保持するクラス
 * 描画部分及びその他のボタン部分などはScreenとしてクライアントで持つ
 * @author SkyTheory
 *
 */
public class DisplayStandMenu extends AbstractContainerMenu {

	public static final int BLOCK_SLOT_X = 80;
	public static final int BLOCK_SLOT_Y = 35;
	public static final int PLAYER_SLOT_X = 8;
	public static final int PLAYER_SLOT_Y = 84;
	public static final int HOTBAR_SLOT_X = 8;
	public static final int HOTBAR_SLOT_Y = 142;
	public final DisplayStandEntity block;

	public static final IContainerFactory<DisplayStandMenu> CLIENT_CONTAINER_FACTORY = new IContainerFactory<>() {
		@Override
		public DisplayStandMenu create(int windowId, Inventory inv, FriendlyByteBuf data) {
			Level level = inv.player.getLevel();
			BlockPos pos = data.readBlockPos();
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof DisplayStandEntity displayStand) {
				return new DisplayStandMenu(windowId, inv, displayStand);
			}
			return null;
		}
	};

	// Blockから呼ばれて、サーバー側のGUIを作成する
	public DisplayStandMenu(int containerId, Inventory inv, DisplayStandEntity block) {
		this(containerId, new InvWrapper(inv), block);
	}

	public DisplayStandMenu(int containerId, IItemHandler playerItems, DisplayStandEntity block) {
		super(MenuInit.DISPLAY_STAND.get(), containerId);
		this.block = block;
		for (int i = 0; i < 27; i++) {
			int x = PLAYER_SLOT_X + (i % 9) * 18;
			int y = PLAYER_SLOT_Y + (i / 9) * 18;
			this.addSlot(new SlotItemHandler(playerItems, i + 9, x, y));
		}
		for (int i = 0; i < 9; i++) {
			int x = HOTBAR_SLOT_X + i * 18;
			int y = HOTBAR_SLOT_Y;
			this.addSlot(new SlotItemHandler(playerItems, i, x, y));
		}
		this.addSlot(new SlotItemHandler(block.itemStackHandler, 0, BLOCK_SLOT_X, BLOCK_SLOT_Y));
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			// クリックしたスロットがプレイヤーのインベントリだった場合
			if (index < Inventory.INVENTORY_SIZE) {
				// プレイヤーのスロットの次のインデックス（inputの最初のスロット）から
				// 全スロットから出力スロットの個数を引いたインデックス（inputの最後のスロット）を対象に
				// クリックしたスロットからアイテムを移動する
				if (!this.moveItemStackTo(itemstack1, Inventory.INVENTORY_SIZE, this.slots.size(), true)) {
					// 移動できなかった場合はEmptyを返して処理を終了
					return ItemStack.EMPTY;
				}
			// クリックしたスロットがBlockEntityのインベントリだった場合
			} else {
				// プレイヤーのスロットを対象にアイテムを移動する
				if (!this.moveItemStackTo(itemstack1, 0, Inventory.INVENTORY_SIZE, false)) {
					// 移動できなかった場合はEmptyを返して処理を終了
					return ItemStack.EMPTY;
				}
			}
			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}

	@Override
	public boolean stillValid(Player player) {
		if (block == null) return false;
		BlockPos pos = this.block.getBlockPos();
		return player.distanceToSqr((double)pos.getX() + 0.5d, (double)pos.getY() + 0.5d, (double)pos.getZ() + 0.5d) <= 64.0d;
	}

}
