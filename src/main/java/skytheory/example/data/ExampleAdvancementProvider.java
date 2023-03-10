package skytheory.example.data;

import java.util.function.Consumer;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import skytheory.example.advancement.ExampleAdvanement;

public class ExampleAdvancementProvider extends AdvancementProvider {

	public ExampleAdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
		super(generatorIn, fileHelperIn);
	}

	/**
	 * ここを上書きしておいて、自作の進捗を出力するようにする
	 */
	@Override
	protected void registerAdvancements(Consumer<Advancement> consumer, net.minecraftforge.common.data.ExistingFileHelper fileHelper) {
		new ExampleAdvanement().accept(consumer);
	}

}
