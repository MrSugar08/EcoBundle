package org.info_0.ecobundle.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MaterialCalculator {
	public static int emptySpace(Player player){
        return (emptySlots(player)*64)+(goldSlots(player)*64-goldAmount(player));
	}
	
	private static int emptySlots(Player player) {
		int x = 0;
        for(int i = 0;i<=35;i++){
			ItemStack item = player.getInventory().getItem(i);
            if (item != null && !(item.getType().isAir())) continue;
            x++;
        }
		return x;
	}
	public static int goldAmount(Player player) {
		int x = 0;
		for(int i = 0;i<=35;i++){
			ItemStack item = player.getInventory().getItem(i);
            if(item == null || item.getType() != Material.GOLD_INGOT) continue;
			x += item.getAmount();
		}
		return x;
	}
	private static int goldSlots(Player player) {
		int x = 0;
		for(int i = 0;i<=35;i++){
			ItemStack item = player.getInventory().getItem(i);
			if(item.getType() != Material.GOLD_INGOT) continue;
			x++;
		}
		return x;
	}
	
}
