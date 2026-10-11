package io.luck.AghsLabs.model.Enums;


import lombok.Getter;

@Getter
public enum Faction {

    RADIANT(0),
    DIRE(1);

    private final int id;

    Faction(int id) {
        this.id = id;
    }

    public static Faction fromPlayerSlot(int playerSlot) {
        return playerSlot <= 127 ? RADIANT : DIRE;
    }

    public boolean won(boolean radiantWin) {
        return (this == RADIANT) == radiantWin;
    }
}
