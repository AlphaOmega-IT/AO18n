package de.alphaomegait.ao18n.i18n;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface II18nImpl {

	/**
	 * Sends a message.
	 *
	 */
	void sendMessage();

	/**
	 * Sends a message as a component.
	 *
	 */
	void sendMessageAsComponent();

	/**
	 *
	 * @return the to be displayed message
	 */
	String displayMessage();

	/**
	 *
	 * @return the to be displayed message as a component
	 */
	List<String> displayMessages();

	/**
	 *
	 * @return the to be displayed message as a component
	 */
	Component displayMessageAsComponent();

	/**
	 *
	 * @return the prefix key
	 */
	String getPrefix();

	/**
	 *
	 * @return the messages from a message key
	 */
	List<String> getMessagesFromKey();

	/**
	 *
	 * @return the prefix message using the prefix key
	 */
	List<String> getPrefixMessage();

	/**
	 *
	 * @return the messages with arguments and a possible prefix
	 */
	List<String> getMessagesWithArgumentsAndPrefix();

	/**
	 *
	 * @return the message with arguments
	 */
	String getMessageWithArguments();

	/**
	 *
	 * @return the completed message after converting
	 */
	String getJoinedMessage();

	/**
	 *
	 * @return the plain message of a message key
	 */
	String getMessage();
}