package entity;

import java.time.LocalDate;
import java.util.UUID;

import entity.enums.*;

public class Paiement {
    private UUID idPaiement;
    private UUID idAbonnement;
    private LocalDate dateEcheance;
    private LocalDate datePaiement;
    private type_paiement typePaiement;
    private String status_paiment;

    public Paiement(UUID idAbonnement, LocalDate dateEcheance, LocalDate datePaiement, type_paiement typePaiement, String status_paiment) {
        this.idPaiement = UUID.randomUUID();
        this.idAbonnement = idAbonnement;
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.status_paiment = status_paiment;
    }

    public Paiement(UUID idPaiement, UUID idAbonnement, LocalDate dateEcheance,
                    LocalDate datePaiement, type_paiement typePaiement, String status_paiment) {
        this.idPaiement = idPaiement;
        this.idAbonnement = idAbonnement;
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.status_paiment = status_paiment;
    }

    public UUID getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(UUID idPaiement) {
        this.idPaiement = idPaiement;
    }

    public String getStatus_paiment() {
        return status_paiment;
    }

    public void setStatus_paiment(String status_paiment) {
        this.status_paiment = status_paiment;
    }

    public type_paiement getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(type_paiement typePaiement) {
        this.typePaiement = typePaiement;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public UUID getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(UUID idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    @Override
    public String toString() {
        return "Paiment id: " + getIdPaiement() + "\n" +
                "Abonnement id: '" + getIdAbonnement() + "'\n" +
                "Date Echeance: " + getDateEcheance() + "\n" +
                "Date de paiment: " + getDatePaiement() + "\n" +
                "Payment Type: " + getTypePaiement() + "\n" +
                "Paiment Status: " + getStatus_paiment();
    }
}
