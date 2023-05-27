package me.darksoul.darksmodifiedwoodchopping;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;

public class DarksModifiedWoodChopping extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("DarksModifiedWoodChopping has been enabled.");

        ItemStack plant_fiber = new ItemStack(Material.STRING, 1);

        // Set the display name of the custom item
        ItemMeta plant_fiberItemMeta = plant_fiber.getItemMeta();
        plant_fiberItemMeta.setDisplayName("Plant Fiber");
        plant_fiber.setItemMeta(plant_fiberItemMeta);

        ItemStack flintAxe = new ItemStack(Material.WOODEN_AXE, 1);

        // Set the durability of the custom axe to half of its maximum durability
        ItemMeta itemMeta = flintAxe.getItemMeta();
        if (itemMeta instanceof Damageable) {
            Damageable damageable = (Damageable) itemMeta;
            damageable.setDamage(flintAxe.getType().getMaxDurability() / 2);
            flintAxe.setItemMeta(itemMeta);
        }
        ShapedRecipe flintAxeRecipe = new ShapedRecipe(flintAxe);

        // Set the shape of the recipe
        flintAxeRecipe.shape("TP", "S ", " ");

        // Define the ingredients for the recipe
        flintAxeRecipe.setIngredient('P', Material.FLINT);
        flintAxeRecipe.setIngredient('T', plant_fiber);
        flintAxeRecipe.setIngredient('S', Material.STICK);

        // Register the recipe
        getServer().addRecipe(flintAxeRecipe);

        ShapedRecipe plantFiberRecipe = new ShapedRecipe(plant_fiber);

        // Set the shape of the recipe
        plantFiberRecipe.shape("RR", "R ", " ");

        // Define the ingredients for the recipe
        plantFiberRecipe.setIngredient('R', Material.WHEAT_SEEDS);
        // Register the recipe
        getServer().addRecipe(plantFiberRecipe);
    }

    @Override
    public void onDisable() {
        getLogger().info("DarksModifiedWoodChopping has been disabled.");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material blockType = event.getBlock().getType();
        Material toolType = event.getPlayer().getInventory().getItemInMainHand().getType();

        if ((blockType == Material.OAK_LOG || blockType == Material.BIRCH_LOG ||
                blockType == Material.SPRUCE_LOG || blockType == Material.JUNGLE_LOG ||
                blockType == Material.ACACIA_LOG || blockType == Material.DARK_OAK_LOG) && !toolType.name().contains("_AXE")) {
            // Player tries to break logs without an axe
            event.setCancelled(true);
            event.getPlayer().damage(0.1);
            event.getPlayer().sendMessage(ChatColor.RED + "You can't break logs with that tool!");
        } else if (isStoneRelatedBlock(blockType) && !toolType.name().contains("_PICKAXE")) {
            // Player tries to break stone-related blocks without a pickaxe
            event.setCancelled(true);
            event.getPlayer().damage(0.1);
            event.getPlayer().sendMessage(ChatColor.RED + "You can't break stone-related blocks with that tool!");
        }
    }

    private boolean isStoneRelatedBlock(Material material) {
        return material.name().contains("STONE") || material.name().contains("COBBLESTONE")
                || material.name().contains("GRANITE") || material.name().contains("ANDESITE")
                || material.name().contains("DIORITE") || material.name().contains("BRICK")
                || material.name().equals("QUARTZ_BLOCK") || material.name().equals("QUARTZ_PILLAR");
    }
}
