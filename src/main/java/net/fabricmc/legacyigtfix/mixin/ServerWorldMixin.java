package net.fabricmc.legacyigtfix.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin extends World {


    protected ServerWorldMixin(SaveHandler handler, LevelProperties properties, Dimension dim, Profiler profiler, boolean isClient) {
        super(handler, properties, dim, profiler, isClient);
    }

    @Shadow
    protected ChunkProvider getChunkCache() {
        return null;
    }

    @Shadow
    protected int getNextMapId() {
        return 0;
    }

    //@Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelProperties;setTime(J)V"))
    //private void injected(CallbackInfo ci) {
        //DEBUG:
        //if(dimension.getType()==0) System.out.println("Total ServerWorld/World time:"+this.levelProperties.getTime());
        //if(dimension.getType()==0) System.out.println("Total Overworld time:"+levelProperties.getTime());
        //if(dimension.getType()==-1) System.out.println("Total Nether time:"+levelProperties.getTime());
        //if(dimension.getType()==1) System.out.println("Total The End time:"+levelProperties.getTime());
    //}


}
