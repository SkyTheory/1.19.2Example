package skytheory.example.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.block.DisplayStandBlock;
import skytheory.example.block.SimpleProcessorBlock;

/**
 * ワールドに存在するブロックを登録しているクラス
 * 硬さや回収にツールが必要かなどはBlockクラスのコンストラクタで設定するが
 * どの段階の（石、鉄、ダイヤモンド）ツールが必要か、また壊した時にどんなアイテムをドロップするか
 * どんな形のブロックで、どんなテクスチャを適用するか、などの設定はjsonで行う
 * 
 * そちらの詳細はdataパッケージ内のExampleBlockTagsProviderとdata.loot.ExampleBlockLootを参照
 * SetupEventにある通りにDataGeneratorを設定しておけば、必要なjsonファイルはrunDataを実行したときに自動で生成してくれる
 * 
 * また、ワールドに設置されていないアイテム状態のブロックは、別途Itemとして登録する必要がある
 * そちらはItemInitを参照すること
 * @author SkyTheory
 *
 */
public class BlockInit {
	
	public static final DeferredRegister<Block> REGISTRY =
			DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MODID);

	public static final RegistryObject<Block> MACGUFFIN_ORE =
			REGISTRY.register("macguffin_ore", () -> new Block(BlockBehaviour.Properties
					.of(Material.STONE)
					.requiresCorrectToolForDrops()
					.strength(3.0f, 3.0f)
					.sound(SoundType.STONE)));

	public static final RegistryObject<Block> DEEPSLATE_MACGUFFIN_ORE =
			REGISTRY.register("deepslate_macguffin_ore", () -> new Block(BlockBehaviour.Properties
					.copy(MACGUFFIN_ORE.get())
					.strength(4.5f, 3.0f)
					.sound(SoundType.DEEPSLATE)));

	public static final RegistryObject<Block> MACGUFFIN_BLOCK =
			REGISTRY.register("macguffin_block", () -> new Block(BlockBehaviour.Properties
					.of(Material.METAL, MaterialColor.METAL)
					.requiresCorrectToolForDrops()
					.strength(5.0F, 6.0F)
					.sound(SoundType.METAL)));

	public static final RegistryObject<Block> MUFFIN_BLOCK =
			REGISTRY.register("muffin_block", () -> new Block(BlockBehaviour.Properties
					.of(Material.CAKE)
					.sound(SoundType.WOOL)));

	public static final RegistryObject<Block> DISPLAY_STAND =
			REGISTRY.register("display_stand", () -> new DisplayStandBlock(BlockBehaviour.Properties
					.of(Material.STONE)
					.strength(1.0f, 3.0f)
					.sound(SoundType.STONE)));

	public static final RegistryObject<Block> SIMPLE_PROCESSOR =
			REGISTRY.register("simple_processor", () -> new SimpleProcessorBlock(BlockBehaviour.Properties
					.of(Material.STONE)
					.strength(1.0f, 3.0f)
					.sound(SoundType.STONE)));

}
