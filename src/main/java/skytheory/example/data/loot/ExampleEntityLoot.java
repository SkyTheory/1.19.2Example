package skytheory.example.data.loot;

import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import skytheory.example.init.EntityInit;

public class ExampleEntityLoot extends EntityLoot {

	@Override
	protected void addTables() {
		this.add(
				// ドロップアイテムを定義するルートテーブルを作成
				EntityInit.EXAMPLE_ENTITY.get(), LootTable.lootTable()
				// ドロップアイテムのプールを追加
				// setRollsの値を変えれば複数アイテムを同時に定義できなくもないはずだが
				// 基本的には都度withPoolを書き、個別に判定させた方が楽そうではある
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
						// 骨をドロップする
						.add(LootItem.lootTableItem(Items.BONE)
								// ドロップ数を0~2個に設定
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 2.0f)))
								// ドロップ増加のエンチャントが付与されている時、(0~1) * ドロップ増加のレベル分個数を追加
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0f, 1.0f)))))
				// ドロップアイテムのプールを追加
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
						// パンをドロップする
						.add(LootItem.lootTableItem(Items.BREAD)
								// ドロップ数を1個に設定
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
								// ドロップ増加のエンチャントが付与されている時、(1~2) * ドロップ増加のレベル分個数を追加
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1.0f, 2.0f))))
						// プレイヤーに倒された時のみドロップさせる
						.when(LootItemKilledByPlayerCondition.killedByPlayer()))
				// ドロップアイテムのプールを追加
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
						// ウィザースケルトンの頭をドロップする
						.add(LootItem.lootTableItem(Blocks.WITHER_SKELETON_SKULL)
								// ドロップ数を3個に設定
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0f))))
						// 通常よりも細かくドロップ率を制御できる
						// この場合は0.1 + (ドロップ増加 * 0.3)が最終的なドロップ率になる
						// ドロップ増加なしなら10%、レベル3なら100%
						.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1f, 0.3f)))
				);
	}

	/**
	 * 内容の検証に使うEntityのリストを取得する
	 * ここに登録したEntityの分のデータを作成しないとエラーが発生する
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Iterable<EntityType<?>> getKnownEntities() {
		var entities = EntityInit.ENTITIES_REGISTRY.getEntries().stream().map(obj -> obj.get()).toList();
		return (Iterable<EntityType<?>>) entities;
	}
}
