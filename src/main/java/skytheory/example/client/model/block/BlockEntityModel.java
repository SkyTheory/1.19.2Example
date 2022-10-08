package skytheory.example.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface BlockEntityModel<T extends BlockEntity> {
	
	public void setupAnim(T entity);
	public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay);
}
