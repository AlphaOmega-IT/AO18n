package de.alphaomegait.ao18n.configurations;

import java.util.List;
import java.util.Map;

public interface I18nConfigurationProvider {

	/**
	 * Retrieves the default locale.
	 *
	 * @return         	the default locale
	 */
	String getDefaultLocale();

	/**
	 * Retrieves the translations for the application.
	 *
	 * @return  a map containing the translations. The map has a structure where the
	 *          key is the language code, the value is another map with the translation
	 *          keys and their corresponding translations as a list of strings.
	 */
	Map<String, Map<String, List<String>>> getTranslations();
}