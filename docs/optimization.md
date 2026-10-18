# Optimization and compatibility guide

## 1. Measure first

Use `/leaflite status` for a quick heap/chunk/entity snapshot. For actual tick cost, install a current version of spark compatible with your server and capture a profile while the lag is happening. Record MSPT, CPU usage, GC pauses, disk latency and loaded chunk counts. A single heap snapshot cannot diagnose lag.

## 2. Memory reality

Do not promise 50 players on 512 MB. Heap allocation is not the same as total process memory: metaspace, code cache, thread stacks, native buffers and the OS also need RAM. If the host kills the process for exceeding its container limit, lowering `-Xmx` may avoid an OOM kill but can cause frequent garbage collection and worse pauses. Use a realistic allocation and leave headroom for the OS/container.

## 3. Chunk generation

- Choose the playable radius and set the world border first.
- Run Chunky (or another maintained, version-compatible pregenerator) before launch or during quiet hours.
- Pregenerate the overworld and other worlds separately where supported.
- Monitor disk use and back up the world. Do not pregenerate an unlimited area.
- Pregeneration reduces exploration-time generation spikes, but loaded chunks still consume RAM and tick time.

## 4. Keep gameplay vanilla

For vanilla-like behavior, do not enable AI simplification, reduced mob caps, aggressive despawn settings, altered entity activation ranges, non-vanilla redstone engines, or shortened tick intervals. These options can change farms, combat, mob behavior or contraptions.

Inspect the generated configuration from your exact Leaf build for a documented vanilla redstone setting. Config names differ across builds; use the version's own comments/docs and do not invent or copy unverified keys. After switching, test observers, pistons, repeaters, comparators, hoppers, clocks and common farms.

## 5. Conservative configuration

`config-examples/server.properties` suggests view distance 6 and simulation distance 4 as a workload-reduction starting point. These values affect visibility and simulation, so use the highest values your hardware can sustain. Change one setting at a time and benchmark.

Keep Leaf/Paper configuration defaults for mob caps, AI, redstone and tick behavior if gameplay must remain unchanged. Remove unnecessary plugins and update plugins that show up in profiles. Avoid running multiple plugins that do the same job.

## 6. JVM and process

- Use a supported Java 21 build appropriate for your server distribution.
- Start with the hosting provider's safe launch command. Set `-Xms` and `-Xmx` within the real container limit and leave native-memory headroom.
- Do not add `System.gc()` calls or random JVM flag bundles. This plugin never triggers GC.
- Keep server files on fast storage and monitor disk capacity; chunk generation and saving can be I/O-bound.

## 7. Validate and roll back

Back up configs and worlds. Test with representative players and redstone/mob farms. Compare identical workloads before and after each change. If mechanics change or timings get worse, revert the last setting. A Paper/Leaf configuration cannot guarantee zero lag spikes because hardware, plugins, world complexity and player behavior all matter.
