package skytheory.example.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import skytheory.example.ExampleMod;
import skytheory.example.block.entity.ExampleBlockEntity;

// Made with Blockbench 4.4.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ExampleBlockEntityModel implements BlockEntityModel<ExampleBlockEntity> {
	
	// モデルの登録のために、他と被らない名前でResourceLocationを登録しておく
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ExampleMod.MODID, "display_stand"), "main");
	
	private final ModelPart model;
	
	public ExampleBlockEntityModel(ModelPart model) {
		this.model = model;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("model", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-8.0F, -4.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 20).addBox(-7.0F, -5.0F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void setupAnim(ExampleBlockEntity entity) {
		// 何かモデルに動きを付ける場合は、ここの引数のentityから行う
	}
	
	public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		model.render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	}

}