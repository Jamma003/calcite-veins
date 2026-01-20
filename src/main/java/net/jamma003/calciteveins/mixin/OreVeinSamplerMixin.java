package net.jamma003.calciteveins.mixin;

import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.world.gen.OreVeinSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OreVeinSampler.class)
abstract class OreVeinSamplerMixin {
    @Inject(
            method = "create",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void disableVanillaVeins(
            DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap, RandomSplitter randomDeriver, CallbackInfoReturnable<ChunkNoiseSampler.BlockStateSampler> cir
    ) {
        cir.setReturnValue(pos -> null);
    }
}