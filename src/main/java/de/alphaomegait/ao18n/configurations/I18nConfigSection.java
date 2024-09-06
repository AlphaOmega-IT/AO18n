package de.alphaomegait.ao18n.configurations;

import me.blvckbytes.bbconfigmapper.sections.CSAlways;
import me.blvckbytes.bbconfigmapper.sections.IConfigSection;

import java.util.*;
import java.util.stream.Collectors;

public class I18nConfigSection implements IConfigSection, I18nConfigurationProvider {

    @CSAlways
    private String defaultLocale;

    @CSAlways
    private Map<String, Object> translations;

    @Override
    public String getDefaultLocale() {
        return this.defaultLocale == null ? "en" : this.defaultLocale;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, List<String>>> getTranslations() {
        return Optional.ofNullable(this.translations)
                .map(map -> map.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> (Map<String, List<String>>) entry.getValue(),
                                (translationKey, translationValue) -> translationValue,
                                HashMap::new
                        )))
                .orElseGet(HashMap::new);
    }
}