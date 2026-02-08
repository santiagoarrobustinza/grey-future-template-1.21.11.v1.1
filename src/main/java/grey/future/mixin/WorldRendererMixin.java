package grey.future.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    private static final Identifier SKY_TEXTURE = Identifier.of("grey-future", "textures/sky/sky.png");

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void renderGreySky(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null &&
                client.world.getRegistryKey().getValue().equals(Identifier.of("grey-future", "tall_realm"))) {
            ci.cancel();

            // Load texture
            client.getTextureManager().getTexture(SKY_TEXTURE);

            // Render a full-screen quad
            Tessellator tessellator = Tessellator.getInstance();
            var buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            MatrixStack matrices = new MatrixStack();
            Matrix4f matrix = matrices.peek().getPositionMatrix();

            // Draw fullscreen sky quad - vertex 1
            buffer.vertex(matrix, -100.0f, 100.0f, -100.0f);
            buffer.texture(0.0f, 0.0f);
            buffer.color(255, 255, 255, 255);
            buffer.end();

            // Vertex 2
            buffer.vertex(matrix, 100.0f, 100.0f, -100.0f);
            buffer.texture(1.0f, 0.0f);
            buffer.color(255, 255, 255, 255);
            buffer.end();

            // Vertex 3
            buffer.vertex(matrix, 100.0f, -100.0f, -100.0f);
            buffer.texture(1.0f, 1.0f);
            buffer.color(255, 255, 255, 255);
            buffer.end();

            // Vertex 4
            buffer.vertex(matrix, -100.0f, -100.0f, -100.0f);
            buffer.texture(0.0f, 1.0f);
            buffer.color(255, 255, 255, 255);
            buffer.end();

            buffer.build().draw();
        }
    }
}