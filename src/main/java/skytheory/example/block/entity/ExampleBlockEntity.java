package skytheory.example.block.entity;

import net.minecraft.core.BlockPos;
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
import net.minecraftforge.network.PacketDistributor;
import skytheory.example.init.BlockInit;
import skytheory.example.inventory.ExampleContainerMenu;
import skytheory.example.network.BlockMessage;
import skytheory.example.network.PacketHandler;

/**
 * MenuProviderを継承するオブジェクトはこれである必要はない
 * ただ、GUIの名前を定義したり、Menuに中身を渡したりするのにここから参照できると便利
 * @author SkyTheory
 *
 */
public class ExampleBlockEntity extends BlockEntity implements MenuProvider {

	public ExampleBlockEntity(BlockPos pos, BlockState state) {
		super(BlockInit.EXAMPLE_BLOCK_ENTITY_TYPE.get(), pos, state);
	}

	public void clientTick() {
		// クライアント側でtickごとに何かする
		// 動作させるためにはTickerが必要、詳細はEntityBlock側を参照
	}
	
	public void serverTick() {
		// サーバー側でtickごとに何かする
		// 動作させるためにはTickerが必要、詳細はEntityBlock側を参照
	}

	/**
	 * MenuProviderからの継承メソッド
	 * Menuオブジェクトを作成してServerPlayerに渡す
	 */
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
		return new ExampleContainerMenu(id, inv, this);
	}

	/**
	 * MenuProviderからの継承メソッド
	 * GUIに表示する名前を定義する
	 */
	@Override
	public Component getDisplayName() {
		// Component.translatableとすると対応したlangkeyから得られる……はず
//		return Component.literal("Example Block Entity");
		return Component.literal("");
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
	 * BlockItemHandlerの実装の方でこれを呼び出すようにしてある
	 * これが呼び出されると、チャンクのアンロード時などに中身のデータを保存してくれる
	 * 逆に言えば、適切にこれが呼ばれていないとデータの書き込みが行われず、変更をロストする
	 */
	@Override
	public void setChanged() {
		super.setChanged();
		// ついでにレンダー用に同期処理を行う
		if (!this.level.isClientSide()) {
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> this.level.getChunkAt(this.getBlockPos())), new BlockMessage(this));
		}
	}
}
