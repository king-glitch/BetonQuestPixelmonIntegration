package dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Getter
@ConfigSerializable
public class LanguageConfig {

    @Setting(value = "general")
    @Comment("General Settings")
    private final GeneralConfig generalConfig = new GeneralConfig();

    @Getter
    @ConfigSerializable
    public static class GeneralConfig {
        @Setting(value = "prefix")
        @Comment("message prefix")
        protected String prefix = "&8[&cRachamonTextureTokens&8]&8:&7 ";
        @Setting(value = "pokemon-doesnt-have-texture")
        @Comment("Message when apply token on wrong pokemon\n" + "variables:\n" + " - {token}: token name\n" + " - {pokemon}: pokemon name")
        protected String pokemonDoesntHaveTexture = "&cYou can't apply this &4&l{token}&c on this &4&l{pokemon}";
        @Setting(value = "pokemon-already-has-texture")
        @Comment("Message when apply token key with same pokemon with the same texture")
        protected String pokemonAlreadyHasTexture = "&cThis pokemon already has this texture!";
        @Setting(value = "successfully-apply-texture-on-pokemon")
        @Comment("Message when apply token successfully\n+" + "variables:\n" + " - {token}: token name\n" + " - {pokemon}: pokemon name")
        protected String successfullyApplyTextureOnPokemon = "&aSuccessfully apply &2&l{token}&a token on &2&l{pokemon}";
        @Setting(value = "successfully-retrieve-token")
        @Comment("message when retrieved token\n" + "variables:\n" + " - {token}: token name")
        protected String successfullyRetrieveToken = "&aYou successfully retrieved &2&l{token}&a token!";
        @Setting(value = "invalid-token-usage")
        @Comment("Message token was wrongly used.")
        protected String invalidTokenUsage = "&cInvalid Token Usage. This Token can't use on this pokemon.";
        @Setting(value = "invalid-token-key")
        @Comment("Message wrong token key")
        protected String invalidTokenKey = "&cInvalid token key, please check the config key name";
        @Setting(value = "token-no-longer-valid")
        @Comment("Message when token is invalid")
        protected String tokenNoLongerValid = "&cThis token is no longer valid";
        @Setting(value = "player-is-not-online-or-invalid")
        @Comment("Message player is not online or invalid")
        protected String playerIsNotOnlineNorInvalid = "&cInvalid Player, please make sure they're online.";
        @Setting(value = "cant-use-on-shiny-pokemon")
        @Comment("Message when apply token on shiny pokemon")
        protected String cantUseOnShinyPokemon = "&cThis pokemon is shiny, you can't apply this texture to a shiny";
    }
}

