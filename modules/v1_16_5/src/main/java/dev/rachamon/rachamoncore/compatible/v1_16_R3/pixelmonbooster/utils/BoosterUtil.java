package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.utils;

public class BoosterUtil {
    public static String secondsToTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
