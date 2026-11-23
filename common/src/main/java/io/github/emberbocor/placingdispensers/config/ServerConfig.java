package io.github.emberbocor.placingdispensers.config;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import io.github.emberbocor.placingdispensers.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ServerConfig {
    static final Settings DEFAULTS = new Settings(true, Set.of(), List.of());

    private static volatile Settings settings = DEFAULTS;

    private ServerConfig() {
    }

    public static void load() {
        Path file = Services.PLATFORM.getConfigDirectory().resolve(ServerConfigFile.FILE_NAME);
        settings = new ServerConfigReader(file).read(ServerConfigFile.load(file).orElse(null));
    }

    public static Settings settings() {
        return settings;
    }

    public record Settings(boolean enabled, Set<Identifier> excludedIds, List<TagKey<Block>> excludedTags) {
        public boolean excludes(Block block) {
            if (excludedIds.contains(BuiltInRegistries.BLOCK.getKey(block))) {
                return true;
            }
            for (TagKey<Block> tag : excludedTags) {
                if (block.defaultBlockState().is(tag)) {
                    return true;
                }
            }
            return false;
        }
    }
}
