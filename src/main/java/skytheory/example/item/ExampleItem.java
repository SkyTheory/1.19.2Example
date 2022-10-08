package skytheory.example.item;

import net.minecraft.world.item.Item;

/**
 * アイテムに追加で何かしらの機能を追加したい場合は、Itemクラスを拡張するなどして対応する
 * このクラスのように、何も機能の追加を行わない場合は本来は不要、通常のItemクラスのインスタンスでOK
 */
public class ExampleItem extends Item {

	public ExampleItem(Properties properties) {
		super(properties);
	}
}
