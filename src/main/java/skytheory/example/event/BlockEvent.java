package skytheory.example.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import skytheory.example.ExampleMod;
import skytheory.example.block.ItemHandlerBlock;
import skytheory.example.capability.DataProvider;

public class BlockEvent {

	public static final ResourceLocation EXAMPLE_INVENTORY = new ResourceLocation(ExampleMod.MODID);
	
	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
		if (event.getObject() instanceof ItemHandlerBlock<?> block) {
			ICapabilityProvider cap = new DataProvider<>(ForgeCapabilities.ITEM_HANDLER, block::getItemHandler, block::getSerializer);
			event.addCapability(EXAMPLE_INVENTORY, cap);
		}
	}
}
