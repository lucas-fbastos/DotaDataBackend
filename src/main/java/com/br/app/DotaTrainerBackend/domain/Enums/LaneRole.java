package com.br.app.DotaTrainerBackend.domain.Enums;

public enum LaneRole {

    CARRY(1),
    NUKER(2),
    INITIATOR(3),
    DISABLER(4),
    DURABLE(5),
    ESCAPE(6),
    SUPPORT(7),
    PUSHER(8);

    final int id;

    LaneRole(int id) {
        this.id = id;
    }
}
