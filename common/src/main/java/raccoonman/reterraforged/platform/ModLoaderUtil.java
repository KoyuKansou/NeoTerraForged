package raccoonman.reterraforged.platform;

import net.fabricmc.loader.api.FabricLoader;

public class ModLoaderUtil {

	public static boolean isLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}
}
