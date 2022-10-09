package skytheory.example.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import skytheory.example.block.entity.ExampleBlockEntity;
import skytheory.example.init.BlockInit;

/**
 * ブロックに追加で何かしらの機能を追加したい場合は、Blockクラスを拡張するなどして対応する
 * 更に、置いたブロックの個々にデータを保存したい場合 e.g.)チェストやかまどなどの中身
 * BlockEntityを実装し、そこに機能を追加していく
 * Blockクラスに定義するのは、ワールドに置かれたその種類のブロック全てに共通すること
 * BlockEntityクラスに定義するのは、その種類のブロックでも異なる動作を必要とすること
 * 例えばレッドストーンランプは、どの場所に、どんな状況で置かれていても同じように振舞うが
 * ドロッパーやディスペンサーは、その時々の中身に応じて異なる動作をする
 * 前者はブロッククラスにレッドストーン入力された時の動作を定義すればいいが
 * 後者は中身のアイテムという個々の情報を持つためにBlockEntityが必要になる
 * 
 * BlockEntityを追加したい場合、まずはこのブロックのようにEntityBlockを継承して
 * newBlockEntityでBlockEntityのインスタンスを作成できるようにする
 * 
 * 次に作成したBlockEntityのクラスをBlockEntityTypeとして登録する（BlockInitに登録例がある）
 * 
 * 作成したBlockEntityに時間経過で何かをする処理を追加したいのなら、getTickerで処理を追加できる
 * 
 * 最後に、デフォルトのブロックより複雑なモデルを描画したいのなら
 * SetupEvent.BlockEntityを参考に、モデルとレンダラーを登録すること
 * モデルが、このブロックはどのような形をしているのか？　ということを定義するクラスで
 * レンダラーが、そのモデルをどういうかたちでワールドに描画するのか？　ということを定義するクラス
 * 
 * ちなみにアイテム状態のBlockEntityはまた別の形で描画する
 * ExampleItemBlockEntityのように、BlockItemを継承したクラスとBlockEntityWithoutLevelRendererを継承したクラスのふたつ
 * それと、jsonの定義も忘れずに！
 * jsonファイルの詳細はdata.ExampleBlockStateProvider及びdata.ExampleItemModelProviderと
 * その2つを元にgenDataで作成したgenerated/resources/assets/st_example/models/block/example_entity_blockを参照
 */
public class ExampleEntityBlock extends Block implements EntityBlock {

	/**
	 *  ブロックの当たり判定などの大きさ、引数はxyz始点、xyz終点の順
	 *  Block.boxはちょうどテクスチャのピクセル数と同じになるように1/16してくれるのは分かりやすい
	 *  1.0dが1ブロックの辺の大きさになる数値で指定したいのなら、Shapes.boxを使うこと
	 */
	public static final VoxelShape PLATE_AABB = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 4.0d, 16.0d);

	public ExampleEntityBlock(Properties properties) {
		super(properties);
	}

	/**
	 * ブロックの形状を指定する
	 */
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
		return PLATE_AABB;
	}
	/**
	 * tickごとにBlockEntityの処理を呼び出すためのtickerを登録する
	 */
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (type == BlockInit.EXAMPLE_BLOCK_ENTITY_TYPE.get()) {
			return (llevel, lpos, lstate, lentity) -> {
				if (lentity instanceof ExampleBlockEntity rentity) {
					if (llevel.isClientSide()) {
						rentity.clientTick();
					} else {
						rentity.serverTick();
					}
				}
			};
		}
		return null;
	}

	/**
	 * ブロックがワールドに配置された時、BlockEntityも同時に作成する
	 */
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ExampleBlockEntity(pos, state);
	}

}
