package entity.enums;

public enum statut_paiement {
    PAYE("Payé"),
    NON_PAYE("Non payé"),
    EN_RETARD("En retard");

    private final String displayName;

    statut_paiement(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
