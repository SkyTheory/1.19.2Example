package skytheory.example.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import skytheory.example.ExampleMod;
import skytheory.example.init.BlockInit;
import skytheory.example.init.EntityInit;
import skytheory.example.init.ItemInit;

public class ExampleLaungageProviderEN extends LanguageProvider {

	public ExampleLaungageProviderEN(DataGenerator gen, String modid) {
		super(gen, modid, "en_us");
	}

	@Override
	protected void addTranslations() {
		this.add("itemGroup." + ExampleMod.MODID, "Example Mod");
		this.addItem(ItemInit.MUFFIN, "Muffin");
		this.addItem(ItemInit.MACGUFFIN, "Macguffin");
		this.addBlock(BlockInit.MUFFIN_BLOCK, "Muffin Block");
		this.addBlock(BlockInit.MACGUFFIN_ORE, "Macguffin Ore");
		this.addBlock(BlockInit.DEEPSLATE_MACGUFFIN_ORE, "Deepslate Macguffin Ore");
		this.addBlock(BlockInit.MACGUFFIN_BLOCK, "Macguffin Block");
		this.addBlock(BlockInit.EXAMPLE_ENTITY_BLOCK, "Example Entity Block");
		this.addEntityType(EntityInit.EXAMPLE_ENTITY, "Understudy");
	}
}
