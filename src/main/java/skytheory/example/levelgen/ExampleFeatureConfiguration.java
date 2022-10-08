package skytheory.example.levelgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * ExampleFeatureで地形生成をする際に、jsonファイルで設定されたBlockStateとintの値を渡すためのインターフェース
 * recordは単純な値のGetterを持った、拡張不可能なクラスのようなもの
 * 
 * 要するに、以下のクラスと似たように扱える
 * 
 * public class ExampleFeatureConfiguration implements FeatureConfiguration {
 * 	
 * 		public BlockState state;
 * 		public int height;
 * 
 * 		public ExampleFeatureConfiguration(BlockState state, int height) {
 * 			this.state = state;
 * 			this.height = height;
 * 		}
 * 
 * 		public BlockState state() {return state;}
 * 		public int height() {return height;}
 * }
 * 
 * 
 * あとはForgeのDocumentationを参照し、それに対応したコーデックを定義するだけ
 * よく分からなければクラスで定義してもOK。コーデックを利用した読み書きさえできれば大丈夫
 * 
 * このレコードでは、BlockStateとintを定義している
 */
public record ExampleFeatureConfiguration(BlockState state, int height) implements FeatureConfiguration {

	/**
	 * このレコードに対応するコーデック
	 * BlockStateの値と、1から64までの範囲をとるintの値をjsonファイルから読み書きする
	 */
	public static final Codec<ExampleFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance ->
	instance.group(
			BlockState.CODEC.fieldOf("state").forGetter(ExampleFeatureConfiguration::state),
			Codec.intRange(1, 64).fieldOf("height").forGetter(ExampleFeatureConfiguration::height)
			).apply(instance, ExampleFeatureConfiguration::new));
}
