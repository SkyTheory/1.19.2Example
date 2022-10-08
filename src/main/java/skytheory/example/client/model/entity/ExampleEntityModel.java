package skytheory.example.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import skytheory.example.ExampleMod;
import skytheory.example.entity.ExampleEntity;

// Made with Blockbench 4.4.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports

/*
 * EntityModelの作成は、BlockBenchというツールが便利なのでお勧め
 * 出力したjavaファイルを元に色々設定していく
 */
public class ExampleEntityModel extends EntityModel<ExampleEntity> {
	

	/** pi/180の近似値 */
	private final static float TORADIAN = 0.017453292f;
	
	/* 手足の振りの角度 */
	public static final float ARM_SWING_AMOUNT = 60.0f * TORADIAN;
	public static final float LEG_SWING_AMOUNT = 65.0f * TORADIAN;
	
	/* 手足の振りの速さ */
	public static final float LIMB_SWING_WEIGHT = 0.85f;

	// モデルの登録のために、他と被らない名前でResourceLocationを登録しておく
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ExampleMod.MODID, "example_entity"), "main");
	
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart armLeft;
	private final ModelPart armRight;
	private final ModelPart legLeft;
	private final ModelPart legRight;

	// まとめて描画するためにrootを取得
	// そのほか、アニメーションしたい部位に応じてModelPartを取得しておく
	public ExampleEntityModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.armLeft = root.getChild("armLeft");
		this.armRight = root.getChild("armRight");
		this.legLeft = root.getChild("legLeft");
		this.legRight = root.getChild("legRight");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(24, 16).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		partdefinition.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(40, 0).addBox(2.8F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 9.5F, 0.0F));

		partdefinition.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(32, 0).addBox(-4.8F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 9.5F, 0.0F));

		partdefinition.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(12, 16).addBox(-1.0F, -1.1F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(-0.1F))
		.texOffs(14, 26).addBox(-1.0F, 5.0F, -2.5F, 3.0F, 2.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(1.0F, 17.0F, 0.0F));

		partdefinition.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -1.1F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(-0.1F))
		.texOffs(0, 26).addBox(-2.0F, 5.0F, -2.5F, 3.0F, 2.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(-1.0F, 17.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	/**
	 * モデルの動きをここで設定する
	 * Entityを引数に持っているので、状態に応じて動きを変えることもできる
	 * limbSwingやlimbSwingAmountは、Entityが移動している際に、その速度などに応じて変わる値
	 * ここでは単純に頭の角度と移動時の手足の振りのみを実装
	 */
	@Override
	public void setupAnim(ExampleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// netHeadYawとheadPitchをもとに、頭の角度を設定
		this.animHead(netHeadYaw, headPitch);
		// コサイン関数で現在の手足があるべき位置を取得
		// 要するに、0から最大値までを行ったり来たりする数値を作っている
		// LIMB_SWING_WEIGHTを変えると、その行ったり来たりするまでの速さが変わり、結果的に手足の振りが早く、あるいは遅くなる
		// entity.getScale()も係数に持っているので、entityのサイズに応じても振りの速さが変わるが
		// これはExampleEntityの実装と合わせて、移動距離に手足の振りの量を合わせる形になっている
		Vector3f vec3f =  new Vector3f(Mth.cos(limbSwing * 0.662f * LIMB_SWING_WEIGHT / entity.getScale()) * limbSwingAmount, 0.0f, 0.0f);
		this.swingArms(vec3f);
		this.swingLegs(vec3f);
	}

	public void animHead(float netHeadYaw, float headPitch) {
		Vector3f vec3f =  new Vector3f(headPitch * TORADIAN, netHeadYaw * TORADIAN, 0.0f);
		head.resetPose();
		head.offsetRotation(vec3f);
		// このモデルのように、headに初期角度を設定していなければ
		head.setRotation(headPitch * TORADIAN, netHeadYaw * TORADIAN, 0.0f);
	}

	public void swingArms(Vector3f vec3f) {
		Vector3f base = vec3f.copy();
		base.mul(ARM_SWING_AMOUNT);
		Vector3f mirror = base.copy();
		mirror.mul(-1.0f);
		armLeft.resetPose();
		armRight.resetPose();
		armLeft.offsetRotation(base);
		armRight.offsetRotation(mirror);
	}

	public void swingLegs(Vector3f vec3f) {
		Vector3f base = vec3f.copy();
		base.mul(LEG_SWING_AMOUNT);
		Vector3f mirror = base.copy();
		mirror.mul(-1.0f);
		legRight.resetPose();
		legLeft.resetPose();
		legRight.offsetRotation(base);
		legLeft.offsetRotation(mirror);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}