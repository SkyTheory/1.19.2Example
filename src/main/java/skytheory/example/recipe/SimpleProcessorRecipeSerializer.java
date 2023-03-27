package skytheory.example.recipe;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * SimpleProcessorRecipeに対応するRecipeSerializer
 * 名前の通り、レシピの読み書きを行うために用いる
 * 
 * jsonを読み取ってレシピを作成する
 * サーバー側にあるレシピをパケットに乗せてクライアント側に飛ばす
 * クライアント側でパケットからレシピを復元する
 * 
 * 以上の3つが必要
 * @author SkyTheory
 *
 */
public class SimpleProcessorRecipeSerializer implements RecipeSerializer<SimpleProcessorRecipe> {

	private static final Logger LOGGER = LogUtils.getLogger();
	
	/**
	 * データパックに含まれるjsonファイルからレシピを生成する
	 */
	@Override
	public SimpleProcessorRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
		// まず完成品をjsonから読み込んで
		ItemStack result = ItemStack.CODEC.parse(JsonOps.INSTANCE, pSerializedRecipe.get("result")).getOrThrow(true, LOGGER::error);
		// JsonArrayをjsonから読み取り
		JsonArray jsonArray = pSerializedRecipe.getAsJsonArray("ingredients");
		// JsonArrayから材料のリストに変換する
		List<Ingredient> ingredients = Streams.stream(jsonArray).map(Ingredient::fromJson).toList();
		// ID、完成品、材料リストからレシピのインスタンスを生成
		return new SimpleProcessorRecipe(pRecipeId, result, ingredients);
	}

	/**
	 * サーバー側から受け取ったパケットから、レシピを復元する
	 * 必ず書き込んだ時と同じ順序で要素を読み込むこと
	 */
	@Override
	public @Nullable SimpleProcessorRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
		// 完成品のItemStackを読み込む
		ItemStack result = pBuffer.readItem();
		// Ingredientの個数を読み込む
		int size = pBuffer.readVarInt();
		List<Ingredient> ingredients = new ArrayList<>();
		// 読み取った個数の分だけ、繰り返し処理でIngredientを読み込む
		for (int i = 0; i < size; i++) {
			ingredients.add(Ingredient.fromNetwork(pBuffer));
		}
		// 読み取った要素からレシピを作成
		return new SimpleProcessorRecipe(pRecipeId, result, ingredients);
	}

	/**
	 * サーバー側からクライアント側に同期するために、レシピをパケットに乗せる
	 * 必ず読み込む時と同じ順序で値を書き込むこと
	 */
	@Override
	public void toNetwork(FriendlyByteBuf pBuffer, SimpleProcessorRecipe pRecipe) {
		// 完成品のItemStackを書き込む
		pBuffer.writeItem(pRecipe.getResultItem());
		NonNullList<Ingredient> ingredients = pRecipe.getIngredients();
		// Ingredientsのサイズを書き込む
		pBuffer.writeVarInt(ingredients.size());
		// Ingredientを全て書き込む
		ingredients.stream().forEach(t -> t.toNetwork(pBuffer));
	}

}
