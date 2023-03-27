package skytheory.example.capability;

/**
 *  IItemHandlerに変更があった際に呼ばれる
 *  これを利用することで、例えば
 *  	インベントリの中身が変わったので、アンロード時にデータを保存するフラグを建てる
 *  	描画用にクライアントにデータの同期処理を飛ばす
 *  などといったことが可能
 */
public interface ItemHandlerListener {

	/**
	 * NBTからインベントリを読み込んだ際に呼ばれる
	 * @param itemHandler
	 */
	public void onLoad(InventoryHandler itemHandler);
	
	/**
	 * インベントリの中身に変更があった際に呼ばれる
	 * @param itemHandler
	 * @param slot
	 */
	public void onContentsChanged(InventoryHandler itemHandler, int slot);
	
}
