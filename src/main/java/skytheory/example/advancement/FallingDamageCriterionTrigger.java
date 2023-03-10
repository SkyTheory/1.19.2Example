package skytheory.example.advancement;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import skytheory.example.ExampleMod;

public class FallingDamageCriterionTrigger extends SimpleCriterionTrigger<FallingDamageCriterionTrigger.TriggerInstance> {

	public static final ResourceLocation CRITERION_ID = new ResourceLocation(ExampleMod.MODID, "falling_damage");

	@Override
	public ResourceLocation getId() {
		return CRITERION_ID;
	}

	@Override
	protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite playerPredicate, DeserializationContext ctx) {
		return new TriggerInstance(playerPredicate);
	}

	public void trigger(ServerPlayer player, DamageSource source) {
		this.trigger(player, (triggerInstance) -> {
			// ModderというよりPacker向けではあるが、この部分をtriggerInstanceの持つ値を基に判定などにするとjson側の記述で設定できる項目が増える
			return source.isFall();
		});
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {

		public TriggerInstance(EntityPredicate.Composite playerPredicate) {
			super(FallingDamageCriterionTrigger.CRITERION_ID, playerPredicate);
		}

		public JsonObject serializeToJson(SerializationContext ctx) {
			JsonObject jsonobject = super.serializeToJson(ctx);
			return jsonobject;
		}
	}

}
