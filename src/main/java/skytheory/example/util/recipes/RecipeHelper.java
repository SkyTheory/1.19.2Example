package skytheory.example.util.recipes;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import skytheory.example.ExampleMod;

public class RecipeHelper {
	
	/*
	 * レシピ解除用進捗のトリガーを作成
	 */

	public static InventoryChangeTrigger.TriggerInstance unlockTrigger(ItemLike... ingredients){
		return InventoryChangeTrigger.TriggerInstance.hasItems(ingredients);
	}

	public static InventoryChangeTrigger.TriggerInstance unlockTrigger(TagKey<Item> ingredients){
		return inventoryTrigger(ItemPredicate.Builder.item().of(ingredients).build());
	}

	private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... predicates) {
		return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, predicates);
	}

	/*
	 * レシピ保存用のResourceLocationを作成
	 */
	
	public static ResourceLocation getLocation(String directory, ItemLike item) {
		return new ResourceLocation(ExampleMod.MODID, directory + "/" + ForgeRegistries.ITEMS.getKey(item.asItem()).getPath());
	}
}
