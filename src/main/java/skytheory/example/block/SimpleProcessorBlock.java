package skytheory.example.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import skytheory.example.block.entity.SimpleProcessorEntity;
import skytheory.example.init.BlockEntityInit;

public class SimpleProcessorBlock extends Block implements EntityBlock {

	public SimpleProcessorBlock(Properties p_49795_) {
		super(p_49795_);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SimpleProcessorEntity(pos, state);
	}

	/**
	 * tickごとにBlockEntityの処理を呼び出すためのtickerを登録する
	 */
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (type == BlockEntityInit.SIMPLE_PROCESSOR.get()) {
			return (tLevel, tPos, tState, tEntity) -> {
				if (tEntity instanceof SimpleProcessorEntity uEntity) {
					if (tLevel.isClientSide()) {
						uEntity.clientTick();
					} else {
						uEntity.serverTick();
					}
				}
			};
		}
		return null;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			if (level.getBlockEntity(pos) instanceof SimpleProcessorEntity entity && player instanceof ServerPlayer serverPlayer) {
				// Forgeの用意したフックを用いて、BlockPosの情報付きでGUIを開かせる
				NetworkHooks.openScreen(serverPlayer, entity, pos);
			}
			return InteractionResult.CONSUME;
		}
	}
	
}
