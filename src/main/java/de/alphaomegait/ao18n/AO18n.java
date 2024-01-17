package de.alphaomegait.ao18n;

import me.blvckbytes.autowirer.AutoWirer;
import me.blvckbytes.bukkitboilerplate.PluginFileHandler;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AO18n implements IAO18nProvider {

	private final Logger logger = Logger.getLogger(AO18n.class.getName());
	private final AutoWirer autoWirer = new AutoWirer();

	private final JavaPlugin loadedPlugin;

	private static @Nullable I18nFactory i18nFactory;

	public AO18n(
		final @NotNull JavaPlugin loadedPlugin,
		final boolean forceReplacement
	) {
		this.loadedPlugin = loadedPlugin;
		this.initialize(forceReplacement);

		i18nFactory = this.autoWirer.findInstance(I18nFactory.class).orElseThrow(() -> new IllegalStateException("Could not find I18nFactory!"));
	}

	@Override
	public void initialize(
		final boolean forceReplacement
	) {
		final long beginTimestamp = System.nanoTime();

		// Create the plugin folder if it doesn't exist
		if (this.loadedPlugin.getDataFolder().mkdir()) {
			this.logger.info("Plugin folder created.");
		}

		// Create config files
		Arrays.stream(this.getConfigPaths()).toList().forEach(
			configPath -> {
				this.loadedPlugin.saveResource(
					configPath,
					forceReplacement
				);
				this.logger.info("Config file created: " + configPath);
			});

		this.autoWirer
			.addExistingSingleton(this)
			.addExistingSingleton(this.logger)
			.addExistingSingleton(this.loadedPlugin)
			.addSingleton(ConfigManager.class)
			.addSingleton(PluginFileHandler.class)
			.addSingleton(I18nFactory.class)
			.onException(exception -> {
				this.logger.log(
					Level.SEVERE,
					"An exception occurred while loading the plugin: " + exception,
					exception
				);
			})
			.wire(success -> {
				// Log the number of classes loaded and the time taken for wiring
				this.logger.info(
					"Successfully loaded " + success.getInstancesCount() + " classes (" + ((System.nanoTime() - beginTimestamp) / 1000 / 1000) + "ms)"
				);
			});

		this.logger.info("I18nFactory initialized.");
	}

	@Override
	public String[] getConfigPaths() {
		return new String[] {
			"translations/i18n.yml"
		};
	}

	public static @NotNull I18nFactory getI18nFactory() {
		if (i18nFactory == null)
			throw new IllegalStateException("I18nFactory is null! Make sure it's correctly setup!");
		return i18nFactory;
	}
}