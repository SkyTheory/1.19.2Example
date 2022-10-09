package skytheory.example.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
 */
public class ExampleEntityBlock extends Block implements EntityBlock {

	public ExampleEntityBlock(Properties properties) {
		super(properties);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return type == BlockInit.EXAMPLE_BLOCK_ENTITY_TYPE.get() ? ExampleBlockEntity::tick : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ExampleBlockEntity(pos, state);
	}

}
