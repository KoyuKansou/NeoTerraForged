package raccoonman.reterraforged.platform;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import raccoonman.reterraforged.registries.RTFRegistries;
import raccoonman.reterraforged.world.worldgen.biome.modifier.BiomeModifier;

@Deprecated
public final class RegistryUtil {
	
	public static <T> void register(Registry<T> registry, String name, T value) {
		getWritable(registry).register(RTFRegistries.createKey(registry.key(), name), value, RegistrationInfo.BUILT_IN);
	}
	
	public static <T> WritableRegistry<T> getWritable(Registry<T> registry) {
		return (WritableRegistry<T>) registry;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Registry<T> createRegistry(ResourceKey<? extends Registry<T>> key) {
		return FabricRegistryBuilder.create((ResourceKey<Registry<T>>) key).buildAndRegister();
	}

	public static <T> void createDataRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
		DynamicRegistries.register(key, codec);
	}
}
