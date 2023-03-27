package skytheory.example.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import skytheory.example.ExampleMod;
import skytheory.example.block.entity.DisplayStandEntity;
import skytheory.example.client.model.block.ExampleBlockEntityModel;

public class ExampleBlockEntityRenderer implements BlockEntityRenderer<DisplayStandEntity> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/example_block.png");

	protected final Model model;
	protected final ItemRenderer itemRenderer;

	public ExampleBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.model = new ExampleBlockEntityModel(ctx.bakeLayer(ExampleBlockEntityModel.LAYER_LOCATION));
		this.itemRenderer = ctx.getItemRenderer();
	}

	@Override
	public void render(DisplayStandEntity block, float f, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
		RenderType renderType = RenderType.entitySolid(TEXTURE);
		VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
		poseStack.translate(0.5f, 0.5f, 0.5f);
		poseStack.pushPose();
		poseStack.translate(0.0f, 1.0f, 0.0f);
		poseStack.scale(-1.0f, -1.0f, 1.0f);
		this.model.renderToBuffer(poseStack, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
		poseStack.popPose();
		renderItem(block, f, poseStack, bufferSource, light, overlay);
	}

	public void renderItem(DisplayStandEntity block, float f, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
		if (block != null) {
			IItemHandler handler = block.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
			if (handler != null) {
				ItemStack stack = handler.getStackInSlot(0);
				if (!stack.isEmpty()) {
					// クライアントのプレイヤーからの角度を算出する
					BlockPos pos = block.getBlockPos();
					@SuppressWarnings("resource")
					LocalPlayer player = Minecraft.getInstance().player;
					Vec3 vec3 = player.position();
					double x1 = pos.getX() + 0.5d;
					double z1 = pos.getZ() + 0.5d;
					double x2 = vec3.x;
					double z2 = vec3.z;
					// プレイヤーの方へアイテムを向けて描画する
					poseStack.mulPose(Vector3f.YP.rotation((float) (Math.atan2(x1 - x2, z1 - z2) + Math.PI)));
					this.itemRenderer.renderStatic(stack, TransformType.GROUND, light, overlay, poseStack, bufferSource, 0);
				}

			}
		}
	}
	
}