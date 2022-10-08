package skytheory.example.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import skytheory.example.ExampleMod;
import skytheory.example.init.ItemInit;

public class ExampleCreativeModeTab extends CreativeModeTab {

	public static final CreativeModeTab INSTANCE = new ExampleCreativeModeTab();
	
	private ExampleCreativeModeTab() {
		super(ExampleMod.MODID);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ItemInit.MACGUFFIN.get());
	}

}
