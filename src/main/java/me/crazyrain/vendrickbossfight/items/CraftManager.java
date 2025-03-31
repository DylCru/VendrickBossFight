package me.crazyrain.vendrickbossfight.items;

import jakarta.json.*;
import me.crazyrain.vendrickbossfight.inventories.RecipeInventory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.FileInputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CraftManager {

    private FileInputStream itemsFile;
    private final Logger logger;

    private final HashMap<ItemStack, HashMap<Integer, ItemStack>> recipes = new HashMap<>();

    private final List<String> failed = new ArrayList<>();

    private boolean loaded = false;

    private HashMap<UUID, RecipeInventory> openInventories = new HashMap<>();

    public CraftManager (FileInputStream itemsFile, Logger logger) {
        this.logger = logger;
        this.itemsFile = itemsFile;
        load();
    }

    private void load() {
        logger.log(Level.INFO, "Loading Crafting Recipes...");
        try {
            JsonReader reader = Json.createReader(itemsFile);
            JsonObject object = reader.readObject();
            JsonObject recipes = object.getJsonObject("recipes");
            if (recipes == null)  {
                logger.log(Level.SEVERE, "Unable to load recipes from recipes.json!");
            } else {
                mapRecipes(recipes);
            }
        } catch (ExceptionInInitializerError | JsonException e) {
            logger.log(Level.SEVERE, "Unable to load recipes from recipes.json!");
        }

    }

    private void mapRecipes(JsonObject recipeObject) {
        for (Map.Entry<String, JsonValue> value : recipeObject.entrySet()) {
            ItemStack result = ItemID.getItemStackById(value.getKey());
            if (result == null) {
                logFailedRecipe(value.getKey(), value.getKey());
                continue;
            }
            JsonArray ingArray = value.getValue().asJsonArray();
            HashMap<Integer, ItemStack> ingredients = new HashMap<>();
            for (int i = 0; i < ingArray.size(); i++) {
                String ing = ingArray.getString(i);
                if (ing.isEmpty()) {
                    continue;
                }
                if (!ing.startsWith("VEN_")) { //Parse Vanilla items
                    try {
                        ItemStack itemStack = new ItemStack(Material.valueOf(ing));
                        ingredients.put(i, itemStack);
                    } catch (IllegalArgumentException e) {
                        logFailedRecipe(value.getKey(), ing);
                        break;
                    }
                    continue;
                }
                ItemStack venItem = ItemID.getItemStackById(ing);
                if (venItem == null) {
                    logFailedRecipe(value.getKey(), ing);
                } else {
                    ingredients.put(i, venItem);
                }
            }
            if (!ingredients.isEmpty()) recipes.put(result, ingredients);
        }
        logger.log(Level.INFO, "Done! Loaded " + recipes.size() + " crafting recipes");
        loaded = true;
        if (!failed.isEmpty()) logger.log(Level.WARNING, "Failed to load " + failed.size() + " crafting recipes. " + failed);
    }

    private void logFailedRecipe(String recipe, String ing) {
        failed.add(recipe);
        logger.log(Level.WARNING,"FAILED TO LOAD RECIPE: " + recipe + " -- Error occurred trying to parse \"" + ing + "\"");
    }

    //TODO: Add getAllRecipesForMaterial(ItemStack material)

    public HashMap<ItemStack, HashMap<Integer, ItemStack>> getAllRecipesForMaterial(ItemStack material) {
        //Scan through all the recipes
        //Find the ones where the material is present
        HashMap<ItemStack, HashMap<Integer, ItemStack>> materialRecipes = new HashMap<>();
        for (ItemStack result : getRecipes().keySet()) {
            HashMap<Integer, ItemStack> recipe = getRecipes().get(result);
            for (ItemStack ing : recipe.values()) {
                if (ing.equals(material)) {
                    materialRecipes.put(result, getRecipes().get(result));
                    break;
                }
            }
        }

        return materialRecipes;
    }

    public HashMap<ItemStack, HashMap<Integer, ItemStack>> getRecipes() {
        return recipes;
    }

    public HashMap<UUID, RecipeInventory> getOpenInventories() {
        return openInventories;
    }

    public void addInventoryInstance(UUID player, RecipeInventory instance) {
        this.openInventories.put(player, instance);
    }

    public void removeInventoryInstance(UUID player) {
        this.openInventories.remove(player);
    }

    public RecipeInventory getInventoryInstance(UUID player) {
        return this.openInventories.get(player);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void reloadRecipes(FileInputStream file) {
        this.itemsFile = file;
        this.recipes.clear();
        this.failed.clear();
        load();
    }

    public List<String> getFailedRecipes() {
        return failed;
    }
}
