package de.alphaomegait.ao18n;

import de.alphaomegait.ao18n.configurations.I18nConfigSection;
import me.blvckbytes.bukkitevaluable.ConfigKeeper;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

/**
 * The AO18n class is responsible for loading and managing translation files
 * for a Bukkit plugin. It dynamically loads translation files from the JAR
 * and processes them for use within the plugin.
 */
public class AO18n {
    
    private static final String TRANSLATION_FOLDER = "translations";
    
    private final JavaPlugin plugin;
    private static final Map<String, Map<String, List<String>>> TRANSLATIONS = new HashMap<>();
    private static String defaultLocale = "en";
    
    /**
     * Constructs an AO18n instance and initializes the translation system.
     *
     * @param plugin The JavaPlugin instance.
     */
    public AO18n(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            loadTranslations();
            logInitialization();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load translations", e);
        }
    }
    
    /**
     * Loads translation files from the JAR and processes them.
     *
     * @throws Exception If no translation files are found or an error occurs during loading.
     */
    private void loadTranslations() throws Exception {
        File translationsDir = new File(plugin.getDataFolder(), TRANSLATION_FOLDER);
        if (!translationsDir.exists() && !translationsDir.mkdirs()) {
            throw new IllegalStateException("Could not create translations directory at " + translationsDir.getAbsolutePath());
        }
        
        // Load resource files from the JAR
        loadResourceFiles(translationsDir);
        
        File[] files = translationsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (files == null || files.length == 0) {
            throw new Exception("No translation files found in " + translationsDir.getAbsolutePath());
        }
        
        ConfigManager translationManager = new ConfigManager(plugin, TRANSLATION_FOLDER);
        
        for (File file : files) {
            String locale = file.getName().replaceFirst("\\.yml$", "");
            ConfigKeeper<I18nConfigSection> translationConfig = new ConfigKeeper<>(translationManager, file.getName(), I18nConfigSection.class);
            I18nConfigSection i18nConfig = translationConfig.rootSection;
            
            if (defaultLocale == null) {
                defaultLocale = i18nConfig.getDefaultLocale();
            }
            
            i18nConfig.loadTranslations(locale);
            
            Map<String, Map<String, List<String>>> translationsForLocale = i18nConfig.getProcessedTranslations();
            mergeTranslations(translationsForLocale);
        }
    }
    
    /**
     * Merges the translations for a specific locale into the main translations map.
     *
     * @param translationsForLocale The translations map for a specific locale.
     */
    private void mergeTranslations(Map<String, Map<String, List<String>>> translationsForLocale) {
        for (Map.Entry<String, Map<String, List<String>>> entry : translationsForLocale.entrySet()) {
            String key = entry.getKey();
            Map<String, List<String>> localeMap = entry.getValue();
            
            TRANSLATIONS.computeIfAbsent(key, k -> new HashMap<>()).putAll(localeMap);
        }
    }
    
    /**
     * Retrieves an unmodifiable view of the translations map.
     *
     * @return The translations map.
     */
    public static Map<String, Map<String, List<String>>> getTranslations() {
        return Collections.unmodifiableMap(TRANSLATIONS);
    }
    
    /**
     * Retrieves the default locale for translations.
     *
     * @return The default locale.
     */
    public static String getDefaultLocale() {
        return defaultLocale;
    }
    
    /**
     * Logs the initialization details of the translation system.
     */
    private void logInitialization() {
        String message = """
                ===============================================================================================
                     _______ __         __           _______                                   _______ _______
                    |   _   |  |.-----.|  |--.---.-.|       |.--------.-----.-----.---.-._____|_     _|_     _|
                    |       |  ||  _  ||     |  _  ||   -   ||        |  -__|  _  |  _  |______||   |_  |   |
                    |___|___|__||   __||__|__|___._||_______||__|__|__|_____|___  |___._|     |_______| |___|
                                      |__|                                 |_____|

                ===============================================================================================
                Language System Initialized
                Company: AlphaOmega-IT
                Website: www.alphaomega-it.com
                ===============================================================================================
                Languages Loaded: %d
                Translation Keys: %d
                ===============================================================================================
                """;
        
        int languageCount = TRANSLATIONS.values().stream().findFirst().map(Map::size).orElse(0);
        int keyCount = TRANSLATIONS.size();
        plugin.getLogger().info(String.format(message, languageCount, keyCount));
    }
    
    /**
     * Loads resource files from the JAR into the translations directory.
     *
     * @param translationsDir The directory where translation files should be copied.
     */
    private void loadResourceFiles(File translationsDir) {
        try {
            // Get the path to the JAR file
            String jarPath = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            try (JarFile jarFile = new JarFile(jarPath)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    
                    // Check if the entry is a .yml file within the translations directory
                    if (entryName.startsWith("translations/") && entryName.endsWith(".yml") && !entry.isDirectory()) {
                        String fileName = entryName.substring(entryName.lastIndexOf('/') + 1);
                        File targetFile = new File(translationsDir, fileName);
                        
                        if (!targetFile.exists()) {
                            try (InputStream in = plugin.getResource(entryName)) {
                                if (in != null) {
                                    Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                }
                            } catch (IOException e) {
                                plugin.getLogger().log(Level.SEVERE, "Failed to copy resource file: " + fileName, e);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load resource files from JAR", e);
        }
    }
}