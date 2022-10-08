package skytheory.example.init;

import java.util.List;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.levelgen.ExampleFeature;
import skytheory.example.levelgen.ExampleFeatureConfiguration;

public class LevelGenInit {


	/*
	 * Minecraftの地形生成時に特定の何か（追加鉱石など）を追加したい時、Featureを利用することができる
	 * 
	 * これらの機能は実際には3つのオブジェクトから運用されるもので、地形生成時にチャンク毎に判定される
	 * 
	 * 具体的には
	 * 
	 * PlacedFeature -> チャンク内のどこに配置するか、何チャンクにひとつの割合で生成するかなどの
	 * 配置する場所を設定するためのオブジェクト
	 * 
	 * ConfiguredFeature -> どのブロックを生成するか、どんな半径で生成するか、などの値を保持するオブジェクト
	 * 
	 * Feature -> ConfiguredFeatureで指定された値をもとに、実際に地形を生成するメソッドを持つオブジェクト
	 * 
	 * 単純な鉱石追加程度であれば、データパックにjsonファイルを記述するだけで十分
	 * デフォルトにない地形生成パターンを作成したい場合、またはどうしてもjava上で記述したい場合には
	 * DeferredRegisterを用いて各オブジェクトを登録することが可能
	 * 
	 * 本来ならこれらはバイオーム情報の生成時に一括で設定しなければならないのだが
	 * BiomeModifierという機能を利用すれば後から登録及び削除が可能
	 * デフォルトでForgeが用意しているものとして、net.minecraftforge.common.world.ForgeBiomeModifiersが存在する
	 * 
	 * 当Modでは3つの追加地形生成サンプルを用意してある
	 * jsonの記述のみでバニラで用意されたfeatureを指定し、水底に金鉱石を生成する
	 * 同様に、jsonのみの記述で追加ブロックであるマクガフィン鉱石を地下に生成する
	 * javaの記述のみでグロウストーンの柱の生成処理を追加する
	 * もちろんjavaとjsonを組み合わせて使用することも可能
	 * おそらく大半の地形生成はjsonのみ、あるいはFeatureのみ作成、残りはjsonの記述で事足りる
	 * そして、jsonファイルで記述するのが一番楽だろう
	 * 
	 * https://minecraft.fandom.com/wiki/Custom_feature
	 * ここなどが参考になる
	 * 
	 * デフォルトの地形生成から変更すると、javaからの登録、jsonからの登録どちらであるかにかかわらず
	 * ワールド生成時及び初回読み込み時に警告が出るものの、きちんと記述できていれば問題はないはず
	 * どうしても警告を出したくない場合、地形生成を諦めるか、Mixinでlifecycleの値を強引に変更するなどしかない
	 * ちなみに記述が間違っているなら、ほとんどの場合はワールド生成前にバリデーションで弾かれるはず
	 */

	// Registries

	public static final DeferredRegister<Feature<?>> FEATURES_REGISTRY =
			DeferredRegister.create(ForgeRegistries.FEATURES, ExampleMod.MODID);

	public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES_REGISTRY =
			DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, ExampleMod.MODID);

	public static final DeferredRegister<PlacedFeature> PLACED_FEATURES_REGISTRY =
			DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, ExampleMod.MODID);

	public static final DeferredRegister<BiomeModifier> BIOME_MODIFIERS_REGISTRY =
			DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ExampleMod.MODID);

	/**
	 * Feature
	 * 実際の地形生成処理の部分（座標とコンフィグが渡されて、それを元に何かしらをワールドに設置する）
	 * デフォルトにない形状で地形生成を行いたい場合であっても（複合種類の鉱脈を作る、など）
	 * Feature及びFeatureConfigurationのみを自作して、あとはjsonで設定するというのが楽であるように思える
	 */
	static final RegistryObject<Feature<ExampleFeatureConfiguration>> EXAMPLE_FEATUR =
			/*
			 * このサンプルでは全てjava内に記述しているものの、もしデータパックで指定する場合は
			 * configured_featureに置くjsonで"type": "st_example:example_feature"が機能するようになる
			 */
			FEATURES_REGISTRY.register("example_feature", () -> new ExampleFeature(ExampleFeatureConfiguration.CODEC));

	/**
	 * FeatureConfiguration
	 * これとFeatureをセットにして、ConfiguredFeatureを作成する
	 * Featureに渡す、座標以外の情報を保持する
	 * ex)どのブロックを設置するか、どの大きさの地形を生成するか、など
	 */
	static final ExampleFeatureConfiguration config = new ExampleFeatureConfiguration(Blocks.GLOWSTONE.defaultBlockState(), 20);
	
	/**
	 * ConfiguredFeature
	 * FeatureConfigurationとFeatureを保持する
	 */
	static final RegistryObject<ConfiguredFeature<?,?>> EXAMPLE_FEATURE_CONFIGURED =
			// placed_featureに置くjsonで"feature": "st_example:example_feature_config"が機能するようになる
			// ここをコーディングによる処理にすることで、データパックに依らないコンフィグの値をもとに地形生成などが可能
			// とはいえ、そういうことをしないのであればjsonで記述する方が楽である
			CONFIGURED_FEATURES_REGISTRY.register("example_feature_config", () -> new ConfiguredFeature<>(EXAMPLE_FEATUR.get(), config));

	/**
	 * PlacedFeature
	 * 地形生成の内容を保持するConfiguredFeatureに対して、こちらは地形生成を行う場所を指定する
	 */
	static final RegistryObject<PlacedFeature> EXAMPLE_FEATURE_PLACED =
			// forge.biome_modifierに置くjsonで"features": "st_example:example_feature_place"が機能するようになる
			PLACED_FEATURES_REGISTRY.register("example_feature_place", () -> new PlacedFeature(
					EXAMPLE_FEATURE_CONFIGURED.getHolder().get(),
					/*
					 * 1チャンクに1回、どの座標に地形生成するかが次のリストに基づいて判定される
					 * PlacementModifierを自作すればさらに複雑な処理も作成可能ではあるが
					 * もともと用意されているものが十分多いため、基本的には必要ないだろう
					 * jsonで記述するのが楽である
					 */
					List.of(
							// 1/20 の確率で生成
							RarityFilter.onAverageOnceEvery(20),
							// チャンク内のランダムなX,Z座標に生成
							InSquarePlacement.spread(),
							// ワールドの地形の表面（例えば海なら海底）に生成
							// ちなみにWORLD_SURFACE_WGだと水面の座標が取れる
							HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG)
							)
					));

	/**
	 * BiomeModifierに渡すHolderSet
	 * うまい取得方法が思いつかなかったので、やっぱりBiomeModifierもjsonで記述するのが良いと思う
	 */
	@SuppressWarnings("deprecation")
	static HolderSet<Biome> biomes = BuiltinRegistries.BIOME.getOrCreateTag(BiomeTags.IS_OVERWORLD);
	
	/**
	 * BiomeModifier
	 * チャンクを生成する時に、ここで指定されたバイオームならば
	 * PlacedFeatureの情報を元に地形生成を行う
	 * （あるいは、デフォルトで持っているPlacedFeatureを削除するといった使い方も可能。詳細はForgeBiomeModifiersを参照）
	 */
	static final RegistryObject<BiomeModifier> EXAMPLE_MODIFIER =
			BIOME_MODIFIERS_REGISTRY.register("example_feature_modifier", () -> 
			new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(EXAMPLE_FEATURE_PLACED.getHolder().get()), GenerationStep.Decoration.TOP_LAYER_MODIFICATION));
	
}
