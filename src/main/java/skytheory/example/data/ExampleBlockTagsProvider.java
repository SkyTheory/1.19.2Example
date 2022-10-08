package skytheory.example.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import skytheory.example.init.BlockInit;
import skytheory.example.tag.ExampleTags;

public class ExampleBlockTagsProvider extends BlockTagsProvider {

	public ExampleBlockTagsProvider(DataGenerator generator, String modId, ExistingFileHelper existingFileHelper) {
		super(generator, modId, existingFileHelper);
	}

	@Override
	protected void addTags() {

		/*
		 * 回収にツールが必要なブロックを作成した場合、このようにツールの種類をタグで指定すること
		 * 独自Tierを作る場合、net.minecraftforge.common.TierSotringRegistryも参照
		 */
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockInit.MACGUFFIN_ORE.get());
		this.tag(BlockTags.NEEDS_STONE_TOOL).add(BlockInit.MACGUFFIN_ORE.get());
		
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockInit.MACGUFFIN_BLOCK.get());
		this.tag(BlockTags.NEEDS_STONE_TOOL).add(BlockInit.MACGUFFIN_BLOCK.get());

		
		// マクガフィン鉱石を鉱石の一種として登録 e.g.) ダイヤモンド鉱石
		this.tag(ExampleTags.Blocks.ORES_MACGUFFIN)
		.add(BlockInit.MACGUFFIN_ORE.get())
		.add(BlockInit.DEEPSLATE_MACGUFFIN_ORE.get());
		
		// マクガフィンブロックを宝石ブロックの一種として登録 e.g.) ダイヤモンドブロック
		this.tag(ExampleTags.Blocks.STORAGE_BLOCKS_MACGUFFIN)
		.add(BlockInit.MACGUFFIN_BLOCK.get());

		// マクガフィン鉱石を地形生成用のタグに登録（石を置き換えて生成するブロックをここへ）
		this.tag(Tags.Blocks.ORES_IN_GROUND_STONE)
		.add(BlockInit.MACGUFFIN_ORE.get());

		// 深層マクガフィン鉱石を地形生成用のタグに登録（深層岩を置き換えて生成するブロックをここへ）
		this.tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
		.add(BlockInit.DEEPSLATE_MACGUFFIN_ORE.get());

		/*
		// もしネザーラックを置き換えて生成するブロックがあるならこのように記述
		this.tag(Tags.Items.ORES_IN_GROUND_NETHERRACK)
		.add(BLOCKInit.NETHERRACK_SOMETHING_ORE.get());
		*/
	}
}
