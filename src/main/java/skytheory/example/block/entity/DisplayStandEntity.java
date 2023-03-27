package skytheory.example.block.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import skytheory.example.block.ItemHandlerBlock;
import skytheory.example.capability.InventoryHandler;
import skytheory.example.capability.ItemHandlerListener;
import skytheory.example.gui.DisplayStandMenu;
import skytheory.example.init.BlockEntityInit;
import skytheory.example.network.BlockMessage;
import skytheory.example.network.PacketHandler;

/**
 * MenuProviderを継承するオブジェクトはこれである必要はない
 * ただ、GUIの名前を定義したり、Menuに中身を渡したりするのにここから参照できると便利
 * @author SkyTheory
 *
 */
public class DisplayStandEntity extends BlockEntity implements MenuProvider, ItemHandlerBlock<CompoundTag>, ItemHandlerListener {

	public ItemStackHandler itemStackHandler;

	public DisplayStandEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.DISPLAY_STAND.get(), pos, state);
		this.itemStackHandler = new InventoryHandler(1, this);
	}

	/**
	 * MenuProviderからの継承メソッド
	 * Menuオブジェクトを作成してServerPlayerに渡す
	 */
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
		return new DisplayStandMenu(id, inv, this);
	}

	/**
	 * MenuProviderからの継承メソッド
	 * GUIに表示する名前を定義する
	 */
	@Override
	public Component getDisplayName() {
		// Component.translatableとすると対応したlangkeyから得られる……はず
		//		return Component.literal("Display Stand");
		return Component.translatable("block.st_example.display_stand");
	}

	/**
	 * ここでクライアントと同期したいタグのデータを返すようにしておけば
	 * チャンクの読み込み時にクライアント側でhandleUpdateTagが実行される
	 * クライアント側でワールドの描画に保持しているアイテムを用いる、みたいな特殊な場合でない限り不要
	 * 単純なチェストのような、GUIで中身を確認するだけのブロックなら
	 * そのGUIの方にもともと同期処理があるので、わざわざこちらで同期する必要はない
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CompoundTag getUpdateTag() {
		IItemHandler handler = this.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		if (handler != null) {
			return ((INBTSerializable<CompoundTag>) handler).serializeNBT();
		}
		return super.getUpdateTag();
	}

	/**
	 * 上で作成したタグをサーバーから受け取って、同期処理を行う
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		if (tag.isEmpty()) return;
		IItemHandler handler = this.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		if (handler != null) {
			((INBTSerializable<CompoundTag>) handler).deserializeNBT(tag);
		}
	}

	/**
	 * 引数のDirectionの値からアクセスした際に、返り値のIItemHandler（もしくはnull）を渡す
	 */
	@Override
	public @Nullable IItemHandler getItemHandler(Direction direction) {
		return this.itemStackHandler;
	}

	/**
	 * BlockEntityのデータ読み書きの際に、Capabilityの部分の読み書きに用いるINBTSerializableを渡す
	 * 要するに、ここでitemStackHandlerを渡しているので、そこのデータの保存・読み込みも行うよ、というもの
	 */
	@Override
	public @NotNull INBTSerializable<CompoundTag> getSerializer() {
		return this.itemStackHandler;
	}
	
	/**
	 * NBTから内部インベントリを読み込んだ際に呼ばれる
	 * 特に何もしない
	 */
	@Override
	public void onLoad(InventoryHandler itemHandler) {
	}
	
	/**
	 * InventoryHandlerの実装の方でこれを呼び出すようにしてある
	 * インベントリの中身に変更があった際にこれが呼び出される
	 */
	@Override
	public void onContentsChanged(InventoryHandler itemHandler, int slot) {
		if (!this.level.isClientSide()) {
			// これが呼び出されると、チャンクのアンロード時などに中身のデータを保存してくれる
			// 逆に言えば、適切にこれが呼ばれていないとデータの書き込みが行われず、ログアウトした際などに変更をロストする
			this.setChanged();
			// ついでにレンダー用に同期処理を行う
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> this.level.getChunkAt(this.getBlockPos())), new BlockMessage(this));
		}
	}

}
