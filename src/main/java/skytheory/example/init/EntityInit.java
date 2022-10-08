package skytheory.example.init;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.entity.ExampleEntity;

public class EntityInit {

	/**
	 * Entity用のレジストリ
	 * これをEventBusに登録しておけば
	 * 登録したEntityのクラスが使えるようになる
	 */
	public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

	/**
	 * レジストリに登録するときは、第一引数に登録名、第二引数にEntityTypeのSupplierを指定
	 */
	public static final RegistryObject<EntityType<ExampleEntity>> EXAMPLE_ENTITY =
			ENTITIES_REGISTRY.register("example_entity", ExampleEntity::createEntityType);
	
}
