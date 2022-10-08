package skytheory.example.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import skytheory.example.init.BlockInit;

public class ExampleBlockEntity extends BlockEntity {

	public ExampleBlockEntity(BlockPos pos, BlockState state) {
		super(BlockInit.EXAMPLE_BLOCK_ENTITY_TYPE.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		// tickごとに行う処理をここに記述、引数にはblockEntityのインスタンスが入る
	}
}
