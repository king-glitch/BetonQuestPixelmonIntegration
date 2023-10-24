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
        protected String prefix = "&8[&cPaletteTokens&8]&8:&7 ";
        @Setting(value = "cant-use-on-this-pokemon")
        @Comment("Message when apply token on wrong pokemon\n" + "variables:\n" + " - {palette}: palette name\n" + " - {pokemon}: pokemon name")
        protected String cantUseOnThisPokemon = "&cThis token can't use on this pokemon. Can only use on &4&l{pokemon}&c";
        @Setting(value = "this-pokemon-doesnt-have-this-palette")
        @Comment("Message when apply token on pokemon that doesn't have this palette\n" + "variables:\n" + " - {palette}: palette name\n" + " - {pokemon}: pokemon name")
        protected String thisPokemonDoesntHaveThisPalette = "&cYou can't apply this &4&l{palette}&c on this &4&l{pokemon}";
        @Setting(value = "pokemon-already-has-texture")
        @Comment("Message when apply token key with same pokemon with the same texture")
        protected String pokemonAlreadyHasTexture = "&cThis pokemon already has this texture!";
        @Setting(value = "successfully-apply-texture-on-pokemon")
        @Comment("Message when apply token successfully\n+" + "variables:\n" + " - {palette}: palette name\n" + " - {pokemon}: pokemon name")
        protected String successfullyApplyTextureOnPokemon = "&aSuccessfully apply &2&l{palette}&a token on &2&l{pokemon}";
        @Setting(value = "successfully-send-token-to-player")
        @Comment("message when send token to player\n" + "variables:\n" + " - {pokemon}: pokemon name\n" + " - {palette}: palette name\n" + " - {player}: player name")
        protected String successfullySendTokenToPlayer = "&aYou send &2&l{pokemon} &7- &a{palette} token to &2&l{player}";
        @Setting(value = "successfully-retrieve-token-from-player")
        @Comment("message when retrieve token from player\n" + "variables:\n" + " - {pokemon}: pokemon name\n" + " - {palette}: palette name")
        protected String successfullyRetrieveToken = "&aYou retrieve &2&l{pokemon} &7- &a{palette} token.";
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
        @Comment("Message player is not online or invalid\n" + "variables:\n" + " - {player}: player name")
        protected String playerIsNotOnlineNorInvalid = "&cPlayer &4&l{player} &cis not online or invalid";
        @Setting(value = "cant-use-on-shiny-pokemon")
        @Comment("Message when apply token on shiny pokemon")
        protected String cantUseOnShinyPokemon = "&cThis pokemon is shiny, you can't apply this texture to a shiny";
        @Setting(value = "not-pokemon-owner")
        @Comment("Message when player is not pokemon owner")
        protected String notPokemonOwner = "&cYou're not the owner of this pokemon";
    }
}

