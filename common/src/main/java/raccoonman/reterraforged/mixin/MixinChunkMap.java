package raccoonman.reterraforged.mixin;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

import net.minecraft.world.level.TicketStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.datafixers.DataFixer;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ChunkStatusUpdateListener;
// ^ broke on 1.21.11 somehow (KoyuKansou)
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import raccoonman.reterraforged.world.worldgen.RTFRandomState;

@Mixin(ChunkMap.class)
public class MixinChunkMap {
	@Final
	@Shadow
    private RandomState randomState;

	@Inject(
		at = @At("TAIL"),
		method = "<init>"
	)
	public void ChunkMap(ServerLevel serverLevel, LevelStorageSource.LevelStorageAccess levelStorageAccess, DataFixer dataFixer, StructureTemplateManager structureTemplateManager, Executor executor, BlockableEventLoop blockableEventLoop, LightChunkGetter lightChunkGetter, ChunkGenerator chunkGenerator, ChunkStatusUpdateListener chunkStatusUpdateListener, Supplier<SavedDataStorage> supplier, TicketStorage ticketStorage, int i, boolean bl, CallbackInfo ci) {
		if((Object) this.randomState instanceof RTFRandomState rtfRandomState) {
			rtfRandomState.initialize(serverLevel.registryAccess());
		}
	}
}
