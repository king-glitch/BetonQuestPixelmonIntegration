package dev.rachamon.rachamoncore.api.chat;

import dev.rachamon.rachamoncore.api.utils.ColorUtil;
import dev.rachamon.rachamoncore.api.version.MCVersion;
import lombok.Getter;

import java.awt.*;

@Getter
public class ColorContainer {
	private ColorCode colorCode;
	private String hexCode;

	public ColorContainer(ColorCode colorCode) {
		this.colorCode = colorCode;
		this.hexCode = null;
	}

	public ColorContainer(String hexCode, boolean noHex) {
		this.hexCode = hexCode;

		if (noHex || MCVersion.isLower(MCVersion.v1_16.getDetailedVersion())) {
			this.colorCode = getColor();
			this.hexCode = null;
		}
	}

	public ColorCode getColor() {
		if (colorCode != null) {
			return colorCode;
		}

		if (hexCode == null) {
			return null;
		}

		java.awt.Color jColor = new Color(
				Integer.valueOf(hexCode.substring(0, 2), 16),
				Integer.valueOf(hexCode.substring(2, 4), 16),
				Integer.valueOf(hexCode.substring(4, 6), 16)
		);

		return ColorUtil.fromRGB(jColor.getRed(), jColor.getGreen(), jColor.getBlue());
	}
}