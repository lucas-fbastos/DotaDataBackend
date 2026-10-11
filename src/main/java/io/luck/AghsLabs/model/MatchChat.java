package io.luck.AghsLabs.model;

import java.util.List;
import java.util.Map;

public record MatchChat(
        List<Chat> chat,
        Map<String, Long> allWordCounts,
        Map<String, Long> myWordCounts
) {
    public record Chat(
            Integer time,
            String type,
            String key,
            Integer slot,
            Integer playerSlot,
            Long accountId
    ) {}
}
