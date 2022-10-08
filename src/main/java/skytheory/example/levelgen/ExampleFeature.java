package skytheory.example.levelgen;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * Minecraftの地形生成時に、既存のfeatureにないものを追加したい時に作るクラス
 * 実際の地形生成処理の具体的な流れとしては
 * BiomeModifierで、どのバイオームに対して地形生成を行うかを決める
 * PlacedFeatureで、チャンク毎にどの座標に地形生成を行うかを決める
 * ConfiguredFeatureで、生成するブロックなどを指定する（このクラスではExampleFeatureConfigurationで指定する部分）
 * Feature（このクラス）で、座標とコンフィグを受け取り実際に地形生成を行う
 * というもの。
 * 登録例は{@link skytheory.example.init.LevelGenInit}を参照
 */
public class ExampleFeature extends Feature<ExampleFeatureConfiguration> {

	public ExampleFeature(Codec<ExampleFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<ExampleFeatureConfiguration> ctx) {
		
		// コンテキストに含まれるFeatureConfigurationから、設定された値を受け取る
		ExampleFeatureConfiguration cfg = ctx.config();
		BlockState state = cfg.state();
		int height = cfg.height();
		
		// PlacedFeatureで指定されたBlockPos
		// こちら側で座標を取り直すこともできるが、PlacedFeatureが多機能なため
		// ワールド内に鉱石を再生成するといったような特殊なことをしないのならあちらで指定するのが無難
		BlockPos pos = ctx.origin();
		WorldGenLevel level = ctx.level();
		
		// 実際の生成処理、ExampleFeatureConfigurationで指定された高さの、指定されたブロックの柱を生成する
		int count = 0;
		while (count < height) {
			if (pos.getY() <= level.getMaxBuildHeight()) {
				this.setBlock(level, pos, state);
				pos = pos.above();
				count++;
			} else {
				break;
			}
		}
		
		// ブロックの配置に1個でも成功しているならtrueを返す
		return count > 0;
	}

}
