package skytheory.example.util;

import java.util.Arrays;

import net.minecraft.core.Direction;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityUtils {

	public static final Direction[] DIRECTIONS = Arrays.copyOf(Direction.values(), Direction.values().length + 1);
	
	public static <T> LazyOptional<T> create(T instance) {
		if (instance == null) return LazyOptional.empty();
		return LazyOptional.of(() -> instance);
	}
}
