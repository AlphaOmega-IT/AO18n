package de.alphaomegait.ao18n;

import de.alphaomegait.ao18n.configurations.I18nConfigSection;
import de.alphaomegait.ao18n.i18n.I18nFactory;
import me.blvckbytes.bukkitevaluable.ConfigKeeper;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class AO18n {

    private static final String TRANSLATION_FOLDER = "translations";

    private final JavaPlugin plugin;
    private static final Map<String, Map<String, List<String>>> TRANSLATIONS = new HashMap<>();
    private static String defaultLocale = "en";

    public AO18n(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            loadTranslations();
            logInitialization();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load translations", e);
        }
    }

    private void loadTranslations() throws Exception {
        File translationsDir = new File(plugin.getDataFolder(), TRANSLATION_FOLDER);
        if (!translationsDir.exists() && !translationsDir.mkdirs()) {
            throw new IllegalStateException("Could not create translations directory at " + translationsDir.getAbsolutePath());
        }

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

    private void mergeTranslations(Map<String, Map<String, List<String>>> translationsForLocale) {
        for (Map.Entry<String, Map<String, List<String>>> entry : translationsForLocale.entrySet()) {
            String key = entry.getKey();
            Map<String, List<String>> localeMap = entry.getValue();

            TRANSLATIONS.computeIfAbsent(key, k -> new HashMap<>()).putAll(localeMap);
        }
    }

    public static Map<String, Map<String, List<String>>> getTranslations() {
        return Collections.unmodifiableMap(TRANSLATIONS);
    }

    public static String getDefaultLocale() {
        return defaultLocale;
    }

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
}