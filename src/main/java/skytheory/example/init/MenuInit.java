package skytheory.example.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import skytheory.example.ExampleMod;
import skytheory.example.inventory.ExampleContainerMenu;

/**
 * ゲーム中に表示させるGUIを登録しているクラス
 * Menu（クライアント・サーバー両側に存在し、中身のアイテムの情報を保持する）と
 * MenuScreen（クライアント側にのみ存在し、GUIの描画やボタンの処理などを行う）に分かれており
 * それを登録するためのMenuTypeが存在する
 * @author SkyTheory
 *
 */
public class MenuInit {

	public static final DeferredRegister<MenuType<?>> MENU_TYPES_REGISTRY =
			DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExampleMod.MODID);
	
	public static final RegistryObject<MenuType<?>> MENU_TYPE =
			MENU_TYPES_REGISTRY.register("example_block_menu", () -> ExampleContainerMenu.TYPE);
}
