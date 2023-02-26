package net.fabricmc.legacyigtfix.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerMetadata;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.snooper.Snoopable;
import net.minecraft.util.snooper.Snooper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

import static net.minecraft.server.MinecraftServer.getTimeMillis;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {


    @Shadow
	private static final Logger LOGGER = LogManager.getLogger();
	@Shadow
	private static MinecraftServer instance;
	@Shadow
	private final Snooper snooper = new Snooper("server", (Snoopable) this, getTimeMillis());
	@Shadow
	public final Profiler profiler = new Profiler();
	@Shadow
	private final ServerMetadata serverMetadata = new ServerMetadata();
	@Shadow
	private final Random random = new Random();
	@Shadow
	private int ticks;
	@Shadow
	public final long[] lastTickLengths = new long[100];
	@Shadow
	private boolean profiling;
	@Shadow
	private PlayerManager playerManager;
	@Shadow
	private long lastPlayerSampleUpdate = 0L;

    @Shadow
	public void tick(){
	}

	@Shadow
	public int getMaxPlayerCount(){
		return 0;
	}
	@Shadow
	public int getCurrentPlayerCount(){
		return 0;
	}
	@Shadow
	protected void saveWorlds(boolean silent){
	}
	/**
	 * @reason Print
	 * @author Crafterdark
	 */
	@Overwrite
	public void setupWorld() {
		long l = System.nanoTime();
		++this.ticks;
		System.out.println("ServerWorld timer:"+this.ticks);
		if (this.profiling) {
			this.profiling = false;
			this.profiler.enabled = true;
			this.profiler.reset();
		}

		this.profiler.push("root");
		this.tick();
		if (l - this.lastPlayerSampleUpdate >= 5000000000L) {
			this.lastPlayerSampleUpdate = l;
			this.serverMetadata.setPlayers(new ServerMetadata.Players(this.getMaxPlayerCount(), this.getCurrentPlayerCount()));
			GameProfile[] gameProfiles = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
			int i = MathHelper.nextInt(this.random, 0, this.getCurrentPlayerCount() - gameProfiles.length);

			for(int j = 0; j < gameProfiles.length; ++j) {
				gameProfiles[j] = ((ServerPlayerEntity)this.playerManager.getPlayers().get(i + j)).getGameProfile();
			}

			Collections.shuffle(Arrays.asList(gameProfiles));
			this.serverMetadata.getPlayers().setSample(gameProfiles);
		}

		if (this.ticks % 900 == 0) {
			this.profiler.push("save");
			this.playerManager.saveAllPlayerData();
			this.saveWorlds(true);
			this.profiler.pop();
		}

		this.profiler.push("tallying");
		this.lastTickLengths[this.ticks % 100] = System.nanoTime() - l;
		this.profiler.pop();
		this.profiler.push("snooper");
		if (!this.snooper.isActive() && this.ticks > 100) {
			this.snooper.setActive();
		}

		if (this.ticks % 6000 == 0) {
			this.snooper.addCpuInfo();
		}

		this.profiler.pop();
		this.profiler.pop();
	}
}
