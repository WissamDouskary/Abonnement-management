package services;

import dao.impl.AbonnementDAOImpl;
import entity.Abonnement;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;
import entity.enums.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AbonnementService {
    private AbonnementDAOImpl abonnementImpl;

    public AbonnementService (AbonnementDAOImpl abonnementImpl){
        this.abonnementImpl = abonnementImpl;
    }

    public String createAbonnement(String nomService, double montantMensuel,
                                   statut_abonnement status, type_abonnement type,
                                   LocalDate startDate,
                                   Optional<LocalDate> dateFin,
                                   Optional<Integer> dureeEngagementMois) throws SQLException {

        // checking fields
        if(nomService.isEmpty()) { return "Enter a valid service name"; }
        if(montantMensuel <= 0) { return "Enter a valid montant mensuel"; }
        if(startDate == null) { return "La date de début est invalide."; }

        if(type == type_abonnement.AVEC_ENGAGEMENT){
            AbonnementAvecEngagement abonnementAvecEngagement =
                    new AbonnementAvecEngagement(nomService, montantMensuel, startDate, dateFin.orElse(null), status, dureeEngagementMois.orElse(0), type_abonnement.AVEC_ENGAGEMENT);
            abonnementImpl.create(abonnementAvecEngagement);
        } else if(type == type_abonnement.SANS_ENGAGEMENT){
            Abonnement abonnementSansEngagement = new AbonnementSansEngagement(nomService, montantMensuel, startDate, dateFin.orElse(null), status, type);
            abonnementImpl.create(abonnementSansEngagement);
        }

        return "Abonnement created successfully!";
    }
}
