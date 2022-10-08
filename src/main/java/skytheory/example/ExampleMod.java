package skytheory.example;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.event.EntityEvent;
import skytheory.example.event.SetupEvent;
import skytheory.example.init.BlockInit;
import skytheory.example.init.EntityInit;
import skytheory.example.init.ItemInit;
import skytheory.example.init.LevelGenInit;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
	// Define mod id in a common place for everything to reference
	public static final String MODID = "st_example";
	// Directly reference a slf4j logger
	public static final Logger LOGGER = LogUtils.getLogger();
	// Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	// Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

	// Creates a new Block with the id "examplemod:example_block", combining the namespace and path
	public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
	// Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
	public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

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
		MinecraftForge.EVENT_BUS.register(EntityEvent.class);

	}

}
