package skytheory.example.data;

import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import skytheory.example.ExampleMod;
import skytheory.example.init.BlockInit;

public class ModBlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {

	protected final ExistingFileHelper existingFileHelper;
	protected final BlockModelProvider modelProvider;

	public ModBlockStateProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
		super(generator, modid, existingFileHelper);
		this.existingFileHelper = existingFileHelper;
		this.modelProvider = this.models();
	}

	@Override
	protected void registerStatesAndModels() {
		/*
		 * BlockStateとブロックをもとに、単純な立方体のブロックのモデル及びBlockStateを登録する
		 * BlockStateProviderで用意されている、Blockという名前で終わるメソッドはBlockStateの生成までやってくれる
		 * 一番使うのがこのsimpleBlockだろう、名前の通り石や丸石といった単純な立方体ブロックのかたちで登録する
		 */
		this.simpleBlock(BlockInit.MACGUFFIN_BLOCK.get());
		this.simpleBlock(BlockInit.SIMPLE_PROCESSOR.get());

		/*
		 * バニラのアセット及びBlockModelProviderに用意されたメソッドからモデルを作成する
		 * このメソッドで作成しているのは側面、天面、底面でテクスチャの異なる立方体のモデル
		 */
		this.cubeBottomTop(BlockInit.MUFFIN_BLOCK.get(),
				new ResourceLocation(ExampleMod.MODID, "block/muffin_side"),
				new ResourceLocation(ExampleMod.MODID, "block/muffin_bottom"),
				new ResourceLocation(ExampleMod.MODID, "block/muffin_top"));

		/*
		 * 手動でassets/st_example/models/blockに配置したモデルから、それを継承したモデルを作成する
		 */
		this.createChildModel(BlockInit.MACGUFFIN_ORE.get(), new ResourceLocation(ExampleMod.MODID, "block/layered_block"))
		.texture("base", new ResourceLocation("minecraft", "block/stone"))
		.texture("mineral", new ResourceLocation(ExampleMod.MODID, "block/macguffin_ore"))
		.renderType(new ResourceLocation("translucent"));

		this.createChildModel(BlockInit.DEEPSLATE_MACGUFFIN_ORE.get(), new ResourceLocation(ExampleMod.MODID, "block/layered_block"))
		.texture("base", new ResourceLocation("minecraft", "block/deepslate"))
		.texture("mineral", new ResourceLocation(ExampleMod.MODID, "block/macguffin_ore"))
		.renderType(new ResourceLocation("translucent"));

		/*
		 * 何も描画しないモデルを作成する
		 */
		this.blockEntity(BlockInit.DISPLAY_STAND.get(), new ResourceLocation("minecraft", "block/stone"));
	}

	/**
	 * バニラのアセット及びBlockModelProviderに用意されたメソッドからモデルを作成する
	 */
	public void cubeBottomTop(Block block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		String name = this.getRegistryKey(block).getPath();
		ConfiguredModel model = new ConfiguredModel(this.models().cubeBottomTop(name, side, bottom, top));
		this.getVariantBuilder(block).partialState().setModels(model);
	}

	/**
	 * 手動でassets/st_example/models/blockに配置したモデルから、それを継承したモデルを作成する
	 */
	public BlockModelBuilder createChildModel(Block block, ResourceLocation parent) {
		BlockModelBuilder model = this.modelProvider.withExistingParent(this.getRegistryKey(block).getPath(), parent);
		this.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(model));
		return model;
	}

	/**
	 * BlockEntityのために、何も描画しないモデルを作成する
	 */
	public void blockEntity(Block block, ResourceLocation particle) {
		ModelFile.UncheckedModelFile parent = new ModelFile.UncheckedModelFile("builtin/entity");
		BlockModelBuilder model = this.modelProvider.getBuilder(this.getRegistryKey(block).getPath())
				.parent(parent)
				.ao(false)
				.texture("particle", particle)
				.guiLight(GuiLight.SIDE)
				.transforms()
				.transform(TransformType.GUI)
				.rotation(30.0f, 225.0f, 0.0f)
				.translation(0.0f, 0.0f, 0.0f)
				.scale(0.625f, 0.625f, 0.625f)
				.end()
				.transform(TransformType.GROUND)
				.rotation(0.0f, 0.0f, 0.0f)
				.translation(0.0f, 0.0f, 0.0f)
				.scale(0.25f, 0.25f, 0.25f)
				.end()
				.transform(TransformType.FIXED)
				.rotation(0.0f, 0.0f, 0.0f)
				.translation(0.0f, 0.0f, 0.0f)
				.scale(0.5f, 0.5f, 0.5f)
				.end()
				.transform(TransformType.THIRD_PERSON_RIGHT_HAND)
				.rotation(75.0f, 45.0f, 0.0f)
				.translation(0.0f, 2.5f, 0.0f)
				.scale(0.375f, 0.375f, 0.375f)
				.end()
				.transform(TransformType.THIRD_PERSON_LEFT_HAND)
				.rotation(75.0f, 225.0f, 0.0f)
				.translation(0.0f, 2.5f, 0.0f)
				.scale(0.375f, 0.375f, 0.375f)
				.end()
				.transform(TransformType.FIRST_PERSON_RIGHT_HAND)
				.rotation(0.0f, 45.0f, 0.0f)
				.translation(0.0f, 2.5f, 0.0f)
				.scale(0.4f, 0.4f, 0.4f)
				.end()
				.transform(TransformType.FIRST_PERSON_LEFT_HAND)
				.rotation(0.0f, 45.0f, 0.0f)
				.translation(0.0f, 2.5f, 0.0f)
				.scale(0.4f, 0.4f, 0.4f)
				.end()
				.end();
		ConfiguredModel configured = new ConfiguredModel(model);
		this.getVariantBuilder(block).partialState().setModels(configured);
	}

	public ResourceLocation getRegistryKey(Block block) {
		return ForgeRegistries.BLOCKS.getKey(block);
	}
}