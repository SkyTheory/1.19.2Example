package skytheory.example.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.block.ExampleEntityBlock;
import skytheory.example.block.entity.ExampleBlockEntity;

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

	// Block
	
	public static final DeferredRegister<Block> BLOCKS_REGISTRY =
			DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MODID);

	public static final RegistryObject<Block> MACGUFFIN_ORE =
			BLOCKS_REGISTRY.register("macguffin_ore", () -> new Block(BlockBehaviour.Properties
					.of(Material.STONE)
					.requiresCorrectToolForDrops()
					.strength(3.0f, 3.0f)
					.sound(SoundType.STONE)));

	public static final RegistryObject<Block> DEEPSLATE_MACGUFFIN_ORE =
			BLOCKS_REGISTRY.register("deepslate_macguffin_ore", () -> new Block(BlockBehaviour.Properties
					.copy(MACGUFFIN_ORE.get())
					.strength(4.5f, 3.0f)
					.sound(SoundType.DEEPSLATE)));

	public static final RegistryObject<Block> MACGUFFIN_BLOCK =
			BLOCKS_REGISTRY.register("macguffin_block", () -> new Block(BlockBehaviour.Properties
					.of(Material.METAL, MaterialColor.METAL)
					.requiresCorrectToolForDrops()
					.strength(5.0F, 6.0F)
					.sound(SoundType.METAL)));

	public static final RegistryObject<Block> MUFFIN_BLOCK =
			BLOCKS_REGISTRY.register("muffin_block", () -> new Block(BlockBehaviour.Properties
					.of(Material.CAKE)
					.sound(SoundType.WOOL)));
	
	public static final RegistryObject<Block> EXAMPLE_ENTITY_BLOCK =
			BLOCKS_REGISTRY.register("example_entity_block", () -> new ExampleEntityBlock(BlockBehaviour.Properties
					.of(Material.STONE)
					.strength(1.0f, 3.0f)
					.sound(SoundType.STONE)
					.noOcclusion()));

	// Block Entity

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES_REGISTRY =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExampleMod.MODID);

	public static final RegistryObject<BlockEntityType<ExampleBlockEntity>> EXAMPLE_BLOCK_ENTITY_TYPE =
			BLOCK_ENTITIES_REGISTRY.register("example_entity_block",() ->  BlockEntityType.Builder.of(ExampleBlockEntity::new, EXAMPLE_ENTITY_BLOCK.get()).build(null));
}
