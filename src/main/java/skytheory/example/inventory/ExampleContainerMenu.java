package skytheory.example.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import skytheory.example.block.entity.ExampleBlockEntity;

public class ExampleContainerMenu extends AbstractContainerMenu {
	
	public static final MenuType<ExampleContainerMenu> TYPE = new MenuType<ExampleContainerMenu>(ExampleContainerMenu::new);
	
	public static final int ENTITY_SLOT_X = 80;
	public static final int ENTITY_SLOT_Y = 35;
	public static final int PLAYER_SLOT_X = 8;
	public static final int PLAYER_SLOT_Y = 84;
	public static final int HOTBAR_SLOT_X = 8;
	public static final int HOTBAR_SLOT_Y = 142;
	public final ExampleBlockEntity block;

	// MenuScreenから呼ばれて、クライアント側のGUIを作る
	public ExampleContainerMenu(int containerId, Inventory inv) {
		this(containerId, new InvWrapper(inv), new ItemStackHandler(1), null);
	}
	
	// Blockから呼ばれて、サーバー側のGUIを作成する
	public ExampleContainerMenu(int containerId, Inventory inv, ExampleBlockEntity block) {
		this(containerId, new InvWrapper(inv), block.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null), block);
	}
	
	public ExampleContainerMenu(int containerId, IItemHandler playerItems, IItemHandler blockItems, ExampleBlockEntity block) {
		super(TYPE, containerId);
		this.block = block;
		this.addSlot(new SlotItemHandler(blockItems, 0, ENTITY_SLOT_X, ENTITY_SLOT_Y));
		// プレイヤーのインベントリ
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				int x = PLAYER_SLOT_X + i * 18;
				int y = PLAYER_SLOT_Y + j * 18;
				this.addSlot(new SlotItemHandler(playerItems, i + j * 9 + 9, x, y));
			}
		}
		for (int i = 0; i < 9; i++) {
			int x = HOTBAR_SLOT_X + i * 18;
			int y = HOTBAR_SLOT_Y;
			this.addSlot(new SlotItemHandler(playerItems, i, x, y));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 0) {
				if (!this.moveItemStackTo(itemstack1, 1, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
				return ItemStack.EMPTY;
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
		if (block == null) return true;
		BlockPos pos = this.block.getBlockPos();
		return player.distanceToSqr((double)pos.getX() + 0.5d, (double)pos.getY() + 0.5d, (double)pos.getZ() + 0.5d) <= 64.0d;
	}
	
}
