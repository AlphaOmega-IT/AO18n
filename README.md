# AO18n

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![GitHub issues](https://img.shields.io/github/issues/AlphaOmega-IT/AO18n.svg)](https://github.com/AlphaOmega-IT/AO18n/issues)
[![GitHub stars](https://img.shields.io/github/stars/AlphaOmega-IT/AO18n.svg)](https://github.com/AlphaOmega-IT/AO18n/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/AlphaOmega-IT/AO18n.svg)](https://github.com/AlphaOmega-IT/AO18n/network)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/AlphaOmega-IT/AO18n.svg)](https://github.com/AlphaOmega-IT/AO18n/pulls)

## Overview

AO18n is a powerful internationalization library for Minecraft [Spigot/Paper] Servers. 
It helps you manage translations and localization in your application efficiently.
You can provide as many localizations as you want! 
If a message key is missing it will lead back to the fallback language, so you do not have to worry at all about adding a new translation in time!

## Features

- **Simple Integration:** Easily integrate AO18n into your project with minimal setup.
- **Pluralization Support:** Handle pluralization effortlessly for different languages.
- **Dynamic Loading:** Dynamically load translations based on user preferences or other criteria.
- **Customizable:** Customize and extend AO18n to suit your specific localization needs.

## Getting Started

To integrate AO18n into your Java project using Maven, follow these simple steps:

### Step 1: Add AO18n as a Dependency
Add the following dependency to your project's `pom.xml` file:

```xml
<dependencies>
    <dependency>
        <groupId>de.alphaomega-it.ao18n</groupId>
        <artifactId>AO18n</artifactId>
        <version>2.3</version>
    </dependency>
</dependencies>
```

### Step 2: Initialize AO18n in Your JavaPlugin class
Add the following method 'onLoad' to your class
```java
import com.alphaomegait.ao18n.AO18n;

public class Plugin extends JavaPlugin {

  //Called when the plugin is being loaded.
  @Override
  public void onLoad() {

    // argument 1: class which does extend the JavaPlugin
    // argument 2: if the language file should be replaced in the plugin folder
    new AO18n(this, false);
  }
}
```

### Step 2.1: Use it for Internationalization
Since we initialized the Dependency you can use the classes as long
the .yml format of the i18n.yml file is correct.
```java
import de.alphaomegait.ao18n.I18n;
import org.bukkit.entity.Player;

// can be whatever class you want
public class Example {

  //Send the player a message based on the message-key in your i18n.yml file
  //It takes if available the player client language, if the language does not
  //exist. It will take the fallback/default language key.
  //.build().sendMessage() will send a message as string.
  //.build().sendMessageAsComponent() will send a message using the https://docs.advntr.dev/minimessage/index.html dependency as a Text Component
  public void sendHelloWorldMessage(
    final Player player
  ) {
    new I18n.Builder(
      "message-key",
      player
    ).hasPrefix(true)
     .build()
     .sendMessageAsComponent();
  }
}
```

### Step 3: Configure Translations
Create a translation file in the appropriate directory. By default, AO18n looks for the translation file in the src/main/resources/translations directory. Here's an example structure:
```css
src
└── main
    └── resources
        └── translations
            ├── i18n.yml
```

### Step 3.1: Fill the Translation File
Now you have to fill in the missing Language keys and Locales in the right format like below:
```yaml
# This file contains the configuration settings for internationalization (i18n).
# Please make sure to update the following values according to your requirements.

# See https://wiki.atwoo.eu for more information.

defaultLocale: 'en'
translations:
  #Prefix has to be a key to make the function #showPrefix work
  prefix:
    de:
      - '<gray>Dies ist eine Nachricht</gray>'
    en:
      - '<gray>This is a message</gray>'
  message_key_2:
    de:
      - '<gray>Dies ist eine Nachricht 2</gray>'
      - '<gray>Dies ist eine Nachricht 3</gray>'
    en:
      - '<gray>This is a message 2</gray>'
      - '<gray>This is a message 3</gray>'
  message_key_3:
    de:
      - '<gray>Dies ist eine Nachricht 4</gray>'
      - '<gray>Dies ist eine Nachricht mit einem Platzhalter {0}</gray>'
    en:
      - '<gray>This is a message 4</gray>'
      - '<gray>This is a message with a placeholder {0}</gray>'
  message_key_4:
    de:
      - '<gray>{0}, {1}, {2} etc. Platzhalter...</gray>'
    en:
      - '<gray>{0}, {1}, {2} etc. Placeholder...</gray>'
```

# Contributing to AO18n

We welcome contributions to AO18n! If you would like to contribute, please follow these guidelines:

1. Fork the repository and clone it locally.
2. Create a new branch for your feature or bug fix.
3. Make your changes and ensure they are well-tested.
4. Submit a pull request with a clear description of your changes.

Thank you for contributing to AO18n!

# Special Thanks

We extend our heartfelt gratitude to BlvckBytes for his incredible work on any of me.blvckbytes projects. Our project benefits greatly from the functionality and reliability provided by AutoWirer, GPEEE, BBConfigMapper, and much more. 
We want to express our appreciation for the effort and dedication put into maintaining such a valuable tool.

If you find AO18n useful, please consider supporting and thanking AlphaOmega-IT, BlvckBytes for their hard work. Here are ways to connect with them:

- GitHub: [BlvckBytes GitHub Profile](https://github.com/BlvckBytes)
- GitHub: [Author's GitHub Profile](https://github.com/AlphaOmega-IT)

Thank you, BlvckBytes, for your outstanding contributions!


# Support

If you have any questions, or issues, or need assistance, feel free to reach out:

- Open an [issue](https://github.com/AlphaOmega-IT/AO18n/issues)
- Join our [community forums](https://discord.gg/Jq5CAUEDWB)
- Email us at services@alphaomega-it.de

We appreciate your feedback and will do our best to assist you!
