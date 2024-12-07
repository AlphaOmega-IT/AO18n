package de.alphaomegait.ao18n.i18n;

import de.alphaomegait.ao18n.AO18n;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class I18n implements II18nImpl {

	private static final String PREFIX_KEY = "prefix";

	private final Map<String, String> placeholders;
	private final String key;
	private final Player player;
	private final boolean includePrefix;
	private final List<Component> appendedComponents;

	private I18n(Builder builder) {
		this.placeholders = builder.placeholders;
		this.key = builder.key;
		this.player = builder.player;
		this.includePrefix = builder.includePrefix;
		this.appendedComponents = builder.appendedComponents;
	}

	@Override
	public void sendMessage() {
		player.sendMessage(getFormattedMessage());
	}

	@Override
	public void sendMessageAsComponent() {
		player.sendMessage(MiniMessage.miniMessage().deserialize(getFormattedMessage()));
	}

	@Override
	public void sendMessagesAsComponent() {
		Arrays.stream(getFormattedMessage().split("<newline>"))
				.forEach(message -> player.sendMessage(MiniMessage.miniMessage().deserialize(message)));
	}

	@Override
	public String displayMessage() {
		return getFormattedMessage();
	}

	@Override
	public List<String> displayMessages() {
		return Arrays.asList(getFormattedMessage().split("<newline>"));
	}

	@Override
	public Component displayMessageAsComponent() {
		Component component = MiniMessage.miniMessage().deserialize(getFormattedMessage());
		for (Component extraComponent : appendedComponents) {
			component = component.append(extraComponent);
		}
		return component;
	}

	@Override
	public String getPrefix() {
		return String.join("<newline>", getRawMessages(PREFIX_KEY));
	}

	@Override
	public List<String> getMessagesFromKey() {
		return getRawMessages(this.key);
	}

	@Override
	public List<String> getPrefixMessage() {
		return getRawMessages(PREFIX_KEY);
	}

	@Override
	public List<String> getMessagesWithArgumentsAndPrefix() {
		List<String> messages = getMessagesFromKey();
		if (includePrefix) {
			List<String> prefixMessages = getPrefixMessage();
			messages.addAll(0, prefixMessages);
		}
		return replacePlaceholdersInMessages(messages);
	}

	@Override
	public String getMessageWithArguments() {
		String message = String.join("<newline>", getMessagesFromKey());
		if (includePrefix) {
			message = getPrefix() + " " + message;
		}
		return replacePlaceholders(message);
	}

	@Override
	public String getJoinedMessage() {
		return getFormattedMessage();
	}

	@Override
	public String getMessage() {
		return String.join("<newline>", getMessagesFromKey());
	}

	private String getFormattedMessage() {
		String message = getMessage();
		if (includePrefix) {
			message = getPrefix() + " " + message;
		}
		return replacePlaceholders(message);
	}

	private List<String> getRawMessages(String messageKey) {
		Map<String, Map<String, List<String>>> translations = AO18n.getTranslations();
		String locale = player.locale().getLanguage();

		// Try to get messages for the player's locale
		List<String> messages = Optional.ofNullable(translations.get(messageKey))
				                        .map(localeMap -> localeMap.get(locale))
				                        .orElse(null);

		// Fallback to default locale
		if (messages == null || messages.isEmpty()) {
			messages = Optional.ofNullable(translations.get(messageKey))
					           .map(localeMap -> localeMap.get(AO18n.getDefaultLocale()))
					           .orElse(null);
		}

		// Fallback to missing message
		if (messages == null || messages.isEmpty()) {
			messages = List.of("<gold>Message key</gold> <red>'" + messageKey + "'</red> <gold>is missing!</gold>");
		}

		return messages;
	}

	private List<String> replacePlaceholdersInMessages(List<String> messages) {
		List<String> formattedMessages = new ArrayList<>();
		for (String message : messages) {
			formattedMessages.add(replacePlaceholders(message));
		}
		return formattedMessages;
	}

	private String replacePlaceholders(String message) {
		for (Map.Entry<String, String> entry : placeholders.entrySet()) {
			String placeholder = "{" + entry.getKey() + "}";
			message = message.replace(placeholder, entry.getValue());
		}
		return message;
	}

	public static class Builder {

		private final Map<String, String> placeholders = new HashMap<>();
		private final List<Component> appendedComponents = new ArrayList<>();
		private final Player player;
		private String key;
		private boolean includePrefix = false;

		public Builder(@NotNull String key, @NotNull Player player) {
			this.key = key;
			this.player = player;
		}

		public Builder includePrefix(boolean includePrefix) {
			this.includePrefix = includePrefix;
			return this;
		}

		public Builder setKey(@NotNull String key) {
			this.key = key;
			return this;
		}

		public Builder appendComponents(Component... components) {
			Collections.addAll(this.appendedComponents, components);
			return this;
		}

		public Builder withPlaceholders(@NotNull Map<String, Object> placeholders) {
			for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
				String value = entry.getValue() != null ? entry.getValue().toString() : "<null>";
				this.placeholders.put(entry.getKey(), value);
			}
			return this;
		}

		public Builder withPlaceholder(@NotNull String key, @NotNull Object value) {
			this.placeholders.put(key, value.toString());
			return this;
		}

		public I18n build() {
			return new I18n(this);
		}
	}
}