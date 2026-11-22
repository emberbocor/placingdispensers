package io.github.emberbocor.placingdispensers;

import io.github.emberbocor.placingdispensers.config.ServerConfig;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@Mod(PlacingDispensers.MODID)
public class PlacingDispensersNeoForge {
    public PlacingDispensersNeoForge() {
        NeoForge.EVENT_BUS.addListener((ServerAboutToStartEvent event) -> ServerConfig.load());
    }
}
