package skytheory.example.event;

import com.mojang.logging.LogUtils;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import skytheory.example.advancement.FallingDamageCriterionTrigger;
import skytheory.example.advancement.ItemThrownCriterionTrigger;

public class AdvancementEvent {

	@SubscribeEvent
	public static void onEntityDamaged(LivingDamageEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			CriterionTrigger<?> trigger = CriteriaTriggers.getCriterion(FallingDamageCriterionTrigger.CRITERION_ID);
			if (trigger instanceof FallingDamageCriterionTrigger t) {
				t.trigger(player, event.getSource());
			}
		}
	}

	@SubscribeEvent
	public static void onItemThrown(ItemTossEvent event) {
		if (event.getPlayer() instanceof ServerPlayer player) {
			LogUtils.getLogger().info(CriteriaTriggers.getCriterion(ItemThrownCriterionTrigger.CRITERION_ID).toString());
			LogUtils.getLogger().info(event.getEntity().toString());
			LogUtils.getLogger().info(event.getEntity().getItem().toString());
			CriterionTrigger<?> trigger = CriteriaTriggers.getCriterion(ItemThrownCriterionTrigger.CRITERION_ID);
			if (trigger instanceof ItemThrownCriterionTrigger t) {
				t.trigger(player, event.getEntity().getItem());
			}
		}
	}
	
}
