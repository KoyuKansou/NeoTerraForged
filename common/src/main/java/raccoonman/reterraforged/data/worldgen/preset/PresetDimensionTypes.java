package raccoonman.reterraforged.data.worldgen.preset;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;
import raccoonman.reterraforged.data.worldgen.preset.settings.Preset;
import raccoonman.reterraforged.data.worldgen.preset.settings.WorldSettings;

public final class PresetDimensionTypes {
	
	public static void bootstrap(Preset preset, BootstrapContext<DimensionType> ctx) {
		WorldSettings worldSettings = preset.world();
		WorldSettings.Properties properties = worldSettings.properties;
		int worldHeight = properties.worldHeight;
		int worldDepth = properties.worldDepth;
		int totalHeight = worldDepth + worldHeight;
		
		HolderGetter<Timeline> timelines = ctx.lookup(Registries.TIMELINE);

		// Build the overworld environment attributes matching vanilla overworld.json
		EnvironmentAttributeMap attributes = EnvironmentAttributeMap.builder()
			.set(EnvironmentAttributes.SKY_COLOR, 0x78a7ff)
			.set(EnvironmentAttributes.FOG_COLOR, 0xc0d8ff)
			.set(EnvironmentAttributes.CLOUD_COLOR, 0xccffffff)
			.set(EnvironmentAttributes.CLOUD_HEIGHT, 192.33f)
			.set(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, true)
			.set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, false)
			.build();

        ctx.register(BuiltinDimensionTypes.OVERWORLD, new DimensionType(
	false, // hasFixedTime
	true,  // hasSkyLight
	false, // hasCeiling
	false, // ultraWarm
	1.0,   // coordinateScale
	-worldDepth,   // minY
	totalHeight,   // height
	totalHeight,   // logicalHeight
	BlockTags.INFINIBURN_OVERWORLD, // infiniburn
	0.0f,  // ambientLight
	new DimensionType.MonsterSettings(UniformInt.of(0, 7), 0),
	DimensionType.Skybox.OVERWORLD,
	CardinalLighting.Type.DEFAULT,
	attributes,
	timelines.getOrThrow(TimelineTags.IN_OVERWORLD),
	java.util.Optional.empty() // worldClock
        ));
	}
}
