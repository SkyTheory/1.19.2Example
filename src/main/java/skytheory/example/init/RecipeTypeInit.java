package skytheory.example.init;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.recipe.SimpleProcessorRecipe;

public class RecipeTypeInit {

	public static final DeferredRegister<RecipeType<?>> REGISTRY =
	DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ExampleMod.MODID);
	
	public static final RegistryObject<RecipeType<SimpleProcessorRecipe>> SIMPLE_PROCESSOR_TYPE =
			REGISTRY.register("simple_processor", () -> new RecipeType<SimpleProcessorRecipe>() {});
	
}
