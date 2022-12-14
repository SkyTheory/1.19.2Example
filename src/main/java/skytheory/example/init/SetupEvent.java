package skytheory.example.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import skytheory.example.ExampleMod;
import skytheory.example.client.gui.ExampleContainerScreen;
import skytheory.example.client.model.block.ExampleBlockEntityModel;
import skytheory.example.client.model.entity.ExampleEntityModel;
import skytheory.example.client.renderer.block.ExampleBlockEntityRenderer;
import skytheory.example.client.renderer.entity.ExampleEntityRenderer;
import skytheory.example.data.ExampleBlockStateProvider;
import skytheory.example.data.ExampleBlockTagsProvider;
import skytheory.example.data.ExampleItemModelProvider;
import skytheory.example.data.ExampleItemTagsProvider;
import skytheory.example.data.ExampleLaungageProviderEN;
import skytheory.example.data.ExampleLaungageProviderJP;
import skytheory.example.data.ExampleLootTableProvider;
import skytheory.example.data.ExampleRecipeProvider;
import skytheory.example.entity.ExampleEntity;
import skytheory.example.inventory.ExampleContainerMenu;
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
		MenuScreens.register(ExampleContainerMenu.TYPE, ExampleContainerScreen::new);
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
		BlockTagsProvider blockTagsProvider = new ExampleBlockTagsProvider(generator, ExampleMod.MODID, event.getExistingFileHelper());
		generator.addProvider(true, new ExampleBlockStateProvider(generator, ExampleMod.MODID, event.getExistingFileHelper()));
		generator.addProvider(true, blockTagsProvider);
		generator.addProvider(true, new ExampleItemTagsProvider(generator, blockTagsProvider, ExampleMod.MODID, event.getExistingFileHelper()));
		generator.addProvider(true, new ExampleItemModelProvider(generator, ExampleMod.MODID, event.getExistingFileHelper()));
		generator.addProvider(true, new ExampleLaungageProviderEN(generator, ExampleMod.MODID));
		generator.addProvider(true, new ExampleLaungageProviderJP(generator, ExampleMod.MODID));
		generator.addProvider(true, new ExampleLootTableProvider(generator));
		generator.addProvider(true, new ExampleRecipeProvider(generator));
	}
	
	public static class BlockEntity {

		/**
		 * BlockEntityの描画設定用のイベントそのいち
		 * BlockEntityのモデルを作成して、レンダラーで呼び出せるようにする
		 */
		@SubscribeEvent
		public static void registerLayerDefonitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
			event.registerLayerDefinition(ExampleBlockEntityModel.LAYER_LOCATION, ExampleBlockEntityModel::createBodyLayer);
		}

		/**
		 * BlockEntityの描画設定用のイベントそのに
		 * レンダラーを登録して、モデルが作成されていればそれを描画できるようにする
		 */
		@SubscribeEvent
		public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
			event.registerBlockEntityRenderer(BlockInit.EXAMPLE_BLOCK_ENTITY_TYPE.get(), ExampleBlockEntityRenderer::new);
		}

	}
	
	/*
	 * 以下はEntityを登録する際のイベント
	 */
	public static class Entity {

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
		public static void registerLayerDefonitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
			event.registerLayerDefinition(ExampleEntityModel.LAYER_LOCATION, ExampleEntityModel::createBodyLayer);
		}
		
		/**
		 * Entityの描画設定用のイベントそのに
		 * レンダラーを登録して、モデルが作成されていればそれを描画できるようにする
		 */
		@SubscribeEvent
		public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
			event.registerEntityRenderer(EntityInit.EXAMPLE_ENTITY.get(), ExampleEntityRenderer::new);
		}

	}
	
}
