package dev.dewy.dqs.protocol.game.entity.player;

import dev.dewy.dqs.protocol.game.world.notify.ClientNotificationValue;

public enum GameMode implements ClientNotificationValue
{
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR
}
