package de.alphaomegait.ao18n.i18n;

import de.alphaomegait.ao18n.configurations.I18nConfigSection;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class I18nFactory {

	private final @Nullable I18nConfigSection i18nConfiguration;

	/**
	 * Constructs an instance of the I18nFactory class.
	 *
	 * @param configManager  the ConfigManager object used to retrieve the I18nConfigSection
	 * @throws Exception     if an error occurs during the construction of the I18nConfigSection
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
				I18nConfigSection.class
			);
	}

	/**
	 * Retrieves the I18nConfigSection object associated with this instance.
	 *
	 * @return         	the I18nConfigSection object
	 */
	public @NotNull I18nConfigSection getI18nConfiguration() {
		if (this.i18nConfiguration == null)
			throw new IllegalStateException("I18nConfigSection is null! Make sure it's correctly setup!");

		return this.i18nConfiguration;
	}
}