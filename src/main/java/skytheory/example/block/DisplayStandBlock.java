package skytheory.example.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import skytheory.example.block.entity.DisplayStandEntity;

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
public class DisplayStandBlock extends Block implements EntityBlock {

	/**
	 *  ブロックの当たり判定などの大きさ、引数はxyz始点、xyz終点の順
	 *  Block.boxはちょうどテクスチャのピクセル数と同じになるように1/16してくれるのは分かりやすい
	 *  1.0dが1ブロックの辺の大きさになる数値で指定したいのなら、Shapes.boxを使うこと
	 */
	public static final VoxelShape PLATE_AABB = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 4.0d, 16.0d);

	public DisplayStandBlock(Properties properties) {
		super(properties);
	}
 
	/**
	 * ブロックがワールドに配置された時、BlockEntityも同時に作成する
	 */
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DisplayStandEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			if (level.getBlockEntity(pos) instanceof DisplayStandEntity entity && player instanceof ServerPlayer serverPlayer) {
				// Forgeの用意したフックを用いて、BlockPosの情報付きでGUIを開かせる
				NetworkHooks.openScreen(serverPlayer, entity, pos);
			}
			return InteractionResult.CONSUME;
		}
	}

	/**
	 * ブロックの当たり判定を指定する
	 * もろもろまとめて指定したいなら、getShapeを用いること
	 * 割と力技で描画問題を解決してるので、あんまり当てにならないかも
	 */
	@Override
	public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_,
			CollisionContext p_60575_) {
		if (CollisionContext.empty().equals(p_60575_)) {
			// 基本的に当たり判定を指定したい形状を返せば大丈夫なのだが、描画の都合上こういう実装に
			// getVisualShapeではなく、こちらの値がクリッピングに使用されているらしいので、それを解決する
			// もしかしたらもっと良い方法があるのかも、というか存在してほしい、そして教えてほしい
			return Shapes.block();
		}
		return PLATE_AABB;
	}
	
	/**
	 * 隣のブロックの面を描画するかの判定に用いる
	 * 基本的に当たり判定と同じで大丈夫だと思う
	 */
	@Override
	public VoxelShape getOcclusionShape(BlockState p_60578_, BlockGetter p_60579_, BlockPos p_60580_) {
		return PLATE_AABB;
	}

}
