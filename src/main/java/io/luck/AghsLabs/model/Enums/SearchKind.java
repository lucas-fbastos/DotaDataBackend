package io.luck.AghsLabs.model.Enums;

import io.luck.AghsLabs.exception.BadRequestException;

import java.util.Locale;
import java.util.Objects;

public enum SearchKind {
    ANY,
    PRO;

    public static SearchKind parse(String value) {
        String normalized = Objects.toString(value, "any").trim().toUpperCase(Locale.ROOT);
        try {
            return SearchKind.valueOf(normalized);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Query parameter 'kind' must be either 'any' or 'pro'");
        }
    }
}