package grey.future.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static SoundEvent TALL_REALM_MUSIC;

    public static void register() {
        TALL_REALM_MUSIC = registerSound("music.tall_realm");
    }

    private static SoundEvent registerSound(String name) {
        Identifier id = Identifier.of("grey-future", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}