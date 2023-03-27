package skytheory.example.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.IContainerFactory;
import skytheory.example.block.entity.SimpleProcessorEntity;
import skytheory.example.init.MenuInit;

/**
 * サーバー・クライアント両側で用いる、GUIのためのアイテムスロットなどを保持するクラス
 * 描画部分及びその他のボタン部分などはScreenとしてクライアントで持つ
 * @author SkyTheory
 *
 */
public class SimpleProcessorMenu extends AbstractContainerMenu {

	public static final int INPUT_SLOT_X = 38;
	public static final int INPUT_SLOT_Y = 26;
	public static final int OUTPUT_SLOT_X = 116;
	public static final int OUTPUT_SLOT_Y = 35;
	public static final int PLAYER_SLOT_X = 8;
	public static final int PLAYER_SLOT_Y = 84;
	public static final int HOTBAR_SLOT_X = 8;
	public static final int HOTBAR_SLOT_Y = 142;
	
	public final SimpleProcessorEntity block;
	public final IItemHandler inputHandler;
	public final IItemHandler outputHandler;
	public final DataSlot progressData;

	public static final IContainerFactory<SimpleProcessorMenu> CLIENT_CONTAINER_FACTORY = new IContainerFactory<>() {
		@Override
		public SimpleProcessorMenu create(int windowId, Inventory inv, FriendlyByteBuf data) {
			Level level = inv.player.getLevel();
			BlockPos pos = data.readBlockPos();
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof SimpleProcessorEntity simpleProcessor) {
				return new SimpleProcessorMenu(windowId, inv, simpleProcessor);
			}
			return null;
		}
	};
	
	// Blockから呼ばれて、サーバー側のGUIを作成する
	public SimpleProcessorMenu(int containerId, Inventory inv, SimpleProcessorEntity block) {
		this(containerId, new InvWrapper(inv), block);
	}
	
	public SimpleProcessorMenu(int containerId, IItemHandler playerItems, SimpleProcessorEntity block) {
		super(MenuInit.SIMPLE_PROCESSOR.get(), containerId);
		this.block = block;
		// ここをinputAccessorにしてしまうと、スロットに入れたアイテムが取り出せなくなるので注意
		this.inputHandler = block.input;
		// ここをoutputではなくoutputAccesorにすることで、スロットのクリックでアイテムを入れてしまうことを防止する
		this.outputHandler = block.outputAccessor;
		// プレイヤーのインベントリ
		for (int i = 0; i < 27; i++) {
			// x座標とy座標を計算して
			int x = PLAYER_SLOT_X + (i % 9) * 18;
			int y = PLAYER_SLOT_Y + (i / 9) * 18;
			// 計算した位置にスロットを追加する
			// i + 9となっているのは、index0から8がホットバーであるため
			this.addSlot(new SlotItemHandler(playerItems, i + 9, x, y));
		}
		for (int i = 0; i < 9; i++) {
			// x座標とy座標を計算して
			int x = HOTBAR_SLOT_X + i * 18;
			int y = HOTBAR_SLOT_Y;
			// 計算した位置にスロットを追加する
			this.addSlot(new SlotItemHandler(playerItems, i, x, y));
		}
		for (int i = 0; i < 4; i++) {
			// x座標とy座標を計算して
			int x = INPUT_SLOT_X + (i % 2) * 18;
			int y = INPUT_SLOT_Y + (i / 2) * 18;
			// 計算した位置にスロットを追加する
			this.addSlot(new SlotItemHandler(inputHandler, i, x, y));
		}
		this.addSlot(new SlotItemHandler(outputHandler, 0, OUTPUT_SLOT_X, OUTPUT_SLOT_Y));
		// 加工タイマーの進捗を同期するためのデータスロットを追加する
		this.progressData = new SimpleProcessorDataSlot(block);
		this.addDataSlot(progressData);
	}

	/**
	 * スロットをシフトクリックした際の動作を定義する
	 */
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
				if (!this.moveItemStackTo(itemstack1, Inventory.INVENTORY_SIZE, this.slots.size() - outputHandler.getSlots(), false)) {
					return ItemStack.EMPTY;
				}
			// クリックしたスロットがBlockEntityのインベントリだった場合
			} else {
				// プレイヤーのスロットを対象にアイテムを移動する
				if (!this.moveItemStackTo(itemstack1, 0, Inventory.INVENTORY_SIZE, false)) {
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

	/**
	 * GUIを開いている間にデータを同期するためのオブジェクト
	 * int値を送ることができる
	 * @author SkyTheory
	 *
	 */
	private static class SimpleProcessorDataSlot extends DataSlot {

		private final SimpleProcessorEntity owner;
		
		private SimpleProcessorDataSlot(SimpleProcessorEntity owner) {
			this.owner = owner;
		}
		
		/**
		 * 同期するデータのint値を取得する
		 */
		@Override
		public int get() {
			return owner.progress;
		}

		/**
		 * 同期時にデータを受け取り、int値を設定する
		 */
		@Override
		public void set(int pParam) {
			owner.progress = pParam;
		}
		
	}
}
