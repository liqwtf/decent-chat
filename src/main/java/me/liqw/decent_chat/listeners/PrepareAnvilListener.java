package me.liqw.decent_chat.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.liqw.decent_chat.utils.Components.mm;

public class PrepareAnvilListener implements Listener {
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory();
        ItemStack firstItem = anvilInventory.getItem(0);
        if (firstItem == null)
            return;

        String renameText = event.getView().getRenameText();
        if (renameText == null || renameText.isEmpty())
            return;

        ItemStack resultItem = firstItem.clone();
        ItemMeta meta = resultItem.getItemMeta();

        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(resultItem.getType());
            if (meta == null) {
                event.setResult(null);
                return;
            }
        }

        meta.displayName(mm("<!i>" + renameText));
        resultItem.setItemMeta(meta);
        event.setResult(resultItem);
    }
}
