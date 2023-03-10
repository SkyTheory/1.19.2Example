package skytheory.example.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import skytheory.example.ExampleMod;
import skytheory.example.init.BlockInit;
import skytheory.example.init.EntityInit;
import skytheory.example.init.ItemInit;

public class ExampleLaungageProviderJP extends LanguageProvider {

	public ExampleLaungageProviderJP(DataGenerator gen, String modid) {
		super(gen, modid, "ja_jp");
	}

	@Override
	protected void addTranslations() {
		this.add("itemGroup." + ExampleMod.MODID, "Example Mod");
		this.addItem(ItemInit.MUFFIN, "マフィン");
		this.addItem(ItemInit.MACGUFFIN, "マクガフィン");
		this.addItem(ItemInit.EXAMPLE_SPAWN_EGG, "アンダースタディのスポーンエッグ");
		this.addBlock(BlockInit.MUFFIN_BLOCK, "マフィンブロック");
		this.addBlock(BlockInit.MACGUFFIN_ORE, "マクガフィン鉱石");
		this.addBlock(BlockInit.DEEPSLATE_MACGUFFIN_ORE, "深層マクガフィン鉱石");
		this.addBlock(BlockInit.MACGUFFIN_BLOCK, "マクガフィンブロック");
		this.addBlock(BlockInit.EXAMPLE_ENTITY_BLOCK, "ブロックエンティティサンプル");
		this.addEntityType(EntityInit.EXAMPLE_ENTITY, "アンダースタディ");
		this.add("st_example:advancements.root_title", "足元注意！");
		this.add("st_example:advancements.root_description", "落下ダメージを受ける");
		this.add("st_example:advancements.macguffin_title", "チェーホフの銃");
		this.add("st_example:advancements.macguffin_description", "マクガフィンを投げ捨てる");
	}
}
