package io.luck.AghsLabs.model;

import java.util.List;

public record MatchWards(List<PlayerWards> players) {
    public record PlayerWards(
            Integer playerSlot,
            Long accountId,
            Long heroId,
            List<WardLog> obsLog,
            List<WardLog> senLog,
            List<WardLog> obsLeftLog,
            List<WardLog> senLeftLog
    ) {}

    public record WardLog(
            Integer time,
            String type,
            String key,
            Integer slot,
            Integer playerSlot,
            Double x,
            Double y,
            Double z,
            Boolean entityLeft,
            Integer entityHandle,
            String attackerName
    ) {}
}
