package de.alphaomegait.ao18n.configurations;

import me.blvckbytes.bbconfigmapper.sections.CSAlways;
import me.blvckbytes.bbconfigmapper.sections.IConfigSection;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class I18nConfiguration implements IConfigSection, I18nConfigurationProvider {

	@CSAlways
	private String defaultLocale;

	@CSAlways
	private Map<String, Object> translations;

	/**
	 * Returns the default locale.
	 *
	 * @return the default locale as a string.
	 */
	@Override
	public String getDefaultLocale() {
		// Return "en" if defaultLocale is null, otherwise return defaultLocale.
		return this.defaultLocale == null ? "en" : this.defaultLocale;
	}

	/**
	 * Retrieves the translations map.
	 *
	 * @return the translations map
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, List<String>>> getTranslations() {
		// Check if translations map is null or empty
		if (this.translations == null || this.translations.isEmpty()) {
			return Collections.emptyMap();
		}

		// Convert translations map to linked hash map
		return this.translations
			.entrySet()
			.stream()
			.collect(
				Collectors.toMap(
					Map.Entry::getKey,
					entry -> (Map<String, List<String>>) entry.getValue(),
					(translationKey, translationValue) -> translationValue,
					LinkedHashMap::new
				)
			);
	}
}