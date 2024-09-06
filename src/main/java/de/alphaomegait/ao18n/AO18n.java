package de.alphaomegait.ao18n;

import de.alphaomegait.ao18n.i18n.I18nFactory;
import de.alphaomegait.aocore.AOCore;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the main class for managing internationalization in the application.
 */
public class AO18n {

    private final AOCore aoCore;
    private final I18nFactory i18nFactory;

    private static Map<String, Map<String, List<String>>> TRANSLATIONS = new HashMap<>();
    private static String DEFAULT_LOCALE;

    /**
     * Constructs an instance of the AO18n class.
     *
     * @param aoCore the AOCore instance
     */
    public AO18n(
        final @NotNull AOCore aoCore
    ) {
        this.aoCore = aoCore;
        this.i18nFactory = this.aoCore.getI18nFactory();

        if (i18nFactory != null) {
            TRANSLATIONS = this.i18nFactory.getI18nConfiguration().getTranslations();
            DEFAULT_LOCALE = this.i18nFactory.getI18nConfiguration().getDefaultLocale();
        }

        this.logInitialization();
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
        this.aoCore.getLogger().logInfo(
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
        ( + TRANSLATIONS.values().size() + x) Languages //TODO CHANGE IT THAT IT WILL JUST DISPLAY THE LANGUAGES
        ( + TRANSLATIONS.values().size() + x) Language Keys
        ===============================================================================================
        """);
    }
}