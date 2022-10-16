package skytheory.example;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import skytheory.example.event.BlockEvent;
import skytheory.example.event.EntityEvent;
import skytheory.example.init.BlockInit;
import skytheory.example.init.EntityInit;
import skytheory.example.init.ItemInit;
import skytheory.example.init.LevelGenInit;
import skytheory.example.init.MenuInit;
import skytheory.example.init.SetupEvent;

/**
 * Minecraft 1.19.2環境でMinecraftForgeによるModdingを行うためのサンプルMod
 * とりあえず、何かを作ってみたい！　と思った時は
 * 
 * init内のあれこれをまず見ると良いかも
 * e.g.) Blockを作ってみたい -> BlockInitを見る
 * @author SkyTheory
 *
 */
@Mod(ExampleMod.MODID)
public class ExampleMod
{
	// Define mod id in a common place for everything to reference
	public static final String MODID = "st_example";
	// Directly reference a slf4j logger
	public static final Logger LOGGER = LogUtils.getLogger();
	
	public ExampleMod() {
		// EventやDeferredRegisterなどを登録するEventBusを取得する
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		/*
		 *  DeferredRegisterを登録しておく
		 *  ここに登録されたレジストリから、ゲーム内に要素を追加していく
		 */
		BlockInit.BLOCKS_REGISTRY.register(modEventBus);
		BlockInit.BLOCK_ENTITIES_REGISTRY.register(modEventBus);
		EntityInit.ENTITIES_REGISTRY.register(modEventBus);
		ItemInit.ITEMS_REGISTRY.register(modEventBus);
		LevelGenInit.FEATURES_REGISTRY.register(modEventBus);
		LevelGenInit.CONFIGURED_FEATURES_REGISTRY.register(modEventBus);
		LevelGenInit.PLACED_FEATURES_REGISTRY.register(modEventBus);
		LevelGenInit.BIOME_MODIFIERS_REGISTRY.register(modEventBus);
		MenuInit.MENU_TYPES_REGISTRY.register(modEventBus);
		/*
		 * EventBusにクラスを登録する
		 * modの初期化時に、ここに登録されたメソッドが呼ばれるようになる
		 * 条件は以下の通り
		 * 
		 * SubscribeEventのアノテーションが付いている
		 * public staticである
		 * 対応するEventひとつが引数
		 */
		modEventBus.register(SetupEvent.class);
		modEventBus.register(SetupEvent.BlockEntity.class);
		modEventBus.register(SetupEvent.Entity.class);

		/*
		 * EventBusにクラスを登録する
		 * 初期化時以外に発生するイベントで、ここに登録されたメソッドが呼ばれるようになる
		 * 要するに、IModBusEventならあっち、そうでないならこっち！
		 * その他の使い方は上記と一緒
		 */
		MinecraftForge.EVENT_BUS.register(BlockEvent.class);
		MinecraftForge.EVENT_BUS.register(EntityEvent.class);

	}

}
