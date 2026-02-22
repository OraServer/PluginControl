package com.woxloi.plugincontrol;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class DisabledCommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        String message = event.getMessage();
        if (!message.startsWith("/")) return;

        String label = message.substring(1).split(" ")[0];

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {

            if (plugin.isEnabled()) continue;

            Map<String, Map<String, Object>> commands =
                    plugin.getDescription().getCommands();

            if (commands == null) continue;

            for (Map.Entry<String, Map<String, Object>> entry : commands.entrySet()) {

                String commandName = entry.getKey();

                // コマンド名一致
                if (commandName.equalsIgnoreCase(label)) {
                    block(event, plugin.getName());
                    return;
                }

                // エイリアス確認
                Object aliasesObj = entry.getValue().get("aliases");
                if (aliasesObj instanceof List<?> aliases) {
                    for (Object alias : aliases) {
                        if (alias.toString().equalsIgnoreCase(label)) {
                            block(event, plugin.getName());
                            return;
                        }
                    }
                }
            }
        }
    }

    private void block(PlayerCommandPreprocessEvent event, String pluginName) {
        event.setCancelled(true);
        event.getPlayer().sendMessage("§c" + pluginName + " は現在無効です。");
    }
}