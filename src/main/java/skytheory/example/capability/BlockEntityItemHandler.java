package skytheory.example.capability;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class BlockEntityItemHandler extends ItemStackHandler {

	private final BlockEntity block;
	
	public BlockEntityItemHandler(BlockEntity block, int size) {
		super(size);
		this.block = block;
	}
	
	@Override
	protected void onContentsChanged(int slot) {
		block.setChanged();
	}
}
