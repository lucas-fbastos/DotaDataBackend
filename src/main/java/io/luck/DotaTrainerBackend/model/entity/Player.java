package io.luck.DotaTrainerBackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Builder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name="player", schema ="public")
public class Player {

    @Id
    private Long accountId;
    private Integer soloRank;
    private Integer rankTier;
    private Integer competitiveRank;
    private Integer leaderboardRank;
    private Integer mmrEstimate;
    private String personaName;
    private String name;
    private boolean plus;
    private Integer cheese;

    private String steamId;

    private String avatarImage;

    private String avatarImageMedium;

    private String avatarImageFull;

    private String profileURL;

    private String lastLogin;

    private String locCountryCode;

    private Boolean contributor;

    private Boolean subscriber;

}
