package skytheory.example.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import skytheory.example.block.ExampleEntityBlock;
import skytheory.example.client.model.block.ExampleBlockEntityModel;
import skytheory.example.client.renderer.block.ExampleBlockEntityRenderer;

public class ExampleBEWLR extends BlockEntityWithoutLevelRenderer {

	public static final ExampleBEWLR INSTANCE = new ExampleBEWLR();
	public static final float COEFFICIENT = (float) (Math.PI / 180.0d);

	private final ModelPart model;

	private ExampleBEWLR() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
		this.model = Minecraft.getInstance().getEntityModels().bakeLayer(ExampleBlockEntityModel.LAYER_LOCATION);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemTransforms.TransformType type, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
		if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ExampleEntityBlock) {
			RenderType renderType = RenderType.entitySolid(ExampleBlockEntityRenderer.TEXTURE);
			VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
			poseStack.scale(-1.0f, -1.0f, 1.0f);
			poseStack.translate(-0.5f, -1.5f, 0.5f);
			this.model.render(poseStack, vertexConsumer, combinedLight, combinedOverlay);
		} else {
			super.renderByItem(stack, type, poseStack, bufferSource, combinedLight, combinedOverlay);
		}
	}
}
