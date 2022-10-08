package skytheory.example.util.recipes;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class CookingRecipes {

	/*
	 * かまど精錬
	 */
	
	public static void smelting(Consumer<FinishedRecipe> register, ItemLike result, ItemLike... ingredients) {
		smelting(register, null, 0.1f, 200, result, ingredients);
	}

	public static void smelting(Consumer<FinishedRecipe> register, ItemLike result, TagKey<Item> ingredients) {
		smelting(register, null, 0.1f, 200, result, ingredients);
	}

	public static void smelting(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, float exp, int time, ItemLike result, ItemLike... ingredients) {
		smeltingInternal(register, location, RecipeHelper.unlockTrigger(ingredients), exp, time, result, Ingredient.of(ingredients));
	}

	public static void smelting(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, float exp, int time, ItemLike result, TagKey<Item> ingredients) {
		smeltingInternal(register, location, RecipeHelper.unlockTrigger(ingredients), exp, time, result, Ingredient.of(ingredients));
	}

	public static void smeltingInternal(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, CriterionTriggerInstance trigger, float exp, int time, ItemLike result, Ingredient ingredient) {
		SimpleCookingRecipeBuilder.smelting(ingredient, result, exp, time)
		.unlockedBy("has_ingredients", trigger)
		.save(register, smeltingLocation(location, result));
	}

	private static ResourceLocation smeltingLocation(ResourceLocation location, ItemLike item) {
		return location != null ? location : RecipeHelper.getLocation("smelting", item);
	}
	
	/*
	 * 溶鉱炉精錬
	 */

	public static void blasting(Consumer<FinishedRecipe> register, ItemLike result, ItemLike... ingredients) {
		blasting(register, null, 0.1f, 100, result, ingredients);
	}

	public static void blasting(Consumer<FinishedRecipe> register, ItemLike result, TagKey<Item> ingredients) {
		blasting(register, null, 0.1f, 100, result, ingredients);
	}

	public static void blasting(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, float exp, int time, ItemLike result, ItemLike... ingredients) {
		blastingInternal(register, location, RecipeHelper.unlockTrigger(ingredients), exp, time, result, Ingredient.of(ingredients));
	}

	public static void blasting(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, float exp, int time, ItemLike result, TagKey<Item> ingredients) {
		blastingInternal(register, location, RecipeHelper.unlockTrigger(ingredients), exp, time, result, Ingredient.of(ingredients));
	}

	public static void blastingInternal(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, CriterionTriggerInstance trigger, float exp, int time, ItemLike result, Ingredient ingredient) {
		SimpleCookingRecipeBuilder.blasting(ingredient, result, exp, time)
		.unlockedBy("has_ingredients", trigger)
		.save(register, blastingLocation(location, result));
	}

	private static ResourceLocation blastingLocation(ResourceLocation location, ItemLike item) {
		return location != null ? location : RecipeHelper.getLocation("blasting", item);
	}
	
}
