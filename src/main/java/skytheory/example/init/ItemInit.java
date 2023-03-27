package skytheory.example.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.item.DisplayStandBlockItem;
import skytheory.example.item.ExampleCreativeModeTab;
import skytheory.example.item.ExampleItem;

/**
 * ワールドに存在するアイテムを登録しているクラス
 * アイコンなどの設定はjsonで行う
 * 
 * そちらの詳細はdataパッケージ内のExampleItemModelProviderを参照
 * SetupEventにある通りにDataGeneratorを設定しておけば、必要なjsonファイルはrunDataを実行したときに自動で生成してくれる
 * @author SkyTheory
 *
 */
public class ItemInit {

	public static final DeferredRegister<Item> REGISTRY =
			DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);
	
	public static final CreativeModeTab EXAMPLE_TAB = ExampleCreativeModeTab.INSTANCE;

	// 無機能アイテムなら、Propertiesはクリエイティブタブ以外は未設定でも問題ない
	// なお1.19.3以降はCreativeModeTabの登録方法が変化しているので注意
	// 当該バージョンならtabの設定も（ここでは）不要になった
	public static final RegistryObject<Item> MACGUFFIN =
			REGISTRY.register("macguffin", () -> new ExampleItem(new Item.Properties().tab(EXAMPLE_TAB)));

	// 食べ物アイテムも、propertyの設定のみで作成可能
	public static final RegistryObject<Item> MUFFIN =
			REGISTRY.register("muffin", () -> new ExampleItem(new Item.Properties()
					// 最大スタックサイズ
					.stacksTo(16)
					// 食べ物として登録
					.food(new FoodProperties.Builder()
							// 満腹度にかかわらず食べることが可能で
							.alwaysEat()
							// 満腹度が4
							.nutrition(4)
							// 隠し満腹度が4 * 0.6 = 2.4
							.saturationMod(0.6f)
							// 食べると1.0fの確率で（つまり確定で）600tickのSpeed効果を得る
							.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0), 1.0f)
							.build()
							)
					// クリエイティブタブ
					.tab(EXAMPLE_TAB)
					));
	
	// 追加Entityのスポーンエッグ
	// バニラのSpawnEggItemではなく、ForgeSpawnEggItemを利用すれば登録処理が多少楽
	public static final RegistryObject<Item> EXAMPLE_SPAWN_EGG =
			REGISTRY.register("example_spawn_egg", () -> new ForgeSpawnEggItem(EntityInit.EXAMPLE_ENTITY, 0xEEEEEE, 0x00CCCC, new Item.Properties().tab(EXAMPLE_TAB)));

	/*
	 *  BlockInitで作成したブロックは、こちらでアイテムとしても登録しておくこと
	 *  そうすることで、アイテムとしてブロックが存在できるようになる
	 *  液体などのアイテムとして存在する必要のないブロックの場合は必要ない
	 */
	public static final RegistryObject<Item> MACGUFFIN_ORE =
			REGISTRY.register("macguffin_ore", () -> new BlockItem(BlockInit.MACGUFFIN_ORE.get(), new Item.Properties().tab(EXAMPLE_TAB)));
	public static final RegistryObject<Item> DEEPSLATE_MACGUFFIN_ORE =
			REGISTRY.register("deepslate_macguffin_ore", () -> new BlockItem(BlockInit.DEEPSLATE_MACGUFFIN_ORE.get(), new Item.Properties().tab(EXAMPLE_TAB)));
	public static final RegistryObject<Item> MACGUFFIN_BLOCK =
			REGISTRY.register("macguffin_block", () -> new BlockItem(BlockInit.MACGUFFIN_BLOCK.get(), new Item.Properties().tab(EXAMPLE_TAB)));
	public static final RegistryObject<Item> MUFFIN_BLOCK =
			REGISTRY.register("muffin_block", () -> new BlockItem(BlockInit.MUFFIN_BLOCK.get(), new Item.Properties().tab(EXAMPLE_TAB)));
	public static final RegistryObject<Item> DISPLAY_STAND =
			REGISTRY.register("display_stand", () -> new DisplayStandBlockItem(BlockInit.DISPLAY_STAND.get(), new Item.Properties().tab(EXAMPLE_TAB)));
	public static final RegistryObject<Item> SIMPLE_PROCESSOR =
			REGISTRY.register("simple_processor", () -> new BlockItem(BlockInit.SIMPLE_PROCESSOR.get(), new Item.Properties().tab(EXAMPLE_TAB)));
}
