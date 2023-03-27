package skytheory.example.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.block.entity.DisplayStandEntity;
import skytheory.example.block.entity.SimpleProcessorEntity;

/**
 * BlockEntityを登録するクラス
 * modでBlockEntityを作成するにはBlockEntityのクラスを作成するだけでなく、別途BlockEntityTypeというものを登録する必要がある
 * ここではそのBlockEntityTypeの登録を行っている
 * @author SkyTheory
 *
 */
public class BlockEntityInit {

	public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
	DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExampleMod.MODID);

	public static final RegistryObject<BlockEntityType<DisplayStandEntity>> DISPLAY_STAND =
	REGISTRY.register("display_stand",() ->  BlockEntityType.Builder.of(DisplayStandEntity::new, BlockInit.DISPLAY_STAND.get()).build(null));

	public static final RegistryObject<BlockEntityType<SimpleProcessorEntity>> SIMPLE_PROCESSOR =
	REGISTRY.register("simple_processor",() ->  BlockEntityType.Builder.of(SimpleProcessorEntity::new, BlockInit.SIMPLE_PROCESSOR.get()).build(null));

}
