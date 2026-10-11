package io.luck.AghsLabs.mapper;

import io.luck.AghsLabs.model.MatchDetailsData;
import io.luck.AghsLabs.model.MatchChat;
import io.luck.AghsLabs.model.MatchWards;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchDetailsData;
import java.util.List;
import java.util.function.Function;

public final class MatchDetailsDataMapper {
    private MatchDetailsDataMapper() {}

    public static <S, T> List<T> mapList(List<S> values, Function<S, T> converter) {
        return values == null ? null : values.stream().map(converter).toList();
    }

    public static MatchDetailsData.TeamfightPlayer convert(OpenDotaMatchDetailsData.TeamfightPlayer value) {
        if (value == null) return null;
        return new MatchDetailsData.TeamfightPlayer(
                value.deathsPos(),
                value.abilityUses(),
                value.abilityTargets(),
                value.itemUses(),
                value.killed(),
                value.deaths(),
                value.buybacks(),
                value.damage(),
                value.healing(),
                value.goldDelta(),
                value.xpDelta(),
                value.xpStart(),
                value.xpEnd()
        );
    }

    public static MatchDetailsData.Teamfight convert(OpenDotaMatchDetailsData.Teamfight value) {
        if (value == null) return null;
        return new MatchDetailsData.Teamfight(
                value.start(),
                value.end(),
                value.lastDeath(),
                value.deaths(),
                MatchDetailsDataMapper.mapList(value.players(), MatchDetailsDataMapper::convert)
        );
    }

    public static MatchDetailsData.Pause convert(OpenDotaMatchDetailsData.Pause value) {
        if (value == null) return null;
        return new MatchDetailsData.Pause(
                value.time(),
                value.duration()
        );
    }

    public static MatchDetailsData.Objective convert(OpenDotaMatchDetailsData.Objective value) {
        if (value == null) return null;
        return new MatchDetailsData.Objective(
                value.time(),
                value.type(),
                value.key(),
                value.slot(),
                value.playerSlot(),
                value.victimPlayerSlot(),
                value.unit(),
                value.team(),
                value.value(),
                value.killer()
        );
    }

    public static MatchChat.Chat convert(OpenDotaMatchDetailsData.Chat value, Long accountId) {
        if (value == null) return null;
        return new MatchChat.Chat(
                value.time(),
                value.type(),
                value.key(),
                value.slot(),
                value.playerSlot(),
                accountId
        );
    }

    public static MatchDetailsData.DraftTiming convert(OpenDotaMatchDetailsData.DraftTiming value) {
        if (value == null) return null;
        return new MatchDetailsData.DraftTiming(
                value.time(),
                value.type(),
                value.draftOrder(),
                value.pick(),
                value.heroId(),
                value.draftActiveTeam(),
                value.draftExtime0(),
                value.draftExtime1()
        );
    }

    public static MatchDetailsData.OdData convert(OpenDotaMatchDetailsData.OdData value) {
        if (value == null) return null;
        return new MatchDetailsData.OdData(
                value.hasApi(),
                value.hasGcData(),
                value.hasParsed(),
                value.hasArchive()
        );
    }

    public static MatchDetailsData.League convert(OpenDotaMatchDetailsData.League value) {
        if (value == null) return null;
        return new MatchDetailsData.League(
                value.leagueId(),
                value.ticket(),
                value.banner(),
                value.tier(),
                value.name()
        );
    }

    public static MatchDetailsData.TeamInfo convert(OpenDotaMatchDetailsData.TeamInfo value) {
        if (value == null) return null;
        return new MatchDetailsData.TeamInfo(
                value.teamId(),
                value.name(),
                value.tag(),
                value.logoUrl()
        );
    }

    public static MatchDetailsData.MaxHeroHit convert(OpenDotaMatchDetailsData.MaxHeroHit value) {
        if (value == null) return null;
        return new MatchDetailsData.MaxHeroHit(
                value.time(),
                value.type(),
                value.unit(),
                value.key(),
                value.value(),
                value.slot(),
                value.playerSlot(),
                value.max(),
                value.inflictor()
        );
    }

    public static MatchWards.WardLog convert(OpenDotaMatchDetailsData.WardLog value) {
        if (value == null) return null;
        return new MatchWards.WardLog(
                value.time(),
                value.type(),
                value.key(),
                value.slot(),
                value.playerSlot(),
                value.x(),
                value.y(),
                value.z(),
                value.entityLeft(),
                value.entityHandle(),
                value.attackerName()
        );
    }

