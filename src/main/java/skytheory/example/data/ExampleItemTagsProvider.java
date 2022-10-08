package skytheory.example.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import skytheory.example.init.ItemInit;
import skytheory.example.tag.ExampleTags;

public class ExampleItemTagsProvider extends ItemTagsProvider {

	public ExampleItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider, String modId, ExistingFileHelper existingFileHelper) {
		super(generator, blockTagsProvider, modId, existingFileHelper);
	}

	@Override
	protected void addTags() {
		// マクガフィン鉱石を鉱石の一種として登録 e.g.) ダイヤモンド鉱石
		this.tag(ExampleTags.Items.ORES_MACGUFFIN)
		.add(ItemInit.MACGUFFIN_ORE.get())
		.add(ItemInit.DEEPSLATE_MACGUFFIN_ORE.get());
		
		// マクガフィンを宝石の一種として登録 e.g.) ダイヤモンド
		this.tag(ExampleTags.Items.GEMS_MACGUFFIN)
		.add(ItemInit.MACGUFFIN.get());
		
		// マクガフィンブロックを宝石ブロックの一種として登録 e.g.) ダイヤモンドブロック
		this.tag(ExampleTags.Items.STORAGE_BLOCKS_MACGUFFIN)
		.add(ItemInit.MACGUFFIN_BLOCK.get());

		// マクガフィン鉱石を地形生成用のタグに登録（石を置き換えて生成するブロックをここへ）
		this.tag(Tags.Items.ORES_IN_GROUND_STONE)
		.add(ItemInit.MACGUFFIN_ORE.get());

		// 深層マクガフィン鉱石を地形生成用のタグに登録（深層岩を置き換えて生成するブロックをここへ）
		this.tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE)
		.add(ItemInit.DEEPSLATE_MACGUFFIN_ORE.get());

		/*
		// もしネザーラックを置き換えて生成するブロックがあるならこのタグにくっつける
		this.tag(Tags.Items.ORES_IN_GROUND_NETHERRACK)
		.add(ItemInit.NETHERRACK_SOMETHING_ORE.get());
		*/
	}
}
