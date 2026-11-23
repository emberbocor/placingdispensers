package io.github.emberbocor.placingdispensers;

import io.github.emberbocor.placingdispensers.config.ServerConfig;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(PlacingDispensers.MODID)
public class PlacingDispensersForge {
    public PlacingDispensersForge() {
        ServerAboutToStartEvent.BUS.addListener(event -> ServerConfig.load());
    }
}
