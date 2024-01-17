package de.alphaomegait.ao18n;

import de.alphaomegait.ao18n.configurations.I18nConfiguration;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class I18nFactory {

	private final @Nullable I18nConfiguration i18nConfiguration;

	/**
	 * Constructs an instance of the I18nFactory class.
	 *
	 * @param configManager  the ConfigManager object used to retrieve the I18nConfiguration
	 * @throws Exception     if an error occurs during the construction of the I18nConfiguration
	 */
	public I18nFactory(
		final @NotNull ConfigManager configManager
	) throws Exception {
		this.i18nConfiguration = configManager
			.getMapper(
				"translations/i18n.yml"
			)
			.mapSection(
				"",
				I18nConfiguration.class
			);
	}

	/**
	 * Retrieves the I18nConfiguration object associated with this instance.
	 *
	 * @return         	the I18nConfiguration object
	 */
	public @NotNull I18nConfiguration getI18nConfiguration() {
		if (this.i18nConfiguration == null)
			throw new IllegalStateException("I18nConfiguration is null! Make sure it's correctly setup!");

		return this.i18nConfiguration;
	}
}