package skytheory.example.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import skytheory.example.data.loot.ExampleBlockLoot;
import skytheory.example.data.loot.ExampleEntityLoot;

public class ExampleLootTableProvider extends LootTableProvider {

	public ExampleLootTableProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
		return List.of(
				Pair.of(ExampleEntityLoot::new, LootContextParamSets.ENTITY),
				Pair.of(ExampleBlockLoot::new, LootContextParamSets.BLOCK)
				);
	}
	
	// 元々のバリデータが限定された用途にしか使えないため、無効化しておく
	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {}
}
