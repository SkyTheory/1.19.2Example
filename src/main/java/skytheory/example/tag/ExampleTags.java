package skytheory.example.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import skytheory.example.ExampleMod;

/**
 * Modで使うタグを保持しておくクラス
 * 旧鉱石辞書のような、色々なModからタグの名前でレシピを追加する時に使う
 */
public class ExampleTags {

	/**
	 * 同じ文字列を何度も入力する時は、こうして定数にしておくのがお勧め
	 * スペルを1文字間違ったばかりに不具合が発生した、といった場合に原因特定に長時間を費やしたくなければ！
	 */
	private static final String MACGUFFIN = "macguffin"; 
	
	/*
	 * 以下が当サンプルModで作成したタグ
	 */
	public static final class Blocks {
		public static final TagKey<Block> ORES_MACGUFFIN = oreBlock(MACGUFFIN);
		public static final TagKey<Block> STORAGE_BLOCKS_MACGUFFIN = storageBlock(MACGUFFIN);
		
	}
	
	public static final class Items {
		public static final TagKey<Item> ORES_MACGUFFIN = oreItem(MACGUFFIN);
		public static final TagKey<Item> GEMS_MACGUFFIN = gemItem(MACGUFFIN);
		public static final TagKey<Item> STORAGE_BLOCKS_MACGUFFIN = storageItem(MACGUFFIN);
	} 

	/**
	 * 鉱石やインゴットなど、他のmodでも参照するようなタグを作るなら、名前空間はforgeのものを使う
	 */
	static TagKey<Block> blockTag(String name) {
		return BlockTags.create(new ResourceLocation("forge", name));
	}

	/**
	 * 鉱石やインゴットなど、他のmodでも参照するようなタグを作るなら、名前空間はforgeのものを使う
	 */
	static TagKey<Item> itemTag(String name) {
		return ItemTags.create(new ResourceLocation("forge", name));
	}
	
	/**
	 * Mod内のみで完結し、他のModとの連携を考える必要のないタグを付けるのならこちら
	 */
	static TagKey<Block> blockTagInternal(String name) {
		return BlockTags.create(new ResourceLocation(ExampleMod.MODID, name));
	}

	/**
	 * Mod内のみで完結し、他のModとの連携を考える必要のないタグを付けるのならこちら
	 */
	static TagKey<Item> itemTagInternal(String name) {
		return ItemTags.create(new ResourceLocation(ExampleMod.MODID, name));
	}
	
	// タイプミスなどによる誤登録を防止するため、こういったショートカットを用意するのも手
	
	static TagKey<Block> oreBlock(String name) {
		return blockTag("ores/" + name);
	}
	
	static TagKey<Block> storageBlock(String name) {
		return blockTag("storage_blocks/" + name);
	}
	
	static TagKey<Block> storageRawMaterialBlock(String name) {
		return blockTag("storage_blocks/raw_"+ name);
	}

	static TagKey<Item> rawMaterial(String name) {
		return itemTag("raw_materials/" + name);
	}

	static TagKey<Item> oreItem(String name) {
		return itemTag("ores/" + name);
	}
	
	static TagKey<Item> dustItem(String name) {
		return itemTag("dusts/" + name);
	}

	static TagKey<Item> nuggetItem(String name) {
		return itemTag("nuggets/" + name);
	}

	static TagKey<Item> ingotItem(String name) {
		return itemTag("ingots/" + name);
	}
	
	static TagKey<Item> gemItem(String name) {
		return itemTag("gems/" + name);
	}

	static TagKey<Item> storageItem(String name) {
		return itemTag("storage_blocks/" + name);
	}
	
	static TagKey<Item> storageRawMaterialItem(String name) {
		return itemTag("storage_blocks/raw_" + name);
	}
	
}
