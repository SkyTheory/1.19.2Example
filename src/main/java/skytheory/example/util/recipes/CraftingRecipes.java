package skytheory.example.util.recipes;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class CraftingRecipes {

	/*
	 *  アイテム9個からブロックへの圧縮レシピを作成する
	 */

	public static void packing(Consumer<FinishedRecipe> register, ItemLike result, ItemLike... ingredients) {
		packing(register, null, result, ingredients);
	}

	public static void packing(Consumer<FinishedRecipe> register, ItemLike result, TagKey<Item> ingredients) {
		packing(register, null, result, ingredients);
	}

	public static void packing(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, ItemLike result, ItemLike... ingredients) {
		packingInternal(register, location, RecipeHelper.unlockTrigger(ingredients), result, Ingredient.of(ingredients));
	}

	public static void packing(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, ItemLike result, TagKey<Item> ingredients) {
		packingInternal(register, location, RecipeHelper.unlockTrigger(ingredients), result, Ingredient.of(ingredients));
	}

	private static void packingInternal(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, CriterionTriggerInstance trigger, ItemLike result, Ingredient ingredient) {
		// こちらは定型レシピ
		// 引数が完成品、defineで材料を、patternでアイテムの並べ方を指定する
		// 複数回defineを呼び出し、異なる記号または文字とアイテムを指定することで、複数の材料を用いたレシピが作成可能
		ShapedRecipeBuilder.shaped(result)
		.define('#', ingredient)
		.pattern("###")
		.pattern("###")
		.pattern("###")
		.unlockedBy("has_ingredients", trigger)
		.save(register, shapedLocation(location, result));
	}
	/*
	 *  ブロックからアイテム9個への展開レシピを作成する
	 */

	public static void unpacking(Consumer<FinishedRecipe> register, ItemLike result, ItemLike... ingredients) {
		unpacking(register, null, result, ingredients);
	}

	public static void unpacking(Consumer<FinishedRecipe> register, ItemLike result, TagKey<Item> ingredients) {
		unpacking(register, null, result, ingredients);
	}

	public static void unpacking(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, ItemLike result, ItemLike... ingredients) {
		unpackingInternal(register, location, RecipeHelper.unlockTrigger(ingredients), result, Ingredient.of(ingredients));
	}

	public static void unpacking(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, ItemLike result, TagKey<Item> ingredients) {
		unpackingInternal(register, location, RecipeHelper.unlockTrigger(ingredients), result, Ingredient.of(ingredients));
	}

	private static void unpackingInternal(Consumer<FinishedRecipe> register, @Nullable ResourceLocation location, CriterionTriggerInstance trigger, ItemLike result, Ingredient ingredient) {
		// こちらは不定形レシピ
		// 1つ目の引数が完成品、2つ目が個数
		ShapelessRecipeBuilder.shapeless(result, 9)
		.requires(ingredient)
		.unlockedBy("has_ingredients", trigger)
		.save(register, shapelessLocation(location, result));
	}

	private static ResourceLocation shapedLocation(ResourceLocation location, ItemLike item) {
		return location != null ? location : RecipeHelper.getLocation("crafting_shaped", item);
	}

	private static ResourceLocation shapelessLocation(ResourceLocation location, ItemLike item) {
		return location != null ? location : RecipeHelper.getLocation("crafting_shapeless", item);
	}
}
