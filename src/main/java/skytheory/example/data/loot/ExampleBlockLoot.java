package skytheory.example.data.loot;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import skytheory.example.init.BlockInit;
import skytheory.example.init.ItemInit;

public class ExampleBlockLoot extends BlockLoot {

	@Override
	protected void addTables() {
		// 壊せばそのままドロップする、いわゆる普通のブロックはこうして登録
		this.dropSelf(BlockInit.MACGUFFIN_BLOCK.get());
		this.dropSelf(BlockInit.EXAMPLE_ENTITY_BLOCK.get());

		// それ以外はaddからBlockLoot.Builderを登録
		// 例えば鉱石の場合はこう
		this.add(BlockInit.MACGUFFIN_ORE.get(), createOreDrop(BlockInit.MACGUFFIN_ORE.get(), ItemInit.MACGUFFIN.get()));

		// レッドストーンやラピスラズリのような、複数個のアイテムをドロップさせたいならBlockLootなどを参考にこう
		this.add(BlockInit.DEEPSLATE_MACGUFFIN_ORE.get(), (block) -> {
			// シルクタッチがあるなら第一引数のブロックを、ないなら第二引数のアイテムをドロップする
			return createSilkTouchDispatchTable(block, LootItem.lootTableItem(ItemInit.MACGUFFIN.get())
					// 爆破で壊されたならドロップアイテムを消滅させる
					.apply(ApplyExplosionDecay.explosionDecay())
					// 3~4個のアイテムをドロップさせる
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 4.0f)))
					// 幸運エンチャントでドロップ数を増加させる
					.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)));
		});

		// シルクタッチで元のブロックを、そうでなければ9個のマフィンをドロップするならこう
		this.add(BlockInit.MUFFIN_BLOCK.get(), (block) -> {
			return createSilkTouchDispatchTable(block, LootItem.lootTableItem(ItemInit.MUFFIN.get())
					.apply(ApplyExplosionDecay.explosionDecay())
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(9.0f))));
		});
		// その他の場合はBlockLootの実装などを参考にして登録すること
	}

	/**
	 * 内容の検証に使うBlockのリストを取得する
	 * ここに登録したBlockの分のデータを作成しないとエラーが発生する
	 */
	protected Iterable<Block> getKnownBlocks() {
		return BlockInit.BLOCKS_REGISTRY.getEntries().stream().map(obj -> obj.get()).toList();
	}
}
