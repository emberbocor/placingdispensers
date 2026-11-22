package io.github.emberbocor.placingdispensers;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(PlacingDispensers.MODID)
public class PlacingDispensers {
    public static final String MODID = "placingdispensers";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PlacingDispensers(IEventBus modEventBus, ModContainer modContainer) {
    }
}
