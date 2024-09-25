package de.alphaomegait.ao18n.i18n;

import de.alphaomegait.ao18n.AO18n;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The `I18n` class is responsible for internationalization in the application.
 * It implements the `II18nImpl` interface and provides methods for sending messages
 * to players in different languages, retrieving messages with arguments and prefix,
 * and handling translations.
 * <p>
 * This class uses a builder pattern for constructing instances of `I18n`.
 * To create a new instance, use the `Builder` class and its methods to set the
 * necessary parameters, and then call the `build()` method to create the `I18n` object.
 * <p>
 * Example usage:
 * ```java
 * I18n i18n = new I18n.Builder()
 *   .setArguments(arguments)
 *   .setKey(key)
 *   .setPlayer(player)
 *   .setHasPrefix(hasPrefix)
 *   .setHasPlaceholder(hasPlaceholder)
 *   .build();
 * ```
 * <p>
 * The `I18n` class provides the following functionality:
 * - Sending messages to players
 * - Sending messages as components
 * - Displaying joined messages
 * - Displaying multiple messages with arguments and prefix
 * - Displaying joined messages as components
 * - Retrieving the prefix message
 * - Retrieving messages associated with a key
 * - Retrieving prefix messages based on the given prefix
 * <p>
 * Note: This class assumes the existence of an `I18nFactory` class for managing translations.
 * Make sure to configure the translations map properly in the `I18nFactory` class.
 * <p>
 * For more information on how to use the `I18n` class, refer to the documentation of each method.
 */
public class I18n implements II18nImpl {

	private final static String PREFIX = "prefix";

	private final List<Component> componentsToAppend;
	private final Map<Integer, String> arguments;
	private final String key;
	private final Player player;
	private final boolean hasPrefix;
	private final boolean hasPlaceholder;

	private I18n(
		final @NotNull Builder builder
	) {
		this.arguments = builder.arguments;
		this.key = builder.key;
		this.player = builder.player;
		this.hasPrefix = builder.hasPrefix;
		this.hasPlaceholder = builder.hasPlaceholder;
		this.componentsToAppend = builder.existingTextToAppendAtTheEnd;
	}

	/**
	 * Sends a message to the player.
	 */
	@Override
	public void sendMessage() {
		this.player.sendMessage(this.getJoinedMessage());
	}

	/**
	 * Sends a message as a component to the player.
	 */
	@Override
	public void sendMessageAsComponent() {
		this.player.sendMessage(MiniMessage.miniMessage().deserialize(this.getJoinedMessage()));
	}
	
