package skytheory.example.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import skytheory.example.ExampleMod;
import skytheory.example.block.entity.ExampleBlockEntity;
import skytheory.example.client.model.block.ExampleBlockEntityModel;

public class ExampleBlockEntityRenderer implements BlockEntityRenderer<ExampleBlockEntity> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/example_block.png");
	
	protected final ExampleBlockEntityModel model;
	
	public ExampleBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.model = new ExampleBlockEntityModel(ctx.bakeLayer(ExampleBlockEntityModel.LAYER_LOCATION));
	}

	@Override
	public void render(ExampleBlockEntity block, float f, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
		RenderType renderType = RenderType.entitySolid(TEXTURE);
		VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
		poseStack.scale(-1.0f, -1.0f, 1.0f);
		poseStack.translate(-0.5f, -1.5f, 0.5f);
		this.model.renderToBuffer(poseStack, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
	}

}

