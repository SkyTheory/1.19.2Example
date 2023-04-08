package skytheory.example.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import skytheory.example.block.ItemHandlerBlock;
import skytheory.example.capability.InventoryHandler;
import skytheory.example.capability.ItemHandlerWrapperExtractOnly;
import skytheory.example.capability.ItemHandlerWrapperInsertOnly;
import skytheory.example.gui.SimpleProcessorMenu;
import skytheory.example.init.BlockEntityInit;
import skytheory.example.recipe.SimpleProcessorRecipe;

public class SimpleProcessorEntity extends BlockEntity implements MenuProvider, ItemHandlerBlock<ListTag> {

	// 200tick（10秒）掛けてアイテムのクラフトを行う
	public static final int PROCESSING_TIME = 200;

	public final InventoryHandler input;
	public final InventoryHandler output;
	public final IItemHandlerModifiable inputAccessor;
	public final IItemHandlerModifiable outputAccessor;

	// 現在クラフトを実行しようとしているレシピ
	public Optional<SimpleProcessorRecipe> currentRecipe;
	// 加工の進捗
	// これがPROCESSING_TIME（200）に達するとクラフトを行う
	public int progress;
	// 加工を一時停止しているかどうか
	public boolean pause;
	// インプットのスロットに変更があったかどうか
	public boolean inputChanged;

