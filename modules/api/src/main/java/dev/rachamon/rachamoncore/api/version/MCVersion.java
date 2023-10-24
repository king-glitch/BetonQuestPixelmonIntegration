package dev.rachamon.rachamoncore.api.version;

import lombok.Getter;

@Getter
public enum MCVersion {
    v1_7_10(MCDetailedVersion.v1_7_R4),
    v1_12(MCDetailedVersion.v1_12_R2),
    v1_13(MCDetailedVersion.v1_13_R2),
    v1_16(MCDetailedVersion.v1_16_R3),
    v1_19(MCDetailedVersion.v1_19_R3),
    v1_20(MCDetailedVersion.v1_20_R1);

    public static MCDetailedVersion getCurrent() {
        return MCDetailedVersion.getCurrent();
    }

    private final MCDetailedVersion detailedVersion;

    MCVersion(MCDetailedVersion detailedVersion) {
        this.detailedVersion = detailedVersion;
    }

    // -----------------------------------------------------------------------------------------------------------------
    //  Comparing with Detailed Version
    // -----------------------------------------------------------------------------------------------------------------

    public static boolean isLower(MCDetailedVersion otherVersion) {
        return MCDetailedVersion.getCurrent().isLower(otherVersion);
    }

    public static boolean isLowerEquals(MCDetailedVersion otherVersion) {
        return MCDetailedVersion.getCurrent().isLowerEquals(otherVersion);
    }

    public static boolean isEqual(MCDetailedVersion otherVersion) {
        return MCDetailedVersion.getCurrent().isEqual(otherVersion);
    }

    public static boolean isHigher(MCDetailedVersion otherVersion) {
        return MCDetailedVersion.getCurrent().isHigher(otherVersion);
    }

    public static boolean isHigherEquals(MCDetailedVersion otherVersion) {
        return MCDetailedVersion.getCurrent().isHigherEquals(otherVersion);
    }

}