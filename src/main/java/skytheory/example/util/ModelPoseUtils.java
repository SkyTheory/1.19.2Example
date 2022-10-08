package skytheory.example.util;

import com.mojang.math.Vector3f;

import net.minecraft.client.model.geom.ModelPart;

public class ModelPoseUtils {

	public static void mirrorMotion(ModelPart model1, ModelPart model2, Vector3f vec3f) {
		mirrorMotion(model1, model2, vec3f, 1.0f);
	}

	public static void mirrorMotion(ModelPart model1, ModelPart model2, Vector3f vec3f, float multiplier) {
		Vector3f base = vec3f.copy();
		base.mul(multiplier);
		Vector3f mirror = base.copy();
		mirror.mul(-1.0f);
		model1.resetPose();
		model2.resetPose();
		model1.offsetRotation(base);
		model2.offsetRotation(mirror);
	}
}
