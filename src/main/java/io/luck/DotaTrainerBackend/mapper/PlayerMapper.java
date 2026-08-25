package io.luck.DotaTrainerBackend.mapper;

import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayer;
import io.luck.DotaTrainerBackend.model.entity.Player;

public class PlayerMapper {

    public static Player toPlayer(OpenDotaPlayer openDotaPlayer){
        return  new Player(
                openDotaPlayer.profile().accountId(),
                openDotaPlayer.rankTier(),
                openDotaPlayer.leaderboardRank(),
                openDotaPlayer.computedMmr(),
                openDotaPlayer.profile().personaName(),
                openDotaPlayer.profile().name(),
                openDotaPlayer.profile().plus(),
                openDotaPlayer.profile().cheese(),
                openDotaPlayer.profile().steamId(),
                openDotaPlayer.profile().avatar(),
                openDotaPlayer.profile().avatarMedium(),
                openDotaPlayer.profile().avatarFull(),
                openDotaPlayer.profile().profileUrl(),
                openDotaPlayer.profile().lastLogin(),
                openDotaPlayer.profile().locCountryCode(),
                openDotaPlayer.profile().isContributor(),
                openDotaPlayer.profile().isSubscriber()
        );

    }

}
