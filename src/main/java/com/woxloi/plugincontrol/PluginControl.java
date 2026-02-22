package com.woxloi.plugincontrol;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginControl extends JavaPlugin {

    private static PluginControl instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        for (String name : getConfig().getStringList("auto-disabled")) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

            if (plugin != null && plugin.isEnabled()) {
                Bukkit.getPluginManager().disablePlugin(plugin);
                getLogger().info(name + " を自動無効化しました");
            }
        }

        getServer().getPluginManager().registerEvents(
                new DisabledCommandListener(),
                this
        );

        PluginCommand command = getCommand("plugincontrol");
        if (command != null) {
            command.setExecutor(new PluginControlCommand());
            command.setTabCompleter(new PluginControlCommand.PluginControlTab());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("PluginControl disabled.");
    }

    public static PluginControl getInstance() {
        return instance;
    }
}