package skytheory.example.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import skytheory.example.ExampleMod;
import skytheory.example.recipe.SimpleProcessorRecipe;

public class SimpleProcessorRecipeProvider implements DataProvider {

	public final DataGenerator generator;
	public final Set<SimpleProcessorRecipe> recipes;
	
	
	public SimpleProcessorRecipeProvider(DataGenerator pGenerator) {
		this.generator = pGenerator;
		this.recipes = new HashSet<>();
	}

	@Override
	public void run(CachedOutput cache) throws IOException {
		this.addRecipes();
		for (SimpleProcessorRecipe recipe : recipes) {
			ResourceLocation id = recipe.getId();
			Path path = this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/recipes/simple_processor/" + id.getPath() + ".json");
			DataProvider.saveStable(cache, recipe.toJson(), path);
		}
	}

	public void addRecipes() {
		
		this.recipes.add(new SimpleProcessorRecipe(
				// ID
				new ResourceLocation(ExampleMod.MODID, "golden_apple"),
				// 完成品
				new ItemStack(Items.GOLDEN_APPLE),
				// 材料
				List.of(
						Ingredient.of(Items.GOLD_INGOT),
						Ingredient.of(Items.APPLE)
						)));
		
		this.recipes.add(new SimpleProcessorRecipe(
				// ID
				new ResourceLocation(ExampleMod.MODID, "solidify_blaze_powder"),
				// 完成品
				new ItemStack(Items.BLAZE_ROD),
				// 材料
				List.of(
						Ingredient.of(Items.BLAZE_POWDER),
						Ingredient.of(Items.BLAZE_POWDER),
						Ingredient.of(Items.BLAZE_POWDER),
						Ingredient.of(Items.MAGMA_CREAM)
						)));

		// NBT付きのアイテムを用いるレシピのサンプル
		ItemStack healingPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING);
		
		this.recipes.add(new SimpleProcessorRecipe(
				// ID
				new ResourceLocation(ExampleMod.MODID, "detoxify_potate"),
				// 完成品
				new ItemStack(Items.POTATO),
				// 材料
				List.of(
						Ingredient.of(Items.POISONOUS_POTATO),
						// 通常のIngredientはItemStackが引数のものであっても
						// Itemの種類しか判定してくれないため、こちらを使う
						StrictNBTIngredient.of(healingPotion)
						)));
		
	}

	@Override
	public String getName() {
		return "Simple Processor Recipes";
	}

}
