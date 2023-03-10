package skytheory.example.advancement;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import skytheory.example.ExampleMod;

public class ItemThrownCriterionTrigger extends SimpleCriterionTrigger<ItemThrownCriterionTrigger.TriggerInstance> {

	public static final ResourceLocation CRITERION_ID = new ResourceLocation(ExampleMod.MODID, "item_thrrown");
	
	@Override
	public ResourceLocation getId() {
		return CRITERION_ID;
	}

	@Override
	protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite playerPredicate, DeserializationContext ctx) {
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
		return new TriggerInstance(playerPredicate, itemPredicate);
	}


	public void trigger(ServerPlayer player, ItemStack stack) {
		this.trigger(player, (triggerInstance) -> {
			// TriggerInstanceの持つItemPredicateから判定する
			return triggerInstance.matches(stack);
		});
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final ItemPredicate item;

		public TriggerInstance(EntityPredicate.Composite playerPredicate, ItemPredicate itemPredicate) {
			super(ItemThrownCriterionTrigger.CRITERION_ID, playerPredicate);
			this.item = itemPredicate;
		}
		
		public boolean matches(ItemStack stack) {
			return (this.item.matches(stack));
		}

		public JsonObject serializeToJson(SerializationContext ctx) {
			JsonObject jsonobject = super.serializeToJson(ctx);
			jsonobject.add("item", this.item.serializeToJson());
			return jsonobject;
		}
	}
	
}
