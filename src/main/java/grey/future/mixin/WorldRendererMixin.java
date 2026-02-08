package grey.future.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.lwjgl.opengl.GL13;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    private static final Identifier SKY_TEXTURE = Identifier.of("grey-future", "textures/sky/sky.png");

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void renderGreySky(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null &&
                client.world.getRegistryKey().getValue().equals(Identifier.of("grey-future", "tall_realm"))) {
            ci.cancel();

            try {
                // Bind texture
                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                var texture = client.getTextureManager().getTexture(SKY_TEXTURE);

                // Draw quad with tessellator
                Tessellator tessellator = Tessellator.getInstance();
                var buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

                buffer.vertex(-1.0f, 1.0f, -0.9f).texture(0.0f, 0.0f).color(255, 255, 255, 255);
                buffer.vertex(1.0f, 1.0f, -0.9f).texture(1.0f, 0.0f).color(255, 255, 255, 255);
                buffer.vertex(1.0f, -1.0f, -0.9f).texture(1.0f, 1.0f).color(255, 255, 255, 255);
                buffer.vertex(-1.0f, -1.0f, -0.9f).texture(0.0f, 1.0f).color(255, 255, 255, 255);

                buffer.end();

                System.out.println("Sky texture rendered");
            } catch (Exception e) {
                System.err.println("Error rendering custom sky:");
                e.printStackTrace();
            }
        }
    }
}