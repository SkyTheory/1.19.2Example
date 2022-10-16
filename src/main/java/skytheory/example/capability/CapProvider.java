package skytheory.example.capability;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import skytheory.example.ExampleMod;
import skytheory.example.util.CapabilityUtils;

public class CapProvider<T> implements ICapabilitySerializable<Tag> {
	
	public final INBTSerializable<Tag> serializer;
	public final Capability<T> cap;
	protected final Map<Direction, LazyOptional<T>> datas;
	
	@SuppressWarnings("unchecked")
	public CapProvider(Capability<T> cap, T data) {
		this(cap, data, (INBTSerializable<Tag>) data);
	}
	
	public CapProvider(Capability<T> cap, T data, INBTSerializable<Tag> serializer) {
		this.cap = cap;
		Validate.notNull(data);
		this.datas = new HashMap<>();
		for (Direction direction : CapabilityUtils.DIRECTIONS) {
			this.datas.put(direction, CapabilityUtils.create(data));
		}
		this.serializer = serializer;
	}
	
	public CapProvider(Capability<T> cap, Map<Direction, T> dataMap, INBTSerializable<Tag> serializer) {
		this.cap = cap;
		if (dataMap.isEmpty()) {
			throw new UnsupportedOperationException("Try to create empty capability.");
		}
		this.datas = new HashMap<>();
		for (Direction direction : CapabilityUtils.DIRECTIONS) {
			T data = dataMap.get(direction);
			this.datas.put(direction, CapabilityUtils.create(data));
		}
		this.serializer = serializer;
	}
	
	@Override
	public <X> @NotNull LazyOptional<X> getCapability(@NotNull Capability<X> cap, @Nullable Direction side) {
		if (cap.equals(this.cap)) {
			return this.datas.get(side).cast();
		}
		return LazyOptional.empty();
	}

	public Tag serializeNBT() {
		if (serializer == null) return new CompoundTag();
		return serializer.serializeNBT();
	}

	// 1.12.2の頃はForge側でcatchしてくれてた気がするんだけれどなー？
	// 処理の重さとか考慮して外されたんだろうか
	@Override
	public void deserializeNBT(Tag nbt) {
		if (serializer != null) {
			try {
				serializer.deserializeNBT(nbt);
			} catch (ClassCastException e) {
				ExampleMod.LOGGER.error("Cannot deserialize capability: " + nbt.getAsString());
			}
		}
	}
}
