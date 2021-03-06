package com.drtshock.obsidiandestroyer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public final class ODConfig {

    private ObsidianDestroyer plugin;
    private static String PLUGIN_VERSION;
    private static String DIRECTORY = "plugins" + File.separator + "ObsidianDestroyer" + File.separator;
    private File configFile = new File(DIRECTORY + "config.yml");
    private File durabilityFile = new File(DIRECTORY + "durability.dat");
    private YamlConfiguration bukkitConfig = new YamlConfiguration();

    private int explosionRadius = 3;
    private boolean tntEnabled = true;
    private boolean cannonsEnabled = false;
    private boolean creepersEnabled = false;
    private boolean ghastsEnabled = false;
    private boolean withersEnabled = false;
    private boolean durabilityEnabled = false;
    private boolean bedrockEnabled = false;
    private int odurability = 1;
    private int edurability = 1;
    private int ecdurability = 1;
    private int adurability = 1;
    private int bdurability = 1;
    private boolean durabilityTimerEnabled = true;
    private long durabilityTime = 600000L;
    private double chanceToDropBlock = 0.7D;
    private boolean waterProtection = true;
    private boolean checkUpdate = true;
    private int checkitemid = 38;
    private boolean ignorecancel = false;
    private static String[] VALUES = new String[25];
    private boolean durabilityTimerSafey = false;
    private int minFreeMemoryLimit = 80;
    private boolean explodeInLiquid = false;
    private boolean protectTNTCannons = true;
    private ArrayList<String> disabledWorlds = new ArrayList<String>();

    public ODConfig(ObsidianDestroyer plugin) {
        this.plugin = plugin;
    }

    public boolean loadConfig() {
        boolean isErrorFree = true;
    	PluginDescriptionFile pdfFile = plugin.getDescription();
    	PLUGIN_VERSION = pdfFile.getVersion();

        new File(DIRECTORY).mkdir();

        if (this.configFile.exists()) {
            try {
                this.bukkitConfig.load(this.configFile);

                if (this.bukkitConfig.getString("Version", "").equals(PLUGIN_VERSION)) {
                    loadData();
                } else {
                    ObsidianDestroyer.LOG.info("Config file outdated. Renamed old and wrote new. Make sure to change.");
                    loadData();
                    writeDefault();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            ObsidianDestroyer.LOG.info("config file not found, creating new config file :D");
            this.plugin.saveDefaultConfig();
        }

        return isErrorFree;
    }
    
    public void reloadConfig() {
        if (this.configFile.exists()) {
            try {
                this.bukkitConfig.load(this.configFile);
                ObsidianDestroyer.LOG.info("Config file found, reloading config...");
                
                if (this.bukkitConfig.getString("Version", "").equals(PLUGIN_VERSION)) {
                    loadData();
                }
                else {
                	ObsidianDestroyer.LOG.info("Version mismatch between plugin and config file!");                     	
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData() {
        try {

            ChatColor y = ChatColor.YELLOW;
            ChatColor g = ChatColor.GRAY;

            this.bukkitConfig.load(this.configFile);

            this.checkUpdate = this.bukkitConfig.getBoolean("checkupdate", true);
            this.explosionRadius = this.bukkitConfig.getInt("Radius", 3);
            this.waterProtection = this.bukkitConfig.getBoolean("FluidsProtectObsidian", true);
            this.checkitemid = this.bukkitConfig.getInt("CheckItemId", 38);
            this.ignorecancel = this.bukkitConfig.getBoolean("IgnoreCancel", false);
            this.bedrockEnabled = this.bukkitConfig.getBoolean("Durability.Bedrock.Enabled", false);

            this.tntEnabled = this.bukkitConfig.getBoolean("EnabledFor.TNT", true);
            this.cannonsEnabled = this.bukkitConfig.getBoolean("EnabledFor.Cannons", false);
            this.creepersEnabled = this.bukkitConfig.getBoolean("EnabledFor.Creepers", false);
            this.ghastsEnabled = this.bukkitConfig.getBoolean("EnabledFor.Ghasts", false);
            this.withersEnabled = this.bukkitConfig.getBoolean("EnabledFor.Withers", false);

            this.durabilityEnabled = this.bukkitConfig.getBoolean("Durability.Enabled", false);
            this.odurability = this.bukkitConfig.getInt("Durability.Obsidian", 1);
            this.edurability = this.bukkitConfig.getInt("Durability.EnchantmentTable", 1);
            this.ecdurability = this.bukkitConfig.getInt("Durability.EnderChest", 1);
            this.adurability = this.bukkitConfig.getInt("Durability.Anvil", 1);
            this.bdurability = this.bukkitConfig.getInt("Durability.Bedrock.Durability", 1);
            this.durabilityTimerEnabled = this.bukkitConfig.getBoolean("Durability.ResetEnabled", true);

            this.durabilityTimerSafey = this.bukkitConfig.getBoolean("Durability.UseTimerSafety", false);
            this.minFreeMemoryLimit = this.bukkitConfig.getInt("Durability.SystemMinMemory", 80);
            
            this.durabilityTime = readLong("Durability.ResetAfter", "600000");
            this.chanceToDropBlock = this.bukkitConfig.getDouble("Blocks.ChanceToDrop", 0.7D);
            
            this.explodeInLiquid = this.bukkitConfig.getBoolean("Explosions.BypassAllFluidProtection", false);
            this.protectTNTCannons = this.bukkitConfig.getBoolean("Explosions.TNTCannonsProtected", true);
            
            this.disabledWorlds = (ArrayList<String>) this.bukkitConfig.getStringList("DisabledOnWorlds");
            
            VALUES[0] = y + "checkupdate: " + g + this.checkUpdate;
            VALUES[1] = y + "ExplosionRadius: " + g + this.getRadius();
            VALUES[2] = y + "FluidsProtectObsidian: " + g + this.getWaterProtection();
            VALUES[3] = y + "CheckItemId: " + g + this.getCheckItemId();
            VALUES[4] = y + "IgnoreCancel: " + g + this.getIgnoreCancel();
            VALUES[5] = y + "TNTEnabled: " + g + this.getTntEnabled();
            VALUES[6] = y + "CannonsEnabled: " + g + this.getCannonsEnabled();
            VALUES[7] = y + "CreepersEnabled: " + g + this.getCreepersEnabled();
            VALUES[8] = y + "GhastsEnabled: " + g + this.getGhastsEnabled();
            VALUES[9] = y + "WithersEnabled: " + g + this.getWithersEnabled();
            VALUES[10] = y + "DurabilityEnabled: " + g + this.getDurabilityEnabled();
            VALUES[11] = y + "ObsidianDurability: " + g + this.getoDurability();
            VALUES[12] = y + "EnchantmentTableDurability: " + g + this.geteDurability();
            VALUES[13] = y + "EnderchestDurability: " + g + this.getecDurability();
            VALUES[14] = y + "AnvilDurability: " + g + this.getaDurability();
            VALUES[15] = y + "BedrockEnabled: " + g + this.getBedrockEnabled();
            VALUES[16] = y + "BedrockDurability: " + g + this.getbDurability();
            VALUES[17] = y + "ResetEnabled: " + g + this.getDurabilityEnabled();
            VALUES[18] = y + "ResetAfter: " + g + this.getDurabilityResetTime();
            VALUES[19] = y + "ChanceToDrop: " + g + this.getChanceToDropBlock();
            VALUES[20] = y + "UseTimerSafety: " + g + this.getDurabilityTimerSafey();
            VALUES[21] = y + "SystemMinMemory: " + g + this.getMinFreeMemoryLimit();
            VALUES[22] = y + "BypassAllFluidProtection: " + g + this.getExplodeInLiquids();
            VALUES[23] = y + "TNTCannonsProtected: " + g + this.getProtectTNTCannons();
            VALUES[24] = y + "DisabledOnWorlds: " + g;
            if (this.getDisabledWorlds() != null)
                for (String dWorld : this.getDisabledWorlds()) {
                    VALUES[24] += dWorld + " ";
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeDefault() {
        this.configFile.renameTo(new File(DIRECTORY + "config.yml.old"));
        
        this.bukkitConfig.set("Version", this.PLUGIN_VERSION);
        this.bukkitConfig.set("checkupdate", this.getCheckUpdate());
        this.bukkitConfig.set("Radius", this.getRadius());
        this.bukkitConfig.set("FluidsProtectObsidian", this.getWaterProtection());
        this.bukkitConfig.set("CheckItemId", this.getCheckItemId());
        this.bukkitConfig.set("IgnoreCancel", this.getIgnoreCancel());
        this.bukkitConfig.set("Durability.Bedrock.Enabled", this.getBedrockEnabled());

        this.bukkitConfig.set("EnabledFor.TNT", this.getTntEnabled());
        this.bukkitConfig.set("EnabledFor.Cannons", this.getCannonsEnabled());
        this.bukkitConfig.set("EnabledFor.Creepers", this.getCannonsEnabled());
        this.bukkitConfig.set("EnabledFor.Ghasts", this.getGhastsEnabled());
        this.bukkitConfig.set("EnabledFor.Withers", this.getWithersEnabled());

        this.bukkitConfig.set("Durability.Enabled", this.getDurabilityEnabled());
        this.bukkitConfig.set("Durability.Obsidian", this.getoDurability());
        this.bukkitConfig.set("Durability.EnchantmentTable", this.geteDurability());
        this.bukkitConfig.set("Durability.EnderChest", this.getecDurability());
        this.bukkitConfig.set("Durability.Anvil", this.getaDurability());
        this.bukkitConfig.set("Durability.Bedrock.Durability", this.getbDurability());
        this.bukkitConfig.set("Durability.ResetEnabled", this.getDurabilityResetTimerEnabled());

        this.bukkitConfig.set("Durability.UseTimerSafety", this.getDurabilityTimerSafey());
        this.bukkitConfig.set("Durability.SystemMinMemory", this.getMinFreeMemoryLimit());
            
        this.bukkitConfig.set("Durability.ResetAfter", this.getDurabilityResetTime());
        this.bukkitConfig.set("Blocks.ChanceToDrop", this.getChanceToDropBlock());
        
        this.bukkitConfig.set("Explosions.BypassAllFluidProtection", this.getExplodeInLiquids());
        this.bukkitConfig.set("Explosions.TNTCannonsProtected", this.getProtectTNTCannons());
        
        this.bukkitConfig.set("DisabledOnWorlds", this.getDisabledWorlds());
        
        try {
			this.bukkitConfig.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        loadData();
    }

    private long readLong(String key, String def) {
        try {
            this.bukkitConfig.load(this.configFile);
        } catch (Exception e) {
            ObsidianDestroyer.LOG.warning("Failed to read reset time.");
        }

        String value = this.bukkitConfig.getString(key, def);

        long tmp = 0L;
        try {
            tmp = Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            ObsidianDestroyer.LOG.warning("Error parsing a long from the config file. Key=" + key);
        }

        return tmp;
    }

    public int getRadius() {
        return this.explosionRadius;
    }

    public boolean getCheckUpdate() {
        return this.checkUpdate;
    }

    public boolean getTntEnabled() {
        return this.tntEnabled;
    }

    public boolean getCannonsEnabled() {
        return this.cannonsEnabled;
    }

    public boolean getCreepersEnabled() {
        return this.creepersEnabled;
    }

    public boolean getGhastsEnabled() {
        return this.ghastsEnabled;
    }

    public boolean getWithersEnabled() {
        return this.withersEnabled;
    }

    public boolean getDurabilityEnabled() {
        return this.durabilityEnabled;
    }

    public int getoDurability() {
        return this.odurability;
    }

    public int geteDurability() {
        return this.edurability;
    }

    public int getecDurability() {
        return this.ecdurability;
    }

    public int getaDurability() {
        return this.adurability;
    }

    public int getbDurability() {
        return this.bdurability;
    }

    public boolean getBedrockEnabled() {
        return this.bedrockEnabled;
    }

    public boolean getDurabilityResetTimerEnabled() {
        return this.durabilityTimerEnabled;
    }

    public long getDurabilityResetTime() {
        return this.durabilityTime;
    }

    public double getChanceToDropBlock() {
        return this.chanceToDropBlock;
    }

    public boolean getWaterProtection() {
        return this.waterProtection;
    }

    public int getCheckItemId() {
        return this.checkitemid;
    }

    public boolean getIgnoreCancel() {
        return this.ignorecancel;
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public String[] getConfigList() {
        return VALUES;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }
    
    public int getMinFreeMemoryLimit() {
    	return this.minFreeMemoryLimit;
    }
    
    public boolean getDurabilityTimerSafey() {
    	return this.durabilityTimerSafey;
    }
    
    public boolean getExplodeInLiquids() {
    	return this.explodeInLiquid;
    }
    
    public boolean getProtectTNTCannons() {
    	return this.protectTNTCannons;
    }
    
    public List<String> getDisabledWorlds() {
    	return this.disabledWorlds;
    }

    public void saveDurabilityToFile() {
        if ((this.plugin.getListener() == null) || (this.plugin.getListener().getObsidianDurability() == null))
            return;

        HashMap<Integer, Integer> map = this.plugin.getListener().getObsidianDurability();

        new File(DIRECTORY).mkdir();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.durabilityFile));
            oos.writeObject(map);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            ObsidianDestroyer.LOG.severe("Failed writing obsidian durability");
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<Integer, Integer> loadDurabilityFromFile() {
        if ((!this.durabilityFile.exists()) || (this.plugin.getListener() == null) || (this.plugin.getListener().getObsidianDurability() == null))
            return null;

        new File(DIRECTORY).mkdir();

        HashMap<Integer, Integer> map = null;
        Object result = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.durabilityFile));
            result = ois.readObject();
            map = (HashMap<Integer, Integer>)result;
            ois.close();
        } catch (IOException ioe) {
            ObsidianDestroyer.LOG.severe("Failed reading obsidian durability.");
            ObsidianDestroyer.LOG.severe("Deleting current durability file and creating new one.");
            this.durabilityFile.delete();

            try {
                this.durabilityFile.createNewFile();
            } catch (IOException exception) {
                ObsidianDestroyer.LOG.severe("Couldn't create new durability file.");
            }
        } catch (ClassNotFoundException cnfe) {
            ObsidianDestroyer.LOG.severe("durability.dat contains an unknown class, was it modified?");
        }

        return map;
    }
}
