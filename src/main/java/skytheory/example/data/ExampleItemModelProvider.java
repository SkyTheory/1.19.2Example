package skytheory.example.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import skytheory.example.init.ItemInit;

public class ExampleItemModelProvider extends ItemModelProvider {

	public ExampleItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
		super(generator, modid, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		this.basicItem(ItemInit.MACGUFFIN.get());
		this.basicItem(ItemInit.MUFFIN.get());
		this.basicBlockItem(ItemInit.MACGUFFIN_BLOCK.get());
		this.basicBlockItem(ItemInit.MACGUFFIN_ORE.get());
		this.basicBlockItem(ItemInit.DEEPSLATE_MACGUFFIN_ORE.get());
		this.basicBlockItem(ItemInit.MUFFIN_BLOCK.get());
		this.basicBlockItem(ItemInit.EXAMPLE_ENTITY_BLOCK.get());
		this.spawnEgg(ItemInit.EXAMPLE_SPAWN_EGG.get());
		
	}

	public void spawnEgg(Item item) {
		this.getBuilder(ForgeRegistries.ITEMS.getKey(item).toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
	}

	public void basicBlockItem(Item item) {
		if (item instanceof BlockItem blockItem) {
			ResourceLocation path = this.getBlockLocation(blockItem.getBlock());
			ResourceLocation parent = new ResourceLocation(path.getNamespace(), "block/" + path.getPath());
			this.getBuilder((path).getPath()).parent(new UncheckedModelFile(parent));
		} else {
			String message = String.format("%s is not Block Item.", ForgeRegistries.ITEMS.getKey(item).toString());
			throw new UnsupportedOperationException(message);
		}
	}

	public ResourceLocation getBlockLocation(Block block) {
		return ForgeRegistries.BLOCKS.getKey(block);
	}
}
