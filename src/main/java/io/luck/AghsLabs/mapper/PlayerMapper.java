package io.luck.AghsLabs.mapper;

import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayer;
import io.luck.AghsLabs.model.entity.Player;

public class PlayerMapper {

    public static Player toPlayer(OpenDotaPlayer openDotaPlayer){
        return Player.builder()
                .accountId(openDotaPlayer.profile().accountId())
                .rankTier(openDotaPlayer.rankTier())
                .leaderboardRank(openDotaPlayer.leaderboardRank())
                .mmrEstimate(openDotaPlayer.computedMmr())
                .avatarImage(openDotaPlayer.profile().avatar())
                .avatarImageMedium(openDotaPlayer.profile().avatarMedium())
                .avatarImageFull(openDotaPlayer.profile().avatarFull())
                .lastLogin(openDotaPlayer.profile().lastLogin())
                .cheese(openDotaPlayer.profile().cheese())
                .name(openDotaPlayer.profile().name())
                .personaName(openDotaPlayer.profile().personaName())
                .plus(openDotaPlayer.profile().plus())
                .steamId(openDotaPlayer.profile().steamId())
                .profileURL(openDotaPlayer.profile().profileUrl())
                .locCountryCode(openDotaPlayer.profile().locCountryCode())
                .contributor(openDotaPlayer.profile().isContributor())
                .subscriber(openDotaPlayer.profile().isSubscriber())
                .build();
    }

}