	@Override
	public void sendMessagesAsComponent() {
		Arrays.stream(this.getJoinedMessage().split("<newline>")).toList().forEach(message -> {
				player.sendMessage(MiniMessage.miniMessage().deserialize(message).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
			}
		);
	}
	
	/**
	 * Returns the joined message.
	 *
	 * @return the joined message
	 */
	@Override
	public String displayMessage() {
		return this.getJoinedMessage();
	}
	/**
	 * Retrieves and displays the messages with arguments and prefix.
	 *
	 * @return the list of messages
	 */
	@Override
	public List<String> displayMessages() {
		return this.getMessagesWithArgumentsAndPrefix();
	}
	/**
	 * Returns the joined message as a Component.
	 *
	 * @return the joined message as a Component
	 */
	@Override
	public Component displayMessageAsComponent() {
		Component displayComponent = MiniMessage.miniMessage().deserialize(this.getJoinedMessage());

		if (
			! this.componentsToAppend.isEmpty()
		) {
			for (
				final Component component : this.componentsToAppend
			) {
				displayComponent = displayComponent.append(component);
			}
		}

		return displayComponent;
	}

	/**
	 * Returns the prefix message.
	 *
	 * @return The prefix message.
	 */
	@Override
	public String getPrefix() {
		return String.join("<newline>", this.getPrefixMessage());
	}

	/**
	 * Retrieves the messages associated with the given key.
	 * If the key is not found in the translations map, a default error message is returned.
	 *
	 * @return A list of messages associated with the key, or a default error message if the key is not found.
	 */
	@Override
	public List<String> getMessagesFromKey() {
		// Error message when the key is not found in the translations map
		final String keyNotFound = "<gold>message key</gold> <red>'" + this.key + "'</red> <gold>is missing in i18n.yml!</gold>";

		// Get the translations map from the I18nFactory
		final Map<String, Map<String, List<String>>> translations = AO18n.getTranslations();

		// Check if the translations map contains the prefix
		if (
			!translations.containsKey(this.key)
		) return List.of(keyNotFound);

		// Check if the translations map contains the key for the player's locale
		if (
			translations.get(this.key).containsKey(this.player.locale().getLanguage())
		) return translations.get(this.key).get(this.player.locale().getLanguage());

		// Check if the translations map contains the key for the default locale
		if (
			translations.get(this.key).containsKey(AO18n.getDefaultLocale())
		) return translations.get(this.key).get(AO18n.getDefaultLocale());

		// Return the default error message
		return List.of(keyNotFound);
	}

	/**
	 * Returns a list of prefix messages based on the given prefix.
	 * If the prefix is not found in the translations, a default error message is returned.
	 * The messages are fetched based on the player's locale, or the default locale if the player's locale is not found.
	 *
	 * @return a list of prefix messages
	 */
	@Override
	public List<String> getPrefixMessage() {
		// Error message when the key is not found in the translations
		final String keyNotFound = "<gold>message key</gold> <red>'" + PREFIX + "'</red> <gold>is missing in i18n.yml!</gold>";

		// Get the translations map
		final Map<String, Map<String, List<String>>> translations = AO18n.getTranslations();

		// Check if the prefix exists in the translations
		if (
			!translations.containsKey(PREFIX)
		) return List.of(keyNotFound);

		// Fetch the messages based on the player's locale
		Map<String, List<String>> prefixTranslations = translations.get(PREFIX);
		String playerLocale = this.player.locale().getLanguage();
		if (
			prefixTranslations.containsKey(playerLocale)
		) return prefixTranslations.get(playerLocale);

		// Fetch the messages based on the default locale
		if (
			prefixTranslations.containsKey(AO18n.getDefaultLocale())
		) return prefixTranslations.get(AO18n.getDefaultLocale());

		// Return the default error message
		return List.of(keyNotFound);
	}

	/**
	 * Retrieves a list of messages with arguments and prefix.
	 * @return The list of messages.
	 */
	@Override
	public @NotNull List<String> getMessagesWithArgumentsAndPrefix() {
		final List<String> messages = new ArrayList<>();

		// Retrieve messages from the key
		List<String> keyMessages = this.getMessagesFromKey();

		// Iterate over each message line
		for (
			String messageLine : keyMessages
		) {
			// Add prefix to the message line if prefix is present
			if (
				this.hasPrefix
			) messageLine = this.getPrefix() + " " + messageLine;

			// Skip message line if placeholder is not present
			if (
				! this.hasPlaceholder
			) {
				messages.add(messageLine);
				continue;
			}

			// Replace arguments in the message line and add it to the list of messages
			messages.add(this.replaceArguments(messageLine));
		}

		return messages;
	}

	/**
	 * Returns the message with arguments.
	 * If the prefix is present, it appends the prefix to the message.
	 * Then, it replaces any placeholders in the message with the actual arguments.
	 *
	 * @return the message with arguments
	 */
	@Override
	public String getMessageWithArguments() {
		String message = this.hasPrefix ? this.getPrefix() + " " + this.getMessage() : this.getMessage();
		return this.replaceArguments(message);
	}

	/**
	 * Returns the joined message based on the conditions.
	 *
	 * @return the joined message
	 */
	@Override
	public String getJoinedMessage() {
		// Case 1: When there is a prefix and no placeholder
		if (
			this.hasPrefix && !this.hasPlaceholder
		) return this.getPrefix() + " " + this.getMessage();

		// Case 2: When there is no prefix and no placeholder
		if (
			!this.hasPrefix && !this.hasPlaceholder
		) return this.getMessage();

		// Case 3: When there is a placeholder
		return this.getMessageWithArguments();
	}

	/**
	 * Replaces placeholders in the message line with arguments provided.
	 *
	 * @param messageLine The message line containing placeholders.
	 * @return The message line with replaced placeholders.
	 */
	private @NotNull String replaceArguments(@NotNull String messageLine) {
		StringBuilder replacedMessage = new StringBuilder(messageLine);
		final Iterator<String> iterator = this.arguments.values().iterator();
		int i = 0;
		while (
			iterator.hasNext()
		) {
			try {
				final String string = iterator.next();

				// Replace the placeholder with argument value
				replacedMessage.replace(replacedMessage.indexOf("{" + i + "}"), replacedMessage.indexOf("{" + i + "}") + 3, string);
				i++;
			} catch (
				final Exception ignored
			) {
				break;
			}
		}

		return replacedMessage.toString();
	}

	/**
	 * Returns a formatted message by joining the messages from a key.
	 *
	 * @return The formatted message.
	 */
	@Override
	public String getMessage() {
		return String.join("<newline>", this.getMessagesFromKey());
	}

	public static class Builder {

		private final Map<Integer, String> arguments = new LinkedHashMap<>();
		private final List<Component> existingTextToAppendAtTheEnd = new ArrayList<>();
		private       String               key;
		private final Player               player;
		private boolean hasPrefix;
		private boolean hasPlaceholder;

		public Builder(
			final @NotNull String key,
			final @NotNull Player player
		) {
            this.key = key;
			this.player = player;
		}

		/**
		 * Set whether the Builder has a prefix.
		 *
		 * @param  hasPrefix  whether the Builder has a prefix
		 * @return                 the current instance of the `Builder` class
		 */
		public Builder hasPrefix(
			final boolean hasPrefix
		) {
			this.hasPrefix = hasPrefix;
			return this;
		}

		/**
		 * Sets the new key for the 'key' property.
		 *
		 * @param key the new key for the 'key' property
		 * @return the current instance of the `Builder` class
		 */
		public Builder setKey(
			final @NotNull String key
		) {
			this.key = key;
			return this;
		}

		/**
		 * Sets the value of the `hasPlaceholder` property.
		 *
		 * @param  hasPlaceholder  the new value for the `hasPlaceholder` property
		 * @return                 the current instance of the `Builder` class
		 */
		public Builder hasPlaceholder(
			final boolean hasPlaceholder
		) {
			this.hasPlaceholder = hasPlaceholder;
			return this;
		}

		public Builder append(
			final Component... components
		) {
			this.existingTextToAppendAtTheEnd.addAll(List.of(components));
			return this;
		}

		/**
		 * Sets the value of the `arguments` property.
		 *
		 * @param  arguments  the new value for the `arguments` property
		 * @return            the current instance of the `Builder` class
		 */
		public Builder setArgs(
			final @Nullable Object... arguments
		) {
			if (
				arguments == null
			) return this;

			this.hasPlaceholder = true;
			for (
				Object argument : arguments
			) this.arguments.put(this.arguments.size(), argument == null ? "<null>" : argument.toString());
			return this;
		}

		/**
		 * 
		 * @return the new instance of the `I18n` class
		 */
		public I18n build() {
			return new I18n(
				this
			);
		}
	}
}