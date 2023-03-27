package skytheory.example.data;

import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import skytheory.example.init.ItemInit;
import skytheory.example.tag.ExampleTags;
import skytheory.example.util.recipes.CookingRecipes;
import skytheory.example.util.recipes.CraftingRecipes;

public class VannilaDeviceRecipeProvider extends RecipeProvider {

	public VannilaDeviceRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	protected void buildCraftingRecipes(Consumer<FinishedRecipe> register) {
		/*
		 * アイテム9個からブロック1個への圧縮、及びブロックからアイテム9個への分解レシピ
		 */
		CraftingRecipes.packing(register, ItemInit.MACGUFFIN_BLOCK.get(), ExampleTags.Items.GEMS_MACGUFFIN);
		CraftingRecipes.packing(register, ItemInit.MUFFIN_BLOCK.get(), ItemInit.MUFFIN.get());
		CraftingRecipes.unpacking(register, ItemInit.MACGUFFIN.get(), ExampleTags.Items.STORAGE_BLOCKS_MACGUFFIN);
		CraftingRecipes.unpacking(register, ItemInit.MUFFIN.get(), ItemInit.MUFFIN_BLOCK.get());
		
		/*
		 * マクガフィン鉱石の精錬レシピ
		 */
		CookingRecipes.smelting(register, ItemInit.MUFFIN.get(), ExampleTags.Items.GEMS_MACGUFFIN);
		CookingRecipes.blasting(register, ItemInit.MUFFIN.get(), ExampleTags.Items.GEMS_MACGUFFIN);
		
		/*
		 * その他レシピを追加したいのであれば、RecipeProviderやRecipeSerializerを参考に実装すること
		 */
	}

}