    public static MatchDetailsData.PurchaseLog convert(OpenDotaMatchDetailsData.PurchaseLog value) {
        if (value == null) return null;
        return new MatchDetailsData.PurchaseLog(
                value.time(),
                value.key(),
                value.charges()
        );
    }

    public static MatchDetailsData.KillLog convert(OpenDotaMatchDetailsData.KillLog value) {
        if (value == null) return null;
        return new MatchDetailsData.KillLog(
                value.time(),
                value.key(),
                value.smoke()
        );
    }

    public static MatchDetailsData.DeathLog convert(OpenDotaMatchDetailsData.DeathLog value) {
        if (value == null) return null;
        return new MatchDetailsData.DeathLog(
                value.goldFed(),
                value.time(),
                value.goldLost(),
                value.key(),
                value.timeDead(),
                value.goldFedShared()
        );
    }

    public static MatchDetailsData.BuybackLog convert(OpenDotaMatchDetailsData.BuybackLog value) {
        if (value == null) return null;
        return new MatchDetailsData.BuybackLog(
                value.time(),
                value.type(),
                value.slot(),
                value.playerSlot()
        );
    }

    public static MatchDetailsData.RuneLog convert(OpenDotaMatchDetailsData.RuneLog value) {
        if (value == null) return null;
        return new MatchDetailsData.RuneLog(
                value.time(),
                value.key()
        );
    }

    public static MatchDetailsData.ConnectionLog convert(OpenDotaMatchDetailsData.ConnectionLog value) {
        if (value == null) return null;
        return new MatchDetailsData.ConnectionLog(
                value.time(),
                value.event(),
                value.playerSlot(),
                value.slot(),
                value.type()
        );
    }

    public static MatchDetailsData.NeutralTokenLog convert(OpenDotaMatchDetailsData.NeutralTokenLog value) {
        if (value == null) return null;
        return new MatchDetailsData.NeutralTokenLog(
                value.time(),
                value.key(),
                value.playerSlot(),
                value.slot(),
                value.type()
        );
    }

    public static MatchDetailsData.NeutralItemHistory convert(OpenDotaMatchDetailsData.NeutralItemHistory value) {
        if (value == null) return null;
        return new MatchDetailsData.NeutralItemHistory(
                value.itemNeutral(),
                value.time(),
                value.itemNeutralEnhancement()
        );
    }

    public static MatchDetailsData.PermanentBuff convert(OpenDotaMatchDetailsData.PermanentBuff value) {
        if (value == null) return null;
        return new MatchDetailsData.PermanentBuff(
                value.permanentBuff(),
                value.stackCount(),
                value.grantTime()
        );
    }

    public static MatchDetailsData.Benchmark convert(OpenDotaMatchDetailsData.Benchmark value) {
        if (value == null) return null;
        return new MatchDetailsData.Benchmark(
                value.raw(),
                value.pct()
        );
    }

    public static MatchDetailsData.Benchmarks convert(OpenDotaMatchDetailsData.Benchmarks value) {
        if (value == null) return null;
        return new MatchDetailsData.Benchmarks(
                MatchDetailsDataMapper.convert(value.goldPerMin()),
                MatchDetailsDataMapper.convert(value.xpPerMin()),
                MatchDetailsDataMapper.convert(value.killsPerMin()),
                MatchDetailsDataMapper.convert(value.deathsPerMin()),
                MatchDetailsDataMapper.convert(value.assistsPerMin()),
                MatchDetailsDataMapper.convert(value.lastHitsPerMin()),
                MatchDetailsDataMapper.convert(value.deniesPerMin()),
                MatchDetailsDataMapper.convert(value.heroDamagePerMin()),
                MatchDetailsDataMapper.convert(value.heroHealingPerMin()),
                MatchDetailsDataMapper.convert(value.towerDamage())
        );
    }

    public static MatchDetailsData.PickBan convert(OpenDotaMatchDetailsData.PickBan value) {
        if (value == null) return null;
        return new MatchDetailsData.PickBan(
                value.isPick(),
                value.heroId(),
                value.team(),
                value.order()
        );
    }
}
