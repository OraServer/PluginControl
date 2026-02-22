package com.woxloi.plugincontrol;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class PluginControlCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("plugincontrol.admin")) {
            sender.sendMessage("§c権限がありません");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {

            case "enable" -> {
                if (args.length != 2) {
                    sender.sendMessage("§e/plugincontrol enable <plugin>");
                    return true;
                }
                enable(sender, args[1]);
            }

            case "disable" -> {
                if (args.length != 2) {
                    sender.sendMessage("§e/plugincontrol disable <plugin>");
                    return true;
                }
                disable(sender, args[1]);
            }

            case "list" -> list(sender);

            case "info" -> {
                if (args.length != 2) {
                    sender.sendMessage("§e/plugincontrol info <plugin>");
                    return true;
                }
                info(sender, args[1]);
            }

            case "reload" -> {
                PluginControl.getInstance().reloadConfig();
                sender.sendMessage("§aPluginControl config を再読み込みしました");
            }

            default -> sendHelp(sender);
        }

        return true;
    }

    /* ================= ENABLE ================= */

    private void enable(CommandSender sender, String name) {

        Plugin target = Bukkit.getPluginManager().getPlugin(name);

        if (target == null) {
            sender.sendMessage("§cプラグインが見つかりません");
            return;
        }

        if (target.equals(PluginControl.getInstance())) {
            sender.sendMessage("§cこのプラグインは操作できません");
            return;
        }

        if (target.isEnabled()) {
            sender.sendMessage("§cすでに有効です");
            return;
        }

        Bukkit.getScheduler().runTask(PluginControl.getInstance(), () -> {
            Bukkit.getPluginManager().enablePlugin(target);

            PluginControl plugin = PluginControl.getInstance();
            List<String> list = plugin.getConfig().getStringList("auto-disabled");
            list.remove(target.getName());
            plugin.getConfig().set("auto-disabled", list);
            plugin.saveConfig();

            sender.sendMessage("§a" + target.getName() + " を有効化しました");
        });
    }

    /* ================= DISABLE ================= */

    private void disable(CommandSender sender, String name) {

        Plugin target = Bukkit.getPluginManager().getPlugin(name);

        if (target == null) {
            sender.sendMessage("§cプラグインが見つかりません");
            return;
        }

        if (target.equals(PluginControl.getInstance())) {
            sender.sendMessage("§cこのプラグインは操作できません");
            return;
        }

        if (!target.isEnabled()) {
            sender.sendMessage("§cすでに無効です");
            return;
        }

        Bukkit.getScheduler().runTask(PluginControl.getInstance(), () -> {

            Bukkit.getPluginManager().disablePlugin(target);

            PluginControl plugin = PluginControl.getInstance();
            List<String> list = plugin.getConfig().getStringList("auto-disabled");

            if (!list.contains(target.getName())) {
                list.add(target.getName());
                plugin.getConfig().set("auto-disabled", list);
                plugin.saveConfig();
            }

            sender.sendMessage("§a" + target.getName() + " を無効化しました");
        });
    }

    /* ================= LIST ================= */

    private void list(CommandSender sender) {

        sender.sendMessage("§6=== Plugin List ===");

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            String status = plugin.isEnabled() ? "§a有効" : "§c無効";
            sender.sendMessage("§7- §f" + plugin.getName() + " §8[" + status + "§8]");
        }
    }

    /* ================= INFO ================= */

    private void info(CommandSender sender, String name) {

        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

        if (plugin == null) {
            sender.sendMessage("§cプラグインが見つかりません");
            return;
        }

        sender.sendMessage("§6=== Plugin Info ===");
        sender.sendMessage("§f名前: §e" + plugin.getName());
        sender.sendMessage("§fバージョン: §e" + plugin.getDescription().getVersion());
        sender.sendMessage("§fメイン: §e" + plugin.getDescription().getMain());
        sender.sendMessage("§f有効: §e" + plugin.isEnabled());
        sender.sendMessage("§f依存: §e" + plugin.getDescription().getDepend());
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6=== PluginControl ===");
        sender.sendMessage("§e/plugincontrol enable <plugin>");
        sender.sendMessage("§e/plugincontrol disable <plugin>");
        sender.sendMessage("§e/plugincontrol list");
        sender.sendMessage("§e/plugincontrol info <plugin>");
        sender.sendMessage("§e/plugincontrol reload");
    }

    /* ================= TAB ================= */

    public static class PluginControlCommandTab implements TabCompleter {

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

            if (args.length == 1) {
                return Arrays.asList("enable", "disable", "list", "info", "reload");
            }

            if (args.length == 2) {
                return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                        .map(Plugin::getName)
                        .collect(Collectors.toList());
            }

            return Collections.emptyList();
        }
    }
}