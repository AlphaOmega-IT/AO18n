package de.alphaomegait.ao18n.configurations;

import me.blvckbytes.bbconfigmapper.sections.AConfigSection;
import me.blvckbytes.bbconfigmapper.sections.CSAlways;
import me.blvckbytes.gpeee.interpreter.EvaluationEnvironmentBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CSAlways
public class I18nConfigSection extends AConfigSection {

    private String defaultLocale;
    private final Map<String, Object> translations;

    public I18nConfigSection(EvaluationEnvironmentBuilder baseEnvironment) {
        super(baseEnvironment);
        this.translations = new HashMap<>();
    }

    public String getDefaultLocale() {
        return defaultLocale != null ? defaultLocale : "en";
    }

    /**
     * Retrieves the processed translations map.
     * The map is structured as:
     * Map<String, Map<String, List<String>>>
     * where the key is the translation key,
     * the inner map's key is the locale,
     * and the value is a list of strings (lines) for the translation.
     *
     * @return The processed translations map.
     */
    public Map<String, Map<String, List<String>>> getProcessedTranslations() {
        return processedTranslations;
    }

    // Internal map to hold the processed translations
    private final Map<String, Map<String, List<String>>> processedTranslations = new HashMap<>();

    /**
     * Processes the existing translations map, converting it into the required structure.
     * Assumes that the 'translations' field already contains all keys and values from the configuration handler.
     *
     * @param locale The locale code (e.g., "en", "de") for which the translations are provided.
     */
    @SuppressWarnings("unchecked")
    public void loadTranslations(String locale) {
        // Process each entry in the translations map
        processTranslations("", locale, translations);
    }

    @SuppressWarnings("unchecked")
    private void processTranslations(String parentKey, String locale, Map<String, Object> currentMap) {
        for (Map.Entry<String, Object> entry : currentMap.entrySet()) {
            String currentKey = parentKey.isEmpty() ? entry.getKey() : parentKey + "." + entry.getKey();
            Object value = entry.getValue();

	        switch (value) {
		        case String s ->
			        // Single-line translation
				        processedTranslations
						        .computeIfAbsent(currentKey, k -> new HashMap<>())
						        .computeIfAbsent(locale, l -> List.of((String) value));
		        case List<?> list -> {
			        // Multi-line translation
			        List<String> lines = (List<String>) value;
			        processedTranslations
					        .computeIfAbsent(currentKey, k -> new HashMap<>())
					        .put(locale, lines);
		        }
		        case Map<?, ?> map -> {
			        // Nested translations
			        Map<String, Object> nestedMap = (Map<String, Object>) value;
			        processTranslations(currentKey, locale, nestedMap);
		        }
		        case null, default -> {
			        // Handle unsupported types
			        // Optionally log a warning or throw an exception
		        }
	        }
        }
    }
}