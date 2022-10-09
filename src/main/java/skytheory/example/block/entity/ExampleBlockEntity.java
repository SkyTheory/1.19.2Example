package skytheory.example.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import skytheory.example.init.BlockInit;

public class ExampleBlockEntity extends BlockEntity {

	public ExampleBlockEntity(BlockPos pos, BlockState state) {
		super(BlockInit.EXAMPLE_BLOCK_ENTITY_TYPE.get(), pos, state);
	}

	public void clientTick() {
	}
	
	public void serverTick() {
	}
}
