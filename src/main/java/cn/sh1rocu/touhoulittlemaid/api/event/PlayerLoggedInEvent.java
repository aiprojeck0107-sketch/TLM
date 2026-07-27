package cn.sh1rocu.touhoulittlemaid.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public class PlayerLoggedInEvent {
    private final Player player;

    public PlayerLoggedInEvent(Player player) {
        this.player = player;
    }

    public Player getEntity() {
        return player;
    }

    public static final Event<Callback> CALLBACK = EventFactory.createArrayBacked(Callback.class, callbacks -> event -> {
        for (final Callback callback : callbacks)
            callback.post(event);
    });

    public interface Callback {
        void post(PlayerLoggedInEvent event);
    }
}