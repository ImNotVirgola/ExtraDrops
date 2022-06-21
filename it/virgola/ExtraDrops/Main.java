package it.virgola.ExtraDrops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    public static Main instance;

    public void onEnable() {
        instance = this;
        GeneralEvents.enabled = true;
        Config();
        Bukkit.getPluginManager().registerEvents(new GeneralEvents(),this);
        getCommand("extradrops").setExecutor(this);
    }
    public void onDisable() {
        GeneralEvents.enabled = false;
        getConfig().getString("Messages.plugin-disabled-message");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if(args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                if (sender.hasPermission("extradrops.help")) {
                    for (String str1 : getConfig().getStringList("Messages.help-message"))
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str1));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Errors.not-permission-command-message")));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("extradrops.reload")) {
                    reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.plugin-reloaded-message")));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Errors.not-permission-command-message")));
                }
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (sender.hasPermission("extradrops.enable")) {
                    GeneralEvents.enabled = true;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.drops-enabled")));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Errors.not-permission-command-message")));
                }
            } else if (args[0].equalsIgnoreCase("disable")) {
                if (sender.hasPermission("extradrops.disable")) {
                    GeneralEvents.enabled = false;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.drops-disabled")));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Errors.not-permission-command-message")));
                }
            } else if (args[0].equalsIgnoreCase("saveitem")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if(p.hasPermission("extradrops.saveitem")) {
                        if(args.length > 1) {
                            String mob_name = args[1].toUpperCase();

                            try {
                                EntityType.valueOf(mob_name);
                            } catch (IllegalArgumentException err) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&',getConfig().getString("Messages.Errors.not-monster-found-message")));
                                return false;
                            }

                            if (getConfig().get("Monsters." + mob_name + ".items") == null) {
                                getConfig().set("Monsters." + mob_name + ".items", new ArrayList<>());
                            }
                            List<ItemStack> itemList = (List<ItemStack>) getConfig().getList("Monsters." + mob_name + ".items");
                            itemList.add(p.getItemInHand());
                            getConfig().set("Monsters." + mob_name + ".items", itemList);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',getConfig().getString("Messages.item-saved-message")));
                            saveConfig();
                            reloadConfig();
                        }else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Errors.not-argument-typed-message")));
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Errors.not-permission-command-message")));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Errors.not-player-execution-message")));
                }
            } else {
                for (String str1 : getConfig().getStringList("Messages.help-message")) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str1));
            }
        }else {
            for (String str1 : getConfig().getStringList("Messages.help-message")) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str1));
        }
        return false;
    }
    public void Config() {

        ArrayList<String> help = new ArrayList<>();
        help.add("&6-----------------------Help------------------------");
        help.add("&e/extradrops help");
        help.add("&e/extradrops reload");
        help.add("&e/extradrops enable");
        help.add("&e/extradrops disable");
        help.add("&6---------------------------------------------------");

        getConfig().options().copyDefaults(true);

        ArrayList<ItemStack> itemList = new ArrayList<>();
        itemList.add(new ItemStack(Material.APPLE));
        itemList.add(new ItemStack(Material.GOLDEN_APPLE));

        getConfig().addDefault("Monsters.ZOMBIE.items", itemList);

        getConfig().addDefault("Messages.help-message", help);
        getConfig().addDefault("Messages.drops-enabled", "&6Extra&eDrops: &a&lDrops Abilitati");
        getConfig().addDefault("Messages.drops-disabled", "&6Extra&eDrops: &c&lDrops Disabilitati");
        getConfig().addDefault("Messages.item-saved-message", "&6Extra&eDrops: &a&lItem Salvato con successo");
        getConfig().addDefault("Messages.plugin-enabled-message", "&6Extra&eDrops: &a&lPlugin Abilitato");
        getConfig().addDefault("Messages.plugin-disabled-message", "&6Extra&eDrops: &c&lPlugin Disabilitato");
        getConfig().addDefault("Messages.plugin-reloaded-message", "&6Extra&eDrops: &a&lPlugin Ricaricato");

        getConfig().addDefault("Messages.Errors.not-permission-command-message", "&6Extra&eDrops: &c&lNon hai i permessi per eseguire questo comando");
        getConfig().addDefault("Messages.Errors.not-player-execution-message", "&6Extra&eDrops: &c&lDevi essere un player per eseguire questo comando");
        getConfig().addDefault("Messages.Errors.not-argument-typed-message", "&6Extra&eDrops: &c&lDevi inserire il nome di un mob a cui vuoi applicare l'item");
        getConfig().addDefault("Messages.Errors.not-monster-found-message", "&6Extra&eDrops: &c&lIl mob inserito non esiste");

        saveConfig();
        reloadConfig();
    }
}
