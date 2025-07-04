package me.crazyrain.vendrickbossfight;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.crazyrain.vendrickbossfight.Commands.Commands;
import me.crazyrain.vendrickbossfight.Commands.FightCommands;
import me.crazyrain.vendrickbossfight.attacks.*;
import me.crazyrain.vendrickbossfight.functionality.merchant.DMerchantFunc;
import me.crazyrain.vendrickbossfight.functionality.merchant.MaterialMerchFunc;
import me.crazyrain.vendrickbossfight.functionality.merchant.MerchantFunc;
import me.crazyrain.vendrickbossfight.vendrick.dark.DarkEvents;
import me.crazyrain.vendrickbossfight.vendrick.dark.DarkRuneHandler;
import me.crazyrain.vendrickbossfight.vendrick.dark.DarkVendrick;
import me.crazyrain.vendrickbossfight.vendrick.dark.VendrickTNT;
import me.crazyrain.vendrickbossfight.vendrick.dark.spirits.FlameSpiritEvents;
import me.crazyrain.vendrickbossfight.vendrick.dark.spirits.StormSpiritEvents;
import me.crazyrain.vendrickbossfight.vendrick.dark.spirits.TideSpiritEvents;
import me.crazyrain.vendrickbossfight.vendrick.dark.spirits.TsunamiCountdown;
import me.crazyrain.vendrickbossfight.vendrick.flaming.FlameEvents;
import me.crazyrain.vendrickbossfight.vendrick.stormy.Hurricane;
import me.crazyrain.vendrickbossfight.vendrick.stormy.StormyEvents;
import me.crazyrain.vendrickbossfight.vendrick.tidal.TidalVendrick;
import me.crazyrain.vendrickbossfight.vendrick.tidal.TideEvents;
import me.crazyrain.vendrickbossfight.functionality.*;
import me.crazyrain.vendrickbossfight.inventories.ClickEvents;
import me.crazyrain.vendrickbossfight.inventories.RecipeInvEvents;
import me.crazyrain.vendrickbossfight.items.CraftHandler;
import me.crazyrain.vendrickbossfight.items.CraftManager;
import me.crazyrain.vendrickbossfight.items.DefaultRecipes;
import me.crazyrain.vendrickbossfight.items.ItemManager;
import me.crazyrain.vendrickbossfight.vendrick.Vendrick;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class VendrickBossFight extends JavaPlugin {

    public static YamlConfiguration LANG;
    public static File LANG_FILE;
    public List<Bar> bars = new ArrayList<>();
    public List<Location> configSpawnLocs = new ArrayList<>();

    Logger log;

    public static VendrickBossFight plugin;

    FightManager fightManager = new FightManager();
    public LootHandler lootHandler;
    CraftManager craftManager;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        log = VendrickBossFight.getPlugin(VendrickBossFight.class).getLogger();
        log.log(Level.INFO, "Plugin is online. Vendrick awaits.");

        LANG = loadLang();
        ItemManager.Init();
        initLocations();
        ItemGlow.initTeams();
        lootHandler = new LootHandler();

        craftManager = new CraftManager(getRecipeFile(), plugin.getLogger());

        getServer().getPluginManager().registerEvents(new Events(this),this);
        getServer().getPluginManager().registerEvents(new ZombieHoard(this), this);
        getServer().getPluginManager().registerEvents(new PigBombs(this), this);
        getServer().getPluginManager().registerEvents(new PlayerShatter(this), this);
        getServer().getPluginManager().registerEvents(new NutrimentFunc(), this);
        getServer().getPluginManager().registerEvents(new PortalWraiths(this), this);
        getServer().getPluginManager().registerEvents(new MerchantFunc(this), this);
        getServer().getPluginManager().registerEvents(new DMerchantFunc(this), this);
        getServer().getPluginManager().registerEvents(new MaterialMerchFunc(this), this);
        getServer().getPluginManager().registerEvents(new FlameEvents(this),this);
        getServer().getPluginManager().registerEvents(new TideEvents(this), this);
        getServer().getPluginManager().registerEvents(new StormyEvents(this), this );
        getServer().getPluginManager().registerEvents(new EnergyRifle(this), this);
        getServer().getPluginManager().registerEvents(new ItemGlow(), this);
        getServer().getPluginManager().registerEvents(new ClickEvents(), this);
        getServer().getPluginManager().registerEvents(new VendrickTNT(), this);
        getServer().getPluginManager().registerEvents(new DarkEvents(this), this);
        getServer().getPluginManager().registerEvents(new FlameSpiritEvents(this), this);
        getServer().getPluginManager().registerEvents(new TideSpiritEvents(this), this);
        getServer().getPluginManager().registerEvents(new StormSpiritEvents(this), this);
        getServer().getPluginManager().registerEvents(new VenArmourEvents(this), this);
        getServer().getPluginManager().registerEvents(new CraftHandler(this), this);
        getServer().getPluginManager().registerEvents(new RecipeInvEvents(this), this);
        getServer().getPluginManager().registerEvents(new BossHealthEvents(this), this);

        getCommand("ven").setExecutor(new Commands(this));
        getCommand("venfight").setExecutor(new FightCommands(this));

        Metrics metrics = new Metrics(plugin, 14496);

        new UpdateChecker(this, 94830).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("You are running the latest version! (" + this.getDescription().getVersion() + ")");
            } else {
                getLogger().info("There is a new update available! (" + version + ")");
            }
        });
    }

    @Override
    public void onDisable(){
        ItemGlow.removeTeams();
        fightManager.clearDown();
    }

    public void reloadPluginConfig() {
        reloadConfig();
        initLocations();
        getCraftManager().reloadRecipes(getRecipeFile());
        lootHandler.refreshChances();
    }

    public void initLocations(){
        configSpawnLocs.clear();
        log.log(Level.INFO, "Setting up custom spawning locations...");
        for (String key : getConfig().getConfigurationSection("spawn-locations").getKeys(false)){
            List<Integer> coords = getConfig().getIntegerList("spawn-locations." + key);
            try {
                Location location = new Location(getServer().getWorld("world"), coords.get(0), coords.get(1), coords.get(2));
                configSpawnLocs.add(location);
            } catch (Exception e){
                log.log(Level.WARNING, "Couldn't initialise custom location: " + key + ". Please ensure the coordinates you entered are integers.");
            }
        }

        log.log(Level.INFO, "Done! You have " + configSpawnLocs.size() + " Custom location(s) for Vendrick to spawn at.");
    }

    public YamlConfiguration loadLang() {
        File lang = new File(getDataFolder(), "lang.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = this.getResource("lang.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(lang);
                    defConfig.save(lang);
                    Lang.setFile(defConfig);
                    return defConfig;
                }
            } catch(IOException e) {
                e.printStackTrace(); // So they notice
                log.severe("[Vendrick] Couldn't create language file.");
                log.severe("[Vendrick] This is a fatal error. Now disabling");
                this.setEnabled(false); // Without it loaded, we can't send them messages
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for(Lang item:Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        VendrickBossFight.LANG = conf;
        VendrickBossFight.LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch(IOException e) {
            log.log(Level.WARNING, "Vendrick: Failed to save lang.yml.");
            log.log(Level.WARNING, "Vendrick: Report this stack trace to RainStxrm.");
            e.printStackTrace();
        }
        return conf;
    }

    /**
     * Gets the lang.yml config.
     * @return The lang.yml config.
     */
    public YamlConfiguration getLang() {
        return LANG;
    }

    /**
     * Get the lang.yml file.
     * @return The lang.yml file.
     */
    public File getLangFile() {
        return LANG_FILE;
    }

    private FileInputStream getRecipeFile()  {
        File file = new File(getDataFolder(), "recipes.json");
        if (!file.exists()) {
            Map<String, Map<String, String[]>> recipeData = new HashMap<>();
            getLogger().log(Level.INFO, "recipes.json doesn't exist, creating one.");
            try {
                getDataFolder().mkdir();
                file.createNewFile();
                recipeData.put("recipes", DefaultRecipes.getDefaultRecipes());
                saveDefaultRecipes(recipeData, file);
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Unable to create recipes.json!");
                return null;
            }
        }

        try {
            return new FileInputStream(new File(getDataFolder(), "recipes.json"));
        } catch (FileNotFoundException e) {
            log.severe("Couldn't load recipe file.");
            log.severe("This is a fatal error. Now disabling");
        }
        return null;
    }

    public void saveDefaultRecipes(Map<String, Map<String, String[]>> recipeData, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = new FileWriter(file);
        gson.toJson(recipeData, writer);
        writer.close();
    }

    public CraftManager getCraftManager() {
        return this.craftManager;
    }

    public FightManager getFightManager() {
        return fightManager;
    }

    public void setFightManager(FightManager fightManager) {
        this.fightManager = fightManager;
    }

}
