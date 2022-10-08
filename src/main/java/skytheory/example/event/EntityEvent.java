package skytheory.example.event;

import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import skytheory.example.entity.ExampleEntity;

public class EntityEvent {

	@SubscribeEvent
	public static void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
		if (event.getEntity() instanceof ExampleEntity entity) {
			entity.onItemUseFinish(event.getItem());
		}
	}
}