	public SimpleProcessorEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.SIMPLE_PROCESSOR.get(), pos, state);
		// 4スロットのインベントリを作成
		this.input = new InventoryHandler(4);
		// 内容物に変更があれば、setChangedを呼ぶ
		input.addChangedListener(this::setChanged);
		// 更に、次のtickでレシピの検証を行うようにフラグを更新する
		input.addChangedListener(() -> this.inputChanged = true);
		
		// 1スロットのインベントリを作成
		this.output = new InventoryHandler(1);
		// 内容物に変更があれば、setChangedを呼ぶ
		output.addChangedListener(this::setChanged);
		// 更に、アイテムの出力の一時停止を解除する
		output.addChangedListener(() -> this.pause = false);
		
		// inputに対する、搬入可、搬出不可でのアクセスができるIItemHandlerを作成
		this.inputAccessor = new ItemHandlerWrapperInsertOnly(input);
		
		// outputに対する、搬出可、搬入不可でのアクセスができるIItemHandlerを作成
		this.outputAccessor = new ItemHandlerWrapperExtractOnly(output);
		
		// Optionalを扱うのならば、必ず初期化を行う（Optional型にnullが入っている状態を作らない）こと！
		this.currentRecipe = Optional.empty();
	}

	/**
	 * クライアント側でtickごとに何かする<br>
	 * 動作させるためにはTickerが必要、詳細はBlock側を参照<br>
	 * なお、このBlockEntityではクライアント側では何もしない<br>
	 * <br>
	 * 例えば、描画のためにクライアント側でも内容物の更新を行わなければならない場合など<br>
	 * クライアント側で（あるいは、サーバー・クライアントの両方）で必要な処理がある場合はここに記述<br>
	 * <br>
	 * ただし、GUI上でのみ同期が必要なときだけサーバーからデータを同期してプレイヤーに見せる、という場合は<br>
	 * BlockEntityではなく、AbstractContainerMenu側で同期を行う方法もある<br>
	 * {@link skytheory.example.gui.SimpleProcessorMenu}<br>
	 * ここのaddSlotで追加したスロットのItemStack及びaddDataSlotで追加したint値が同期対象となる<br>
	 */
	public void clientTick() {
	}

	/**
	 * サーバー側でtickごとに何かする
	 * 動作させるためにはTickerが必要、詳細はBlock側を参照
	 */
	public void serverTick() {
		// 入力スロットに変更があった場合
		if (this.inputChanged) {
			// クラフトするレシピの検証を行う
			this.updateRecipe();
		}
		// 利用可能なレシピがある（材料が全部インプットのスロットに入っている）場合
		if (currentRecipe.isPresent()) {
			/**
			 *  progressを1増やして（加工タイマーを1個進めて）
			 *  それがPROCESSING_TIME以上か（タイマーが進み切ったか）をチェックする
			 */
			progress = Math.min(progress + 1, PROCESSING_TIME);
			this.setChanged();
			if (progress >= PROCESSING_TIME) {
				// 加工を一時停止していなければ
				if (!pause) {
					// レシピを元にクラフトを試みる
					this.onCrafting(currentRecipe.get());
				}
			}
		}
	}

	public void onCrafting(SimpleProcessorRecipe recipe) {
		// インプット側に入っているItemStackのリストを作成して
		List<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < input.getSlots(); i++) {
			items.add(input.getStackInSlot(i));
		}
		// レシピから完成品を取得
		ItemStack result = recipe.getResultItem(items);
		// アウトプットのインベントリにアイテムを入れることができるならば
		if (output.insertItem(0, result, true).isEmpty()) {
			// 完成品をアウトプットに入れて
			output.insertItem(0, result, false);
			for (int i = 0; i < input.getSlots(); i++) {
				// 材料をひとつずつ減らす
				ItemStack item = items.get(i);
				item.shrink(1);
				if (item.isEmpty()) input.setStackInSlot(i, ItemStack.EMPTY);
			}
			// 最後に、加工タイマーをゼロに戻して終了
			this.progress = 0;
			// 完成品を入れることができなければ
		} else {
			// インベントリに変更があるまで、加工を一時停止
			this.pause = true;
		}
	}

	/**
	 * インプットのインベントリに入っているアイテムから、レシピか利用可能かの判定を行う
	 */
	public void updateRecipe() {
		// インプット側に入っているItemStackのリストを作成して
		List<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < input.getSlots(); i++) {
			items.add(input.getStackInSlot(i));
		}
		// 利用するレシピを取得
		Optional<SimpleProcessorRecipe> recipe = SimpleProcessorRecipe.getRecipe(getLevel(), items);
		if (!currentRecipe.equals(recipe)) {
			currentRecipe = recipe;
			this.progress = 0;
		}
	}

	/**
	 * 現在の加工タイマーの値をNBTに書き込む
	 * インベントリの内容を保存するのはCapabilityの方でよしなにやってくれる
	 * {@link skytheory.example.capability.DataProvider#serializeNBT()}
	 */
	@Override
	public void saveAdditional(CompoundTag pCompoundTag) {
		super.saveAdditional(pCompoundTag);
		pCompoundTag.putInt("Progress", this.progress);
	}

	/**
	 * 現在の加工タイマーの値をNBTから読みこむ
	 * インベントリの内容を復元するのはIItemHandlerの方でよしなにやってくれる
	 * {@link skytheory.example.capability.DataProvider#deserializeNBT(Tag)}
	 */
	@Override
	public void load(CompoundTag pCompoundTag) {
		super.load(pCompoundTag);
	}

	/**
	 * BlockEntityの読み込み時に実行される
	 * 加工タイマーをリセットすることなく、レシピの初期化を行う
	 */
	@Override
	public void onLoad() {
		super.onLoad();
		List<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < input.getSlots(); i++) {
			items.add(input.getStackInSlot(i));
		}
		// 利用するレシピを取得
		Optional<SimpleProcessorRecipe> recipeOpt = SimpleProcessorRecipe.getRecipe(getLevel(), items);
		this.currentRecipe = recipeOpt;
	}

	/**
	 * MenuProviderからの継承メソッド
	 * AbstractContainerMenuを作成して、プレイヤーに表示するGUIとして返す
	 */
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
		return new SimpleProcessorMenu(id, inv, this);
	}

	/**
	 * MenuProviderからの継承メソッド
	 * GUI左上に表示する文字列を返す
	 */
	@Override
	public Component getDisplayName() {
		return Component.translatable("block.st_example.simple_processor");
	}

	/**
	 * ItemHandlerBlockからの継承メソッド
	 * 下面からの接続にはアウトプットを返し、それ以外からの接続にはインプットを返す
	 * {@link skytheory.example.event.BlockEvent#onAttachCapabilities(net.minecraftforge.event.AttachCapabilitiesEvent)}
	 * {@link skytheory.example.capability.DataProvider}
	 */
	@Override
	public @Nullable IItemHandler getItemHandler(Direction direction) {
		return direction == Direction.DOWN ? outputAccessor : inputAccessor;
	}

	/**
	 * ItemHandlerBlockからの継承メソッド
	 * IItemHandlerのデータの読み書きを行うためのシリアライザーを渡す
	 * {@link skytheory.example.event.BlockEvent#onAttachCapabilities(net.minecraftforge.event.AttachCapabilitiesEvent)}
	 * {@link skytheory.example.capability.DataProvider}
	 */
	@Override
	public @NotNull INBTSerializable<ListTag> getSerializer() {
		return new INBTSerializable<ListTag>() {

			@Override
			public ListTag serializeNBT() {
				ListTag list = new ListTag();
				list.add(SimpleProcessorEntity.this.input.serializeNBT());
				list.add(SimpleProcessorEntity.this.output.serializeNBT());
				return list;
			}

			@Override
			public void deserializeNBT(ListTag nbt) {
				if (nbt.getElementType() == Tag.TAG_COMPOUND && nbt.size() == 2) {
					SimpleProcessorEntity.this.input.deserializeNBT(nbt.getCompound(0));
					SimpleProcessorEntity.this.output.deserializeNBT(nbt.getCompound(1));
				}
			}

		};
	}
	
}
