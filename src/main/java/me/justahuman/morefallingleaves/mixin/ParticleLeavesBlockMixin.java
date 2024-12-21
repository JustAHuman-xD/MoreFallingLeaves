package me.justahuman.morefallingleaves.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.justahuman.morefallingleaves.MoreFallingLeaves;
import net.minecraft.block.ParticleLeavesBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ParticleLeavesBlock.class)
public class ParticleLeavesBlockMixin {
    @Redirect(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    public int onRandomDisplay(Random random, int chance, @Local(argsOnly = true) BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        double option = MoreFallingLeaves.guaranteedLeafDistance;
        if (option == 0) {
            return random.nextInt(chance);
        }

        Camera camera = client.gameRenderer.getCamera();
        double distance = camera.getBlockPos().getSquaredDistance(pos.getX(), pos.getY(), pos.getZ());
        double guaranteedDistance = Math.pow(option, 2);
        chance = (int) (chance * (1 - distance / guaranteedDistance));
        return chance <= 0 ? 0 : random.nextInt(chance);
    }
}
