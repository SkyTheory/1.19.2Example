package skytheory.example.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import skytheory.example.init.RecipeSerializerInit;
import skytheory.example.init.RecipeTypeInit;

/**
 * Recipeクラスを利用することで、データパックによるレシピの編集を実装できる
 * 必要なのはRecipeクラス（これ）、RecipeType、RecipeSerializerの3つ
 * RecipeTypeとRecipeSerializerはForgeRegistryへの登録が必要
 * Containerを指定している総称型の部分は、バニラに準拠した実装方法で作成する際に用いることになる
 * 独自レシピを作成する際には、多くの場合で不要（ContainerでOK）
 * @author SkyTheory
 *
 */
public class SimpleProcessorRecipe implements Recipe<Container> {

	private static final Logger LOGGER = LogUtils.getLogger();
	
	private final ResourceLocation id;
	private final ItemStack result;
	private final List<Ingredient> ingredients;
	
	/**
	 * レシピのコンストラクタ
	 * 引数はデータパック内のファイル名にあたるid、完成品のItemStack、材料となるアイテムを指定するIngredient
	 * @param id
	 * @param result
	 * @param ingredients
	 */
	public SimpleProcessorRecipe(ResourceLocation id, ItemStack result, List<Ingredient> ingredients) {
		this.id = id;
		this.result = result.copy();
		this.ingredients = ImmutableList.copyOf(ingredients);
		if (result.isEmpty()) throw new IllegalArgumentException("Recipe has no result: " + id.toString());
		if (ingredients.isEmpty()) throw new IllegalArgumentException("Recipe has no ingredients: " + id.toString());
	}
	
	/**
	 * 材料を持つContainerを引数にとり、レシピ通りの材料が入っているかどうかを判定する
	 * 独自実装を行う場合、別の方法でも可能
	 */
	@Override
	public boolean matches(Container pContainer, Level pLevel) {
		throw new UnsupportedOperationException("Use a method that takes a List<ItemStack> as an argument.");
	}

	/**
	 * 材料を持つContainerを引数にとり、完成品を返す
	 * 独自実装を行う場合、別の方法でも可能
	 * 今回はそちらを行う
	 */
	@Override
	public ItemStack assemble(Container p_44001_) {
		throw new UnsupportedOperationException("Use a method that takes a List<ItemStack> as an argument.");
	}

	/**
	 * Containerの大きさから、クラフトレシピが適用可能かどうかを判定する
	 * 独自マシンで用いる場合には気にせずともOK（あるいは、別の方法で判定を実装する）
	 */
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		throw new UnsupportedOperationException();
	}

	/**
	 * LevelとItemStackのリストを引数に、利用可能なレシピを検索する
	 * @param level
	 * @param ingredients
	 * @return Recipe that can be crafted from ingredients. Returns Optional#empty if the recipe is not found.
	 */
	public static Optional<SimpleProcessorRecipe> getRecipe(Level level, List<ItemStack> ingredients) {
		// 引数のLevelからRecipeManagerを呼び出し、全てのSimpleProcessorRecipeを取得
		return level.getRecipeManager().getAllRecipesFor(RecipeTypeInit.SIMPLE_PROCESSOR_TYPE.get()).stream()
				// 材料が合致するかどうかでフィルタリングを行い
				.filter(t -> t.matches(ingredients))
				// 最初にヒットしたレシピを返す
				.findFirst();
	}
	
	/**
	 * List<ItemStack>を引数に、それらがレシピの材料と合致するかを判定する
	 * @param items
	 * @return Recipe matched
	 */
	public boolean matches(List<ItemStack> items) {
		// itemを元に、その中身のコピーを持つArrayListを作成する
		List<ItemStack> checkList = items.stream()
				// 空白スロットを除外しつつ
				.filter(Predicate.not(ItemStack::isEmpty))
				// 安全のため、ItemStackそのものではなく、そのコピーを持つようにして
				.map(ItemStack::copy)
				// ArrayListに格納する
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		// 特殊な判定方法を実装する場合はここから独自処理を書いていくことになるが
		// 単純にIngredientが揃っているかどうかを判定するだけであれば以下のメソッドで行える
		// 引数のリストの長さが同じである場合にのみ利用可能であることは留意すること
		return RecipeMatcher.findMatches(checkList,  this.ingredients) != null;
	}
	
	/**
	 * List<ItemStack>を引数に、完成品のアイテムを返す
	 * 例えば、材料の中に〇〇があるなら完成品に××のエンチャントを付与するといったことを行う場合
	 * このタイミングで完成品にエンチャントを付与する
	 * @return Result item
	 */
	public ItemStack getResultItem(List<ItemStack> items) {
		ItemStack result = this.result.copy();
		return result;
	}
	
	/**
	 * 完成品のItemStackを返す
	 * レシピの持つItemStackを直接渡すのでなく、そのコピーを返すようにすること
	 * 原理的には、ItemStack以外の完成品を指定するレシピも作成可能
	 * その場合このメソッドは未使用となる
	 */
	@Override
	public ItemStack getResultItem() {
		return this.result.copy();
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}
	
	/**
	 * ここでForgeRegistryに登録したRecipeSerializerを返すようにする
	 */
	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipeSerializerInit.SIMPLE_PROCESSOR_TYPE.get();
	}

	/**
	 * ここでForgeRegistryに登録したRecipeTypeを返すようにする
	 */
	@Override
	public RecipeType<?> getType() {
		return RecipeTypeInit.SIMPLE_PROCESSOR_TYPE.get();
	}

	/**
	 * DataGeneratorなどで利用するために、レシピからjsonへの変換を行うメソッドを用意しておく
	 * レシピのjsonファイルを手書きで作成するのなら不要
	 * @param pRecipe
	 * @return
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		// RecipeSerializerのキーを書き込み
		json.addProperty("type", RecipeSerializerInit.SIMPLE_PROCESSOR_TYPE.getId().toString());
		// 完成品のItemStackをItemStackのコーデックから書き込み
		JsonElement result = ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.getResultItem()).getOrThrow(true, LOGGER::error);
		json.add("result", result);
		JsonArray ingredients = new JsonArray();
		// Streamを用いてIngredientのリスト→JsonElementのリストへ変換→リストの中身をJsonArrayに書き込みと処理
		this.ingredients.stream().map(Ingredient::toJson).forEach(ingredients::add);
		json.add("ingredients", ingredients);
		return json;
	}
	
}
