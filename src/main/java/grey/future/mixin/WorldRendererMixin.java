package grey.future.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.texture.AbstractTexture;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    private static final Identifier SKY_TEXTURE = Identifier.of("grey-future", "textures/sky/sky.png");

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void renderGreySky(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null &&
                client.world.getRegistryKey().getValue().equals(Identifier.of("grey-future", "tall_realm"))) {
            ci.cancel();

            // Get texture
            AbstractTexture texture = client.getTextureManager().getTexture(SKY_TEXTURE);

            if (texture != null) {
                System.out.println("AbstractTexture methods:");
                for (var method : texture.getClass().getDeclaredMethods()) {
                    if (method.getParameterCount() == 0) {
                        System.out.println("  - " + method.getName());
                    }
                }
            }
        }
    }
}