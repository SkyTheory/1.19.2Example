package skytheory.example.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import skytheory.example.ExampleMod;
import skytheory.example.init.BlockInit;
import skytheory.example.init.EntityInit;
import skytheory.example.init.ItemInit;

public class LaungageProviderEN extends LanguageProvider {

	public LaungageProviderEN(DataGenerator gen, String modid) {
		super(gen, modid, "en_us");
	}

	@Override
	protected void addTranslations() {
		this.add("itemGroup." + ExampleMod.MODID, "Example Mod");
		this.addItem(ItemInit.MUFFIN, "Muffin");
		this.addItem(ItemInit.MACGUFFIN, "Macguffin");
		this.addItem(ItemInit.EXAMPLE_SPAWN_EGG, "Understudy Spawn Egg");
		this.addBlock(BlockInit.MUFFIN_BLOCK, "Muffin Block");
		this.addBlock(BlockInit.MACGUFFIN_ORE, "Macguffin Ore");
		this.addBlock(BlockInit.DEEPSLATE_MACGUFFIN_ORE, "Deepslate Macguffin Ore");
		this.addBlock(BlockInit.MACGUFFIN_BLOCK, "Macguffin Block");
		this.addBlock(BlockInit.DISPLAY_STAND, "Display Stand");
		this.addBlock(BlockInit.SIMPLE_PROCESSOR, "Simple Processor");
		this.addEntityType(EntityInit.EXAMPLE_ENTITY, "Understudy");
		this.add("st_example:advancements.root_title", "Watch your step!");
		this.add("st_example:advancements.root_description", "Hurt by falling.");
		this.add("st_example:advancements.macguffin_title", "Chekhov's Gun");
		this.add("st_example:advancements.macguffin_description", "Throw Macguffin.");
	}
}
