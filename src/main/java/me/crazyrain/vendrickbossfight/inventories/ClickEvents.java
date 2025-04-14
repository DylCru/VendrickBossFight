package me.crazyrain.vendrickbossfight.inventories;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class ClickEvents implements Listener {

    @EventHandler
    public void AddItem(InventoryClickEvent e){
        if (e.getClickedInventory() == null ||!(e.getClickedInventory().getHolder() instanceof VenInventory)){
            return;
        }

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();

        ItemStack item = e.getCurrentItem();
        if (item != null  && !item.getType().equals(Material.BLACK_STAINED_GLASS_PANE) && !item.getType().equals(Material.ARROW)){
            player.getInventory().addItem(e.getCurrentItem());
        }
    }

    @EventHandler
    public void movePage(InventoryClickEvent e){
        if (e.getClickedInventory() == null ||!(e.getClickedInventory().getHolder() instanceof VenInventory)){
            return;
        }

        e.setCancelled(true);
        VenInventory inv = null;
        Player player = (Player) e.getWhoClicked();

        ItemStack item = e.getCurrentItem();
        if (item != null  && item.getType().equals(Material.ARROW)){
            int pageNum = Integer.parseInt(String.valueOf(item.getItemMeta().getLore().get(0).toCharArray()[7]));
            switch (pageNum){
                case 1:
                    inv = new VenInventory("Vendrick Items: All items", ItemManager.allItems, 1, false);
                    break;
                case 2:
                    inv = new VenInventory("Vendrick Items: Vendrick", ItemManager.vendrick, 2, false);
                    break;
                case 3:
                    inv = new VenInventory("Vendrick Items: Items", ItemManager.items, 3, false);
                    break;
                case 4:
                    inv = new VenInventory("Vendrick Items: Materials", ItemManager.materials, 4, true);
                    break;
            }
            player.openInventory(inv.getInventory());
        }
    }

    @EventHandler
    public void searchForItem(InventoryClickEvent e){
        if (e.getClickedInventory() == null ||!(e.getClickedInventory().getHolder() instanceof VenInventory)){
            return;
        }

        e.setCancelled(true);
        VenInventory inv = null;
        Player player = (Player) e.getWhoClicked();

        ItemStack item = e.getCurrentItem();
        if (item != null  && item.getType().equals(Material.OAK_SIGN)){
            player.closeInventory();

            ConversationFactory convoFac = new ConversationFactory(VendrickBossFight.plugin).withTimeout(10).withFirstPrompt(new StringPrompt() {
                @Override
                public String getPromptText(ConversationContext context) {
                    return ChatColor.GREEN + "Enter the name of the item you would like to search for in chat.";
                }

                @Override
                public Prompt acceptInput(ConversationContext context, String input) {
                    ItemStack[] items = findItems(input);
                    if (items == null) {
                        return new MessagePrompt() {
                            @Override
                            public String getPromptText(ConversationContext context) {
                                return ChatColor.RED + "Couldn't find item: " + input;
                            }

                            @Override
                            protected Prompt getNextPrompt(ConversationContext context) {
                                return null;
                            }
                        };
                    } else {
                        player.sendMessage(Arrays.toString(items));
                        return new MessagePrompt() {
                            @Override
                            protected Prompt getNextPrompt(ConversationContext context) {
                                return null;
                            }

                            @Override
                            public String getPromptText(ConversationContext context) {
                                VenInventory inv = new VenInventory("Item Search: \"" + input + "\"", items, 0, true);
                                player.openInventory(inv.getInventory());
                                return ChatColor.GREEN + "Found " + items.length + " items with name: " + input;
                            }
                        };
                    }
                }
            });
            Conversation convo = convoFac.buildConversation(player);
            player.beginConversation(convo);
        }
    }

    public static ItemStack[] findItems(String name){
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item: ItemManager.allItems){
            if (item.getItemMeta().getDisplayName().toUpperCase().contains(name.toUpperCase())){
                items.add(item);
            }
        }
        if (items.size() >= 1){
            return items.toArray(new ItemStack[0]);
        }
        return null;
    }
}
