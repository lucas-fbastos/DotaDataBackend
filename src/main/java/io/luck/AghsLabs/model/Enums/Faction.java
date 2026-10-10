package io.luck.AghsLabs.model.Enums;


public enum Faction {

    RADIANT,
    DIRE;

    public static Faction fromPlayerSlot(int playerSlot) {
        return playerSlot <= 127 ? RADIANT : DIRE;
    }

    public boolean won(boolean radiantWin) {
        return (this == RADIANT) == radiantWin;
    }
}