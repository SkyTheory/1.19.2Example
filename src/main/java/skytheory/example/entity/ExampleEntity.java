package skytheory.example.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class ExampleEntity extends PathfinderMob {

	/*
	 * Entityの当たり判定の幅、高さ、視点の位置などを定数として保持
	 * ちょっと大きく、あるいは小さくしたいなあ、なんて思ったらここの値を書き換えればオーケー
	 */

	public static final float HITBOX_WIDTH = 0.5f;
	public static final float HITBOX_HEIGHT = 1.40625f;
	public static final float EYE_HEIGHT_BASE = 1.171875f;
	public static final float EYE_HEIGHT_MODIFIER = EYE_HEIGHT_BASE / HITBOX_HEIGHT;

	public ExampleEntity(EntityType<? extends ExampleEntity> type, Level level) {
		super(type, level);
	}

	/**
	 * Entityのレジストリ登録用の情報を記述する
	 */
	public static EntityType<ExampleEntity> createEntityType() {
		EntityType<ExampleEntity> type =
				EntityType.Builder
				.of(ExampleEntity::new, MobCategory.CREATURE)
				.sized(ExampleEntity.HITBOX_WIDTH, ExampleEntity.HITBOX_HEIGHT)
				.build("st_example:example_entity"); 
		return type;
	}
	
	/**
	 * Entityのワールド内での基本情報を設定する
	 */
	public static AttributeSupplier createAttributeSupplier() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.3d)
				.add(Attributes.MAX_HEALTH, 20.0d)
				.add(Attributes.FOLLOW_RANGE, 48.0d);
		return builder.build();
	}


	/**
	 * ここに登録した処理に基づいて行動するようになる
	 * いわゆるEntityのAI
	 * 引数に設定した優先度が小さい順に実行される
	 */
	@Override
	protected void registerGoals() {
		/*
		 * 要するにこのEntityは
		 * 
		 * 水に浮かぶ
		 * Enemyを実装したLivingEntityから逃げる
		 * ダメージを受けると走り回る
		 * HPが最大でない時にパンを食べる（よって、EntityEventのonItemUseFinishが発生する）
		 * ダイヤモンドを持っているプレイヤーに付いていく
		 * 水を避けて、ランダムに移動する
		 * 近くのプレイヤーを見る
		 * 
		 * 以上の行動を実行するAIを持つことになる
		 */
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LivingEntity.class, 8.0f, 1.0f, 1.5f, (Enemy.class::isInstance)));
		this.goalSelector.addGoal(2, new PanicGoal(this, 1.5d));
		this.goalSelector.addGoal(3, new UseItemGoal<>(this, new ItemStack(Items.BREAD), SoundEvents.ITEM_PICKUP, this::isDamaged));
		this.goalSelector.addGoal(4, new TemptGoal(this, 1.0d, Ingredient.of(Items.DIAMOND), false));
		this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0d));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0f));
	}

	/**
	 * Entityの視点の高さを設定する
	 */
	@Override
	public float getEyeHeight(Pose pose) {
		return this.getDimensions(pose).height * EYE_HEIGHT_MODIFIER;
	}

	/**
	 * Entityの速度を設定する
	 * ここではgetScale()で得られた大きさによって変わるようにしている
	 * 特に変更の必要がないなら、スーパークラスの実装そのままで大丈夫
	 */
	@Override
	public float getSpeed() {
		return super.getSpeed() * getScale();
	}

	/**
	 * Entityのサイズを指定する
	 * 例えば0.5fを入れれば、Entityが半分のサイズになる
	 */
	@Override
	public float getScale() {
		return 1.0f;
	}

	@Override
	public void tick() {
		super.tick();
		this.tickUpdate();
	}

	/**
	 * tickごとに何かを実行したい時は、ここにその内容を書くこと
	 */
	protected void tickUpdate() {

	}
	
	/**
	 * UseItemGoalを実行するか判断する
	 */
	public boolean isDamaged(ExampleEntity entity) {
		return this.getHealth() < this.getMaxHealth();
	}

	/**
	 * EntityEventの方から呼ばれて、パンを食べた時に回復するようになる
	 */
	public void onItemUseFinish(ItemStack stack) {
		if (stack.getItem() == Items.BREAD) {
			this.heal(this.getMaxHealth());
		}
	}
}
