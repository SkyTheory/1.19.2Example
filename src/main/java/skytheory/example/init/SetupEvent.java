package skytheory.example.init;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import skytheory.example.ExampleMod;
import skytheory.example.advancement.FallingDamageCriterionTrigger;
import skytheory.example.advancement.ItemThrownCriterionTrigger;
import skytheory.example.client.gui.DisplayStandScreen;
import skytheory.example.client.gui.SimpleProcessorScreen;
import skytheory.example.client.model.block.ExampleBlockEntityModel;
import skytheory.example.client.model.entity.ExampleEntityModel;
import skytheory.example.client.renderer.block.ExampleBlockEntityRenderer;
import skytheory.example.client.renderer.entity.ExampleEntityRenderer;
import skytheory.example.data.LaungageProviderEN;
import skytheory.example.data.LaungageProviderJP;
import skytheory.example.data.ModAdvancementProvider;
import skytheory.example.data.ModBlockStateProvider;
import skytheory.example.data.ModBlockTagsProvider;
import skytheory.example.data.ModItemModelProvider;
import skytheory.example.data.ModItemTagsProvider;
import skytheory.example.data.ModLootTableProvider;
import skytheory.example.data.SimpleProcessorRecipeProvider;
import skytheory.example.data.VannilaDeviceRecipeProvider;
import skytheory.example.entity.ExampleEntity;
import skytheory.example.network.PacketHandler;

/**
 * Modの基本情報を構築するイベントをまとめたクラス
 * 初期化時にここのイベントが色々呼ばれて、様々な登録処理を行う
 * @author SkyTheory
 *
 */
public class SetupEvent {

	/**
	 * Modのデータ構築時に呼ばれる
	 * 基本的にはcommonSetupで大丈夫だと思う
	 * @param event
	 */
	@SubscribeEvent
	public static void modConstruct(FMLConstructModEvent event) {
		ExampleMod.LOGGER.info("Mod Construct Event");
	}

	/**
	 * サーバー・クライアントのどちらの時にも呼ばれるイベント
	 * 読み込み時に色々設定したい時は、とりあえずここ
	 * @param event
	 */
	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		ExampleMod.LOGGER.info("Common Setup Event");
		PacketHandler.setup();
		// 進捗のためのトリガーを登録する
		CriteriaTriggers.register(new FallingDamageCriterionTrigger());
		CriteriaTriggers.register(new ItemThrownCriterionTrigger());
	}

	/**
	 * クライアントのセットアップ時に呼ばれる
	 * OnlyIn(Dist.CLIENT)が付いているクラスやメソッドを使いたい時に活用する
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ExampleMod.LOGGER.info("Client Setup Event");
		// MenuScreensはクライアント側にのみ存在するのでこちらで登録
		MenuScreens.register(MenuInit.DISPLAY_STAND.get(), DisplayStandScreen::new);
		MenuScreens.register(MenuInit.SIMPLE_PROCESSOR.get(), SimpleProcessorScreen::new);
	}

	/**
	 * runDataを実行した際に呼ばれる
	 * generatorに色々登録することで、jsonファイルの自動生成をしてくれる
	 * 基本的に開発環境用。使い方さえ分かればかなり便利だが、やや難解
	 * 具体的な使用例はdataパッケージ内を参照すること
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		generator.addProvider(true, new LaungageProviderEN(generator, ExampleMod.MODID));
		generator.addProvider(true, new LaungageProviderJP(generator, ExampleMod.MODID));
		generator.addProvider(true, new ModAdvancementProvider(generator, existingFileHelper));
		generator.addProvider(true, new ModBlockStateProvider(generator, ExampleMod.MODID, existingFileHelper));
		BlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(generator, ExampleMod.MODID, existingFileHelper);
		generator.addProvider(true, blockTagsProvider);
		generator.addProvider(true, new ModItemModelProvider(generator, ExampleMod.MODID, existingFileHelper));
		generator.addProvider(true, new ModItemTagsProvider(generator, blockTagsProvider, ExampleMod.MODID, existingFileHelper));
		generator.addProvider(true, new ModLootTableProvider(generator));
		generator.addProvider(true, new SimpleProcessorRecipeProvider(generator));
		generator.addProvider(true, new VannilaDeviceRecipeProvider(generator));
	}

	/**
	 * BlockEntityの描画設定用のイベントそのいち
	 * BlockEntityのモデルを作成して、レンダラーで呼び出せるようにする
	 */
	@SubscribeEvent
	public static void registerBlockEntityLayerDefonitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ExampleBlockEntityModel.LAYER_LOCATION, ExampleBlockEntityModel::createBodyLayer);
	}

	/**
	 * BlockEntityの描画設定用のイベントそのに
	 * レンダラーを登録して、モデルが作成されていればそれを描画できるようにする
	 */
	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(BlockEntityInit.DISPLAY_STAND.get(), ExampleBlockEntityRenderer::new);
	}

	/**
	 * Entityの基本情報の設定用のイベント
	 * AttributeSupplierをもとに、Entityのワールド内での基本情報を設定する
	 */
	@SubscribeEvent
	public static void entityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(EntityInit.EXAMPLE_ENTITY.get(), ExampleEntity.createAttributeSupplier());
	}

	/**
	 * Entityの描画設定用のイベントそのいち
	 * Entityのモデルを作成して、レンダラーで呼び出せるようにする
	 */
	@SubscribeEvent
	public static void registerEntityLayerDefonitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ExampleEntityModel.LAYER_LOCATION, ExampleEntityModel::createBodyLayer);
	}

	/**
	 * Entityの描画設定用のイベントそのに
	 * レンダラーを登録して、モデルが作成されていればそれを描画できるようにする
	 */
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(EntityInit.EXAMPLE_ENTITY.get(), ExampleEntityRenderer::new);
	}


}
