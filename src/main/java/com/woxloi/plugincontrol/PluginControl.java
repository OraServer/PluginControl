package com.woxloi.plugincontrol;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class PluginControl extends JavaPlugin {

    private static PluginControl instance;

    public static PluginControl getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        // ⭐ 1tick遅延で安全に無効化
        Bukkit.getScheduler().runTask(this, () -> {

            List<String> disabledList = getConfig().getStringList("auto-disabled");

            for (String name : disabledList) {

                Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

                if (plugin != null && plugin.isEnabled()) {

                    Bukkit.getPluginManager().disablePlugin(plugin);
                    getLogger().info(name + " を起動時に無効化しました");
                }
            }
        });

        getServer().getPluginManager().registerEvents(
                new DisabledCommandListener(),
                this
        );

        PluginCommand command = getCommand("plugincontrol");
        if (command != null) {
            command.setExecutor(new PluginControlCommand());
            command.setTabCompleter(new PluginControlCommand.PluginControlCommandTab());
        }

        getLogger().info("PluginControl enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PluginControl disabled.");
    }
}