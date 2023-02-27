package net.fabricmc.legacyigtfix.mixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin{

    //@Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V"))
    //private void injected(CallbackInfo ci) {
    //}

    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V"))
    public void injected(PlayerEntity instance, Stat stat) {
    //removes incrementStat
    }



}
