package io.github.emberbocor.placingdispensers.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.toml.TomlFormat;

import io.github.emberbocor.placingdispensers.PlacingDispensers;

final class ServerConfigFile {
    static final String FILE_NAME = PlacingDispensers.MODID + "-server.toml";

    private static final String DEFAULT_TEXT = """
            # Placing Dispensers server config.
            # Created on the first server or world start.
            # Restart the server or reopen the singleplayer world to apply changes.

            # Let dispensers place block items in front of them. False restores vanilla dispensing.
            enabled = true

            # Blocks that dispensers never place; those items are thrown out as in vanilla.
            # Block IDs ("minecraft:torch") and block tags ("#minecraft:saplings").
            # An item matches by the block it places, so wheat seeds are "minecraft:wheat".
            excludedBlocks = []
            """;

    private ServerConfigFile() {
    }

    static Optional<UnmodifiableConfig> load(Path file) {
        if (!Files.exists(file)) {
            try {
                Files.createDirectories(file.getParent());
                Files.writeString(file, DEFAULT_TEXT, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
                PlacingDispensers.LOGGER.info("Created {}", file);
            } catch (IOException e) {
                PlacingDispensers.LOGGER.warn("Could not create {}, using the default settings: {}", file, e.getMessage());
            }
            return parse(file, DEFAULT_TEXT);
        }

        try {
            String text = Files.readString(file, StandardCharsets.UTF_8);
            return parse(file, text.startsWith("﻿") ? text.substring(1) : text);
        } catch (IOException e) {
            PlacingDispensers.LOGGER.warn("Could not read {}, using the default settings; file left unchanged: {}", file, e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<UnmodifiableConfig> parse(Path file, String text) {
        try {
            return Optional.of(TomlFormat.instance().createParser().parse(text));
        } catch (RuntimeException e) {
            PlacingDispensers.LOGGER.warn("Could not parse {}, using the default settings; file left unchanged: {}", file, e.getMessage());
            return Optional.empty();
        }
    }
}
