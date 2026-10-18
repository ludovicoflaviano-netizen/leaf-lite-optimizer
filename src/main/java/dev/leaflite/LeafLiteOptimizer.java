package dev.leaflite;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class LeafLiteOptimizer extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Enabled. This plugin reports metrics only; it does not change gameplay mechanics.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("leaflite")) return false;
        if (args.length > 0 && !args[0].equalsIgnoreCase("status")) {
            sender.sendMessage("Usage: /leaflite [status]");
            return true;
        }
        report(sender);
        return true;
    }

    private void report(CommandSender sender) {
        Runtime runtime = Runtime.getRuntime();
        long max = runtime.maxMemory();
        long used = runtime.totalMemory() - runtime.freeMemory();
        long committed = runtime.totalMemory();
        int loadedChunks = 0;
        long entities = 0;
        for (World world : Bukkit.getWorlds()) {
            Chunk[] chunks = world.getLoadedChunks();
            loadedChunks += chunks.length;
            entities += world.getEntityCount();
        }
        sender.sendMessage("§6[LeafLite] Runtime snapshot (not a benchmark)");
        sender.sendMessage(String.format(Locale.ROOT,
                "§eHeap: §f%s used / %s committed / %s max",
                mib(used), mib(committed), mib(max)));
        sender.sendMessage("§ePlayers: §f" + Bukkit.getOnlinePlayers().size()
                + "§7/" + Bukkit.getMaxPlayers()
                + " §eLoaded chunks: §f" + loadedChunks
                + " §eEntities: §f" + entities);
        sender.sendMessage("§7Worlds: " + Bukkit.getWorlds().size()
                + ". Check CPU, MSPT and disk I/O with a profiler for real bottlenecks.");
        sender.sendMessage("§7This command does not force GC or modify server settings.");
    }

    private String mib(long bytes) {
        return String.format(Locale.ROOT, "%.0f MiB", bytes / 1024.0 / 1024.0);
    }
}
