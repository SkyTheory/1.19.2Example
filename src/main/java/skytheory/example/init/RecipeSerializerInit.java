package skytheory.example.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.recipe.SimpleProcessorRecipe;
import skytheory.example.recipe.SimpleProcessorRecipeSerializer;

public class RecipeSerializerInit {


	public static final DeferredRegister<RecipeSerializer<?>> REGISTRY =
	DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExampleMod.MODID);
	
	public static final RegistryObject<RecipeSerializer<SimpleProcessorRecipe>> SIMPLE_PROCESSOR_TYPE =
			REGISTRY.register("simple_processor", () -> new SimpleProcessorRecipeSerializer());
	
	
}
