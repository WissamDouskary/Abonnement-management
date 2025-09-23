package entity;

import java.time.LocalDate;

import entity.enums.*;

public class AbonnementAvecEngagement extends Abonnement {
    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, statut_abonnement statut, int dureeEngagementMois, type_abonnement type) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut, type);
        setDureeEngagementMois(dureeEngagementMois);
    }

    public int getDureeEngagementMois() {
        return dureeEngagementMois;
    }

    public void setDureeEngagementMois(int dureeEngagementMois) {
        this.dureeEngagementMois = dureeEngagementMois;
    }

    @Override
    public String toString() {
        return super.toString() + " | Engagement: " + dureeEngagementMois + " mois";
    }
}