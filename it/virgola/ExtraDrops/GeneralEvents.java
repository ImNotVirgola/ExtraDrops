package it.virgola.ExtraDrops;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class GeneralEvents implements Listener {

    public static boolean enabled = false;

    public HashMap<UUID, HashMap<EntityType, Integer>> kills_counter = new HashMap<>();

    @EventHandler
    public void onKill(EntityDeathEvent e)
    {
        if(enabled) {
            Player p = e.getEntity().getKiller();
            Entity monster = e.getEntity();
            if (p != null) {
                if (Main.instance.getConfig().get("Monsters." + monster.getType().toString()) != null) {
                    if (!kills_counter.containsKey(p.getUniqueId())) {
                        kills_counter.put(p.getUniqueId(), new HashMap<>());
                    }

                    if (!kills_counter.get(p.getUniqueId()).containsKey(monster.getType())) {
                        kills_counter.get(p.getUniqueId()).put(monster.getType(), 0);
                    }

                    kills_counter.get(p.getUniqueId()).put(monster.getType(), kills_counter.get(p.getUniqueId()).get(monster.getType()) + 1);
                    if (kills_counter.get(p.getUniqueId()).get(monster.getType()) >= Main.instance.getConfig().getInt("Options.mobs-kill-drop-number")) {
                        List<ItemStack> items = (List<ItemStack>) Main.instance.getConfig().getList("Monsters." + monster.getType().toString() + ".items");
                        e.getDrops().add(items.get(new Random().nextInt(items.size())));
                        kills_counter.get(p.getUniqueId()).put(monster.getType(), 0);
                    }
                }
            }
        }
    }
}
