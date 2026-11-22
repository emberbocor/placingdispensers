# Placing Dispensers

Let vanilla dispensers place blocks, crops and saplings, the way a player places blocks.

With Placing Dispensers, a dispenser holding a block item puts that block into the space right in front of it instead of throwing the item out. Seeds and other crop items plant their crop, saplings and flowers are planted, and any other block is placed. Supply the dispenser, then send a redstone pulse.

Available for NeoForge, Forge and Fabric.

## Features

- **Place any block item.** Full blocks, slabs, stairs, torches, doors, beds, rails, carpets and every other item that places a block.
- **Plant crops and saplings.** Wheat seeds, carrots, potatoes, beetroot seeds, saplings, flowers and other plants are placed where they can grow.
- **Keep what cannot be placed.** If the block cannot go in front, the item stays in the dispenser instead of being thrown out.
- **Vanilla handlers stay.** Items that already do something special in a dispenser keep doing it.
- **Exclusions.** A small server config can turn chosen blocks or block tags back to vanilla dispensing.

## How it works

Point a **vanilla dispenser** at the space where the block should go and give it a redstone pulse. All six facing directions work. As in vanilla, the dispenser picks one non-empty slot at random, and each pulse uses at most one item.

- **Placing.** The block is placed in the space directly in front of the dispenser, the same way a vanilla dispenser places a shulker box. One item is used, you hear the block's normal placing sound and the dispenser plays its usual click and smoke.
- **When it is placed.** The space must hold air, water, lava or another replaceable block (such as short grass or a single snow layer), no entity may be in the way, and the block must be able to stay there: crops need farmland and light, saplings and flowers need soil, torches need something to stand on or hang from. Blocks that also need room next to them, such as doors, beds and tall flowers, need that room too.
- **When it is not placed.** The item stays in the dispenser and the dispenser plays its failure click. This includes seeds aimed at ordinary dirt, a sapling aimed at stone, or a space that is already filled. If the block in front is the same block the item would place (a slab in front of the same slab, snow in front of snow layers, scaffolding in front of scaffolding), nothing is placed either.
- **Facing.** Blocks are placed as if a player looked in the dispenser's direction while looking straight down. Blocks with a horizontal facing follow the dispenser: furnaces and similar blocks face the dispenser, stairs rise away from it, beds and doors point along it. A dispenser facing up or down places them facing north. Blocks that can face up and down, such as pistons, observers and barrels, are always placed vertically. Logs and other pillars stand upright when there is a block below the target space, and lie along the dispenser's direction when there is not.
- **Block data.** Names and other data stored on the item are applied as when a player without operator rights places the block.

What stays vanilla:

- **Droppers** keep their normal transfer and ejection behavior.
- **Items with their own dispenser action** keep it: shulker boxes, carved pumpkins, skulls, TNT, chests (for donkeys and llamas), buckets, bone meal, glowstone for respawn anchors and so on.
- **Items that do not place a block** (spawn eggs, tools, food) are thrown out as before.
- **Command blocks, structure blocks and jigsaw blocks** are thrown out as before, since a dispenser has no operator rights.
- **Excluded blocks** from the config are thrown out as before.

## Requirements

| | NeoForge | Forge | Fabric |
|---|---|---|---|
| Minecraft | 1.21.1 | 1.21.1 | 1.21.1 |
| Java | 21 | 21 | 21 |
| Loader | NeoForge 21.1.1 or newer | Forge 52.0.0 or newer | Fabric Loader 0.15.11 or newer |
| Other | | | Fabric API 0.102.0+1.21.1 or newer for Minecraft 1.21.1 |
| Jar | `placingdispensers-neoforge-1.21.1-<version>.jar` | `placingdispensers-forge-1.21.1-<version>.jar` | `placingdispensers-fabric-1.21.1-<version>.jar` |

On Minecraft 1.21.1, NeoForge and Forge are separate loaders that do not run each other's mods, so each has its own jar. Pick the jar that matches your game's loader.

### Client and server

Install the matching jar on the **server**. It adds no items, blocks, entities, sounds, network messages or registries, so clients can join without Placing Dispensers:

- **NeoForge:** vanilla clients and NeoForge clients without the mod can join.
- **Forge:** vanilla clients and Forge clients without the mod can join. The mod is marked as server-only, so a Forge client without it still shows the server as compatible in the server list.
- **Fabric:** vanilla clients and Fabric clients without the mod can join. Fabric API is required on a Fabric server.

In singleplayer, install it in your own game; the built-in server runs it. Fabric singleplayer also needs Fabric API.

## Configuration

`config/placingdispensers-server.toml` is created with comments on the first server or world start:

```toml
enabled = true
excludedBlocks = []
```

- `enabled = false` restores all vanilla dispenser behavior.
- `excludedBlocks` lists blocks that dispensers never place. Use block IDs (`"minecraft:torch"`) and block tags (`"#minecraft:saplings"`), for example `excludedBlocks = ["minecraft:torch", "#minecraft:saplings"]`. An item matches by the block it places, so wheat seeds are excluded with `"minecraft:wheat"` and carrots with `"minecraft:carrots"`. Excluded items are thrown out as in vanilla.
- Tags are checked each time a dispenser fires, so data pack changes to a tag apply after `/reload`.

Restart the server or reopen the singleplayer world after editing. There is no config screen or hot reload.

Missing keys use defaults. An invalid `enabled` value uses the default with a warning; an `excludedBlocks` entry that is not a block ID or tag is ignored with a warning. Malformed or unreadable files use defaults and are left unchanged. Existing files are never rewritten to add keys. Config is server-local and is not synchronized to clients.

## Saved data and removing the mod

Placed blocks are ordinary vanilla blocks, and items left in a dispenser are ordinary dispenser contents. No custom world data, block or item is added; config is separate from world data.

Removing the mod restores vanilla dispensing: block items are thrown out again. Blocks that were already placed stay.

## Compatibility

- Targets Minecraft 1.21.1. Use one matching loader jar at a time.
- **Ranching Dispensers:** placing comes first. A carrot or seed aimed at farmland is planted; if it cannot be placed, Ranching Dispensers can still use it to feed an animal in front. If neither mod uses it, the item stays in the dispenser. The two mods do not depend on each other.
- Block items added by other mods are placed like vanilla ones unless that mod registers its own dispenser action for them.
- Mods that replace dispenser behaviors or change the default dispenser action may take priority or conflict.

## Building from source

Use JDK 21 and the included Gradle wrapper:

```
./gradlew build
```

Release jars are written to `neoforge/build/libs/`, `forge/build/libs/` and `fabric/build/libs/`. Use Fabric's remapped jar from `fabric/build/libs/`; development jars are not release artifacts.

Development profiles use `runs/1.21.1/neoforge` for both NeoForge sides, and separate `runs/1.21.1/forge-client`, `forge-server`, `fabric-client` and `fabric-server` directories. These directories are ignored by Git.

## License

MIT, copyright emberbocor. See [LICENSE](LICENSE).

The original NeoForged template notice is retained in [TEMPLATE_LICENSE.txt](TEMPLATE_LICENSE.txt).

The Fabric jar bundles unmodified NightConfig core and toml 3.8.0 under LGPL-3.0, with notice, source links and LGPL/GPL texts in `META-INF/`. NeoForge and Forge use their loader-provided NightConfig libraries.
