package entity.enums;

public enum type_abonnement {
    AVEC_ENGAGEMENT("Avec engagement"),
    SANS_ENGAGEMENT("Sans engagement");

    private final String displayName;

    type_abonnement(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
