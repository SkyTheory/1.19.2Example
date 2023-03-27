package skytheory.example.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * IItemHandlerのCapabilityを用いる際に、アイテムのインベントリを作る際の実装例
 * ItemStackHandlerというクラスに一通りの機能が存在するので、それを継承する
 * インスタンスを生成する際にItemHandlerListenerを引数に渡しておけば、中身に変更があった際にそれを通知する用に実装
 * @author SkyTheory
 *
 */
public class InventoryHandler extends ItemStackHandler {

	private final ItemHandlerListener[] listeners;

	public InventoryHandler(int size, ItemHandlerListener... listeners) {
		super(size);
		this.listeners = listeners;
	}


	/**
	 * 仕様変更時に要らない苦労を背負いたくないのならば
	 * データの読み出しの際にsetSizeをしないようにしておくのが良いと思う
	 * （不正なNBTを持つBlockEntityがワールドに存在するとクラッシュの原因になる）
	 */
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
		for (int i = 0; i < Math.min(tagList.size(), this.getSlots()); i++) {
			CompoundTag itemTags = tagList.getCompound(i);
			int slot = itemTags.getInt("Slot");
			if (slot >= 0 && slot < stacks.size()) {
				stacks.set(slot, ItemStack.of(itemTags));
			}
		}
		for (int i = tagList.size(); i < this.getSlots(); i++) {
			stacks.set(i, ItemStack.EMPTY);
		}
		onLoad();
	}

	@Override
	protected void onLoad() {
		for (ItemHandlerListener listener : listeners) {
			listener.onLoad(this);
		}
	}

	@Override
	protected void onContentsChanged(int slot) {
		for (ItemHandlerListener listener : listeners) {
			listener.onContentsChanged(this, slot);
		}
	}
}
