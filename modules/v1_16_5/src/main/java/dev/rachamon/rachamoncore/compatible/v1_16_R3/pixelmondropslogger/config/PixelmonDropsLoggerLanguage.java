package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Getter
public class PixelmonDropsLoggerLanguage {

	@Setting(value = "general")
	@Comment("General Section")
	private final GeneralConfig generalConfig = new GeneralConfig();

	@ConfigSerializable
	@Getter
	public static class GeneralConfig {
		@Setting(value = "prefix")
		@Comment("message prefix")
		protected String prefix = "&8[&cPixelmonDropsLogger&8]&8:&7 ";

		@Setting("player-drop-message")
		@Comment("Message when player drop item\n" + "variables:\n" + " - {id}: drop id\n" + " - {amount}: drop amount")
		protected String playerDropMessage = "&7You defeated pokemon with id &c{id}&7. if you didn't get the drops, please contact the server administrator";

		@Setting("player-not-online")
		@Comment("Message when player is not online\n" + "variables:\n" + " - {player}: player name")
		protected String playerNotOnline = "&7Player &c{player}&7 is not online";

		@Setting("player-received-drops")
		@Comment("Message when player received drops\n" + "variables:\n" + " - {id}: drop id")
		protected String playerReceivedDrops = "&7You received drops with id &c{id}";

		@Setting("give-command")
		@Comment("Give command message\n" + "variables:\n" + " - {id}: drop id\n" + " - {player}: player name")
		protected String giveCommand = "&7You gave drops with id &c{id}&7 to player &c{player}";


	}
}

