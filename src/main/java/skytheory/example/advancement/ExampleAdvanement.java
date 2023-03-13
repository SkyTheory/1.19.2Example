package skytheory.example.advancement;

import java.util.function.Consumer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import skytheory.example.init.ItemInit;

public class ExampleAdvanement implements Consumer<Consumer<Advancement>> {

	// 背景画像の指定
	// 開始地点となる進捗に設定する
	public static final ResourceLocation BACK_GROUND = new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png");

	// 本来はjsonから読み取るものだが、DataGeneratorで逆にjsonを生成する際にも使える
	private static final ItemPredicate MACGUFFIN_ITEM_PREDICATE = ItemPredicate.Builder.item().of(ItemInit.MACGUFFIN.get()).build();

	public void accept(Consumer<Advancement> consumer) {
		
		Advancement root_advancement = Advancement.Builder.advancement()
				.display(ItemInit.MUFFIN.get(), Component.translatable("st_example:advancements.root_title"),
						Component.translatable("st_example:advancements.root_description"),
						// 背景を指定する
						BACK_GROUND,
						// アイコンの枠の種類を指定する
						FrameType.TASK,
						// 画面右上に表示するかどうか
						false,
						// 達成時にチャットに表示するかどうか
						false,
						// 進捗を隠すかどうか
						true)
				.addCriterion("falling_damage", new FallingDamageCriterionTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
				.save(consumer, "st_example:main/root");
		
		Advancement.Builder.advancement()
				.parent(root_advancement)
				.display(ItemInit.MACGUFFIN.get(), Component.translatable("st_example:advancements.macguffin_title"),
						Component.translatable("st_example:advancements.macguffin_description"),
						// 背景の指定はルートの進捗ひとつで十分
						(ResourceLocation)null,
						// アイコンの枠の種類を指定する
						FrameType.GOAL,
						// 画面右上に表示するかどうか
						true,
						// 達成時にチャットに表示するかどうか
						true,
						// 進捗を隠すかどうか
						false)
				.addCriterion("throw_macguffin", new ItemThrownCriterionTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MACGUFFIN_ITEM_PREDICATE))
				.save(consumer, "st_example:main/throw_mcguffin");
	}

}