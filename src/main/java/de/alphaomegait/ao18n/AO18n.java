package de.alphaomegait.ao18n;

import de.alphaomegait.ao18n.configurations.I18nConfigSection;
import de.alphaomegait.ao18n.i18n.I18nFactory;
import me.blvckbytes.bukkitevaluable.ConfigKeeper;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Represents the main class for managing internationalization in the application.
 */
public class AO18n {

    private final static String TRANSLATION_FOLDER = "translations";

    private final JavaPlugin loadedPlugin;

    private static Map<String, Map<String, List<String>>> TRANSLATIONS = new HashMap<>();
    private static String DEFAULT_LOCALE;

    public AO18n(
        final @NotNull JavaPlugin loadedPlugin
    ) {
        this.loadedPlugin = loadedPlugin;
        try {

            var translationManager = new ConfigManager(this.loadedPlugin, TRANSLATION_FOLDER);
            var translationConfig = new ConfigKeeper<>(translationManager, "i18n.yml", I18nConfigSection.class);

            I18nFactory i18nFactory = new I18nFactory(translationConfig);

            TRANSLATIONS = i18nFactory.getI18nConfiguration().getTranslations();
            DEFAULT_LOCALE = i18nFactory.getI18nConfiguration().getDefaultLocale();
            this.logInitialization();
        } catch (Exception e) {
            this.loadedPlugin.getLogger().log(Level.SEVERE, "Failed to load translations", e);
        }
    }

    /**
     * Retrieves the translations map.
     *
     * @return the translations map
     */
    public static Map<String, Map<String, List<String>>> getTranslations() {
        return TRANSLATIONS;
    }

    /**
     * Retrieves the default locale.
     *
     * @return the default locale
     */
    public static String getDefaultLocale() {
        return DEFAULT_LOCALE;
    }

    /**
     * Logs the initialization details of the language system.
     */
    private void logInitialization() {
        String message =
        """
        ===============================================================================================
             _______ __         __           _______                                   _______ _______ 
            |   _   |  |.-----.|  |--.---.-.|       |.--------.-----.-----.---.-._____|_     _|_     _|
            |       |  ||  _  ||     |  _  ||   -   ||        |  -__|  _  |  _  |______||   |_  |   |  
            |___|___|__||   __||__|__|___._||_______||__|__|__|_____|___  |___._|     |_______| |___|  
        ===============================================================================================
        Language System by: SaltyFeaRz
        Company: AlphaOmega-IT
        Website: www.alphaomega-it.com
        ===============================================================================================
        Language System is initialized with...
        (%translation_languages%x) Languages
        (%translation_keys%x) Language Keys
        ===============================================================================================
        """;

        this.loadedPlugin.getLogger().log(
                Level.INFO,
                message
                    .replaceAll("%translation_languages%", String.valueOf(TRANSLATIONS.getOrDefault("prefix", Map.of()).size()))
                    .replaceAll("%translation_keys%", String.valueOf(TRANSLATIONS.values().size()))
        );
    }
}