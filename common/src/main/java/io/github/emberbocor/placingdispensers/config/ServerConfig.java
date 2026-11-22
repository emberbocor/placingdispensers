package io.github.emberbocor.placingdispensers.config;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import io.github.emberbocor.placingdispensers.platform.Services;
import net.minecraft.resources.ResourceLocation;
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

    public record Settings(boolean enabled, Set<ResourceLocation> excludedIds, List<TagKey<Block>> excludedTags) {
        public boolean excludes(Block block) {
            // Forge 1.20.1 deprecates BuiltInRegistries.BLOCK; the block's holder carries the same ID.
            if (block.defaultBlockState().getBlockHolder().unwrapKey().map(key -> excludedIds.contains(key.location())).orElse(false)) {
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
