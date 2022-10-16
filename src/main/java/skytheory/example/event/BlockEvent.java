package skytheory.example.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import skytheory.example.ExampleMod;
import skytheory.example.block.entity.ExampleBlockEntity;
import skytheory.example.capability.BlockEntityItemHandler;
import skytheory.example.capability.CapProvider;

public class BlockEvent {

	public static final ResourceLocation EXAMPLE_INVENTORY = new ResourceLocation(ExampleMod.MODID);
	
	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
		if (event.getObject() instanceof ExampleBlockEntity block) {
			IItemHandler handler = new BlockEntityItemHandler(block, 1);
			ICapabilityProvider cap = new CapProvider<IItemHandler>(ForgeCapabilities.ITEM_HANDLER, handler);
			event.addCapability(EXAMPLE_INVENTORY, cap);
		}
	}
}
