package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ConfigSerializable
@Getter
public class PixelmonDropsLoggerLog {
	@Setting(value = "data")
	@Comment("Data")
	protected Map<String, Data> dataConfig = new HashMap<>();

	@ConfigSerializable
	@Getter
	public static class Data {
		@Setting(value = "date")
		@Comment("Date")
		protected String date = "";

		@Setting(value = "drops")
		@Comment("Drops")
		protected Map<String, Integer> drops = new HashMap<>();

		@Setting(value = "username")
		@Comment("Username")
		protected String username = "";

	}

	public void addDrops(String id, String username, Map<String, Integer> drops) {


		Data data = new Data();
		data.date = new Date().toString();
		data.drops = drops;
		data.username = username;

		dataConfig.put(id, data);

	}

	public void removeDrops(String id) {
		if (dataConfig.get(id) == null) {
			return;
		}

		dataConfig.remove(id);
	}

}