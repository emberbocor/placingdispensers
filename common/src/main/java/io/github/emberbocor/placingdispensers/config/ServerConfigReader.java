package io.github.emberbocor.placingdispensers.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.helpers.MessageFormatter;

import com.electronwill.nightconfig.core.UnmodifiableConfig;

import io.github.emberbocor.placingdispensers.PlacingDispensers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

final class ServerConfigReader {
    private static final Set<String> KEYS = Set.of("enabled", "excludedBlocks");

    private final Path file;

    ServerConfigReader(Path file) {
        this.file = file;
    }

    ServerConfig.Settings read(UnmodifiableConfig config) {
        ServerConfig.Settings defaults = ServerConfig.DEFAULTS;
        if (config == null) {
            return defaults;
        }
        for (UnmodifiableConfig.Entry entry : config.entrySet()) {
            if (!KEYS.contains(entry.getKey())) {
                warn("unknown option '{}', ignored", entry.getKey());
            }
        }

        Set<ResourceLocation> ids = new LinkedHashSet<>();
        List<TagKey<Block>> tags = new ArrayList<>();
        Object excluded = config.get("excludedBlocks");
        if (excluded instanceof List<?> entries) {
            for (Object entry : entries) {
                addExclusion(entry, ids, tags);
            }
        } else if (excluded != null) {
            warn("excludedBlocks must be a list of block IDs and #tags, got '{}'; using []", excluded);
        }
        return new ServerConfig.Settings(enabled(config, defaults.enabled()), Set.copyOf(ids), List.copyOf(tags));
    }

    private boolean enabled(UnmodifiableConfig config, boolean fallback) {
        Object value = config.get("enabled");
        if (value == null) {
            return fallback;
        }
        if (value instanceof Boolean enabled) {
            return enabled;
        }
        warn("enabled must be true or false, got '{}'; using {}", value, fallback);
        return fallback;
    }

    private void addExclusion(Object entry, Set<ResourceLocation> ids, List<TagKey<Block>> tags) {
        if (entry instanceof String text) {
            boolean tag = text.startsWith("#");
            String name = tag ? text.substring(1) : text;
            ResourceLocation id = name.isBlank() ? null : ResourceLocation.tryParse(name);
            if (id != null) {
                if (tag) {
                    TagKey<Block> key = TagKey.create(Registries.BLOCK, id);
                    if (!tags.contains(key)) {
                        tags.add(key);
                    }
                } else {
                    ids.add(id);
                }
                return;
            }
        }
        warn("excludedBlocks entry '{}' is not a block ID or #tag, ignored", entry);
    }

    private void warn(String message, Object... args) {
        PlacingDispensers.LOGGER.warn("{}: {}", file, MessageFormatter.arrayFormat(message, args).getMessage());
    }
}
