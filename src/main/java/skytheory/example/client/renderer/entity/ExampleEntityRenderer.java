package skytheory.example.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import skytheory.example.ExampleMod;
import skytheory.example.client.model.entity.ExampleEntityModel;
import skytheory.example.entity.ExampleEntity;

/**
 * ExampleEntity用のレンダラー
 * @author SkyTheory
 *
 */
public class ExampleEntityRenderer extends MobRenderer<ExampleEntity, ExampleEntityModel> {

	/** テクスチャファイルのある場所を指定 */
	public static ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/example_entity.png");
	/** 影の大きさを指定 */
	public static final float SHADOW = 0.5f;

	public ExampleEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new ExampleEntityModel(ctx.bakeLayer(ExampleEntityModel.LAYER_LOCATION)), SHADOW);
	}

	/**
	 * Entityの大きさに応じて、描画するモデルの大きさも変更できるように
	 * そういったことをしないのであれば、スーパークラスの実装そのままで大丈夫
	 */
	@Override
	public void render(ExampleEntity entity, float f, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		poseStack.pushPose();
		this.resize(entity, poseStack);
		super.render(entity, f, partialTick, poseStack, multiBufferSource, packedLight);
		poseStack.popPose();
	}
	
	public void resize(ExampleEntity entity, PoseStack poseStack) {
		float size = entity.getScale();
		this.shadowRadius = SHADOW * size;
		if (size != 1.0f) {
			poseStack.scale(size, size, size);
		}
	}
	
	// モデルに貼り付けるテクスチャの位置を指定する
	// ここを書き換えれば、Entityの状態に応じて違うテクスチャにするなどといったことも可能
	@Override
	public ResourceLocation getTextureLocation(ExampleEntity entity) {
		return TEXTURE;
	}

}
