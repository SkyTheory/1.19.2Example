package skytheory.example.network;

import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import skytheory.example.ExampleMod;

public class BlockMessage {

	public BlockPos pos;
	public String dimension;
	public CompoundTag tag;

	@SuppressWarnings("unchecked")
	public BlockMessage(BlockEntity block) {
		this.pos = block.getBlockPos();
		this.dimension = block.getLevel().dimension().location().toString();
		IItemHandler handler = block.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		Validate.notNull(handler);
		this.tag = ((INBTSerializable<CompoundTag>) handler).serializeNBT();
	}

	private BlockMessage(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		this.dimension = buf.readUtf();
		this.tag = buf.readNbt();
	}

	public static void encode(BlockMessage msg, FriendlyByteBuf buf) {
		buf.writeBlockPos(msg.pos);
		buf.writeUtf(msg.dimension);
		buf.writeNbt(msg.tag);
	}

	public static BlockMessage decode(FriendlyByteBuf buf) {
		return new BlockMessage(buf);
	}

	public static void process(BlockMessage msg, Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.setPacketHandled(true);
		ctx.enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacketClient(msg, ctx));
			DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> handlePacketServer(msg, ctx));
		});
	}

	/**
	 * メッセージがサーバーからクライアントへ呼ばれたときに呼び出される
	 * @param msg
	 * @param ctx
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	private static void handlePacketClient(BlockMessage msg, NetworkEvent.Context ctx) {
		LocalPlayer player = Minecraft.getInstance().player;
		Level level = player.level;
		if (!msg.dimension.equals(level.dimension().location().toString())) {
			ExampleMod.LOGGER.error("Sync failed: Invalid dimension.");
			return;
		}
		if (!level.hasChunkAt(msg.pos)) {
			ExampleMod.LOGGER.error("Sync failed: Chunk not exist.");
			return;
		}
		BlockEntity block = level.getBlockEntity(msg.pos);
		if (block == null) {
			ExampleMod.LOGGER.error("Sync failed: Missing packet object.");
			return;
		}
		IItemHandler handler = block.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		if (handler == null) {
			ExampleMod.LOGGER.error("Sync failed: Invalid packet object.");
			return;
		}
		try {
			@SuppressWarnings("unchecked")
			INBTSerializable<CompoundTag> sync = (INBTSerializable<CompoundTag>) handler;
			sync.deserializeNBT(msg.tag);
		} catch (ClassCastException e) {
			ExampleMod.LOGGER.error("Sync failed: Invalid packet object.");
			return;
		}
	}
	
	/**
	 * メッセージがサーバーからクライアントへ呼ばれたときに呼び出される
	 * @param msg
	 * @param ctx
	 */
	@SuppressWarnings("deprecation")
	private static void handlePacketServer(BlockMessage msg, NetworkEvent.Context ctx) {
		ServerPlayer player = ctx.getSender();
		Level level = player.level;
		if (!msg.dimension.equals(level.dimension().location().toString())) {
			ExampleMod.LOGGER.error("Sync failed: Invalid dimension.");
			return;
		}
		if (!level.hasChunkAt(msg.pos)) {
			ExampleMod.LOGGER.error("Sync failed: Chunk not exist.");
			return;
		}
		BlockEntity block = level.getBlockEntity(msg.pos);
		if (block == null) {
			ExampleMod.LOGGER.error("Sync failed: Missing packet object.");
			return;
		}
		PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new BlockMessage(block));
	}
	
}
