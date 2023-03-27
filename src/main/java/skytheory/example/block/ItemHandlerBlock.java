package skytheory.example.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

/**
 *  DataProviderにメソッド参照で渡すためのインターフェース
 *  Capabilityって結構ややこしいよね……。
 * @author SkyTheory
 *
 * @param <T>
 */
public interface ItemHandlerBlock<T extends Tag> {

	/**
	 * DataProviderの方で用いるために、IItemHandlerの値を返す
	 * LazyOptionalで包む前に、DataProvider側でnullチェックをしているのでここではnull許容
	 * @param direction
	 * @return
	 */
	@Nullable
	public IItemHandler getItemHandler(Direction direction);
	
	/**
	 * IItemHandlerのデータを読み書きするためのシリアライザーを取得
	 * 良く分からなければ、とりあえずItemStackHandlerをそのまま渡せばOK
	 * こっちでnullを返すとNPEが発生するので注意！
	 * @return
	 */
	@NotNull
	public INBTSerializable<T> getSerializer();
	
}
