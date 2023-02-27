package net.fabricmc.legacyigtfix.mixin;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntity {

    //@Shadow @Final private ServerStatHandler statHandler;

    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    public boolean isSpectator() {
        return false;
    }


    /**
     * Added incrementStat to ServerPlayerEntity tick()
     */
    @Inject(method = "tick()V", at = @At(value = "HEAD"))
    private void injected(CallbackInfo ci) {
        if (!this.world.isClient) {
            this.incrementStat(Stats.MINUTES_PLAYED);
        }

        //DEBUG:
        //int playOneMinute = this.statHandler.getStatLevel(Stats.MINUTES_PLAYED);
        //System.out.println("Total Player Time:"+playOneMinute);
    }

}
