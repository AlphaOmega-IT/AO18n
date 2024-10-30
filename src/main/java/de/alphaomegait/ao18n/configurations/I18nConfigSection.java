package de.alphaomegait.ao18n.configurations;

import me.blvckbytes.bbconfigmapper.sections.AConfigSection;
import me.blvckbytes.bbconfigmapper.sections.CSAlways;
import me.blvckbytes.gpeee.interpreter.EvaluationEnvironmentBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CSAlways
public class I18nConfigSection extends AConfigSection {

    private String defaultLocale;
    private Map<String, Object> translations;

    public I18nConfigSection(EvaluationEnvironmentBuilder baseEnvironment) {
        super(baseEnvironment);
    }

    public String getDefaultLocale() {
        return this.defaultLocale == null ? "en" : this.defaultLocale;
    }

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