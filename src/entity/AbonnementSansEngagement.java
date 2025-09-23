package entity;

import entity.enums.statut_abonnement;
import entity.enums.type_abonnement;

import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement{

    public AbonnementSansEngagement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate datefin, statut_abonnement status, type_abonnement type) {
        super(nomService, montantMensuel, dateDebut, datefin, status, type);
    }
}
