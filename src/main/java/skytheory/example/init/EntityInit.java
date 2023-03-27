package skytheory.example.init;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.entity.ExampleEntity;

/**
 * Entityを登録するクラス
 * modでEntityを作成するにはEntityのクラスを作成するだけでなく、別途EntityTypeというものを登録する必要がある
 * ここではそのEntityTypeの登録を行っている
 * @author SkyTheory
 *
 */
public class EntityInit {

	/**
	 * Entity用のレジストリ
	 * これをEventBusに登録しておけば
	 * 登録したEntityのクラスが使えるようになる
	 */
	public static final DeferredRegister<EntityType<?>> REGISTRY =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

	/**
	 * レジストリに登録するときは、第一引数に登録名、第二引数にEntityTypeのSupplierを指定
	 */
	public static final RegistryObject<EntityType<ExampleEntity>> EXAMPLE_ENTITY =
			REGISTRY.register("example_entity", ExampleEntity::createEntityType);
	
}
