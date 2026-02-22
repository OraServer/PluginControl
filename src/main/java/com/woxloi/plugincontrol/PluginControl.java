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

    /* ==============================
       起動前に無効化（重要）
       ============================== */
    @Override
    public void onLoad() {

        saveDefaultConfig();

        List<String> disabledList = getConfig().getStringList("auto-disabled");

        for (String name : disabledList) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

            if (plugin != null) {
                Bukkit.getPluginManager().disablePlugin(plugin);
                getLogger().info(name + " を起動時に無効化しました");
            }
        }
    }

    /* ==============================
       有効化処理
       ============================== */
    @Override
    public void onEnable() {

        instance = this;

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