package services;

import dao.impl.AbonnementDAOImpl;
import dao.impl.PaimentDAOImpl;
import entity.Abonnement;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;
import entity.Paiement;
import entity.enums.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class AbonnementService {
    private AbonnementDAOImpl abonnementImpl;
    private PaiementService paiementService;

    public AbonnementService (AbonnementDAOImpl abonnementImpl, PaiementService paiementService){
        this.abonnementImpl = abonnementImpl;
        this.paiementService = paiementService;
    }

    public Abonnement findById(String id) throws SQLException {
        return abonnementImpl.findbyId(id);
    }

    public String createAbonnement(String nomService, double montantMensuel,
                                   statut_abonnement status, type_abonnement type,
                                   LocalDate startDate,
                                   Optional<LocalDate> dateFin,
                                   Optional<Integer> dureeEngagementMois
    ) throws SQLException {

        // checking fields
        if (nomService.isEmpty()) { return "Enter a valid service name"; }
        if (montantMensuel <= 0) { return "Enter a valid montant mensuel"; }
        if (startDate == null) { return "La date de début est invalide."; }

        Abonnement abonnement;

        if (type == type_abonnement.AVEC_ENGAGEMENT) {
            int duree = dureeEngagementMois.orElse(0);
            LocalDate endDate = dateFin.orElse(startDate.plusMonths(duree));

            abonnement = new AbonnementAvecEngagement(
                    nomService,
                    montantMensuel,
                    startDate,
                    endDate,
                    status,
                    duree,
                    type_abonnement.AVEC_ENGAGEMENT
            );

            abonnementImpl.create(abonnement);

        } else {
            abonnement = new AbonnementSansEngagement(
                    nomService,
                    montantMensuel,
                    startDate,
                    dateFin.orElse(null),
                    status,
                    type_abonnement.SANS_ENGAGEMENT
            );
            abonnementImpl.create(abonnement);
        }

        return abonnement.getId().toString();
    }

    public String updateAbonnement(
            String id,
            String nomService, double montantMensuel,
            statut_abonnement status, type_abonnement type,
            LocalDate startDate,
            Optional<LocalDate> dateFin,
            Optional<Integer> dureeEngagementMois
    ){
        try {
            Abonnement abonnement;

            if (type == type_abonnement.AVEC_ENGAGEMENT) {
                abonnement = new AbonnementAvecEngagement(
                        id,
                        nomService,
                        montantMensuel,
                        startDate,
                        dateFin.orElse(null),
                        status,
                        dureeEngagementMois.orElse(0),
                        type
                );
            } else {
                abonnement = new AbonnementSansEngagement(
                        id,
                        nomService,
                        montantMensuel,
                        startDate,
                        dateFin.orElse(null),
                        status,
                        type
                );
            }

            abonnementImpl.update(abonnement);
            return "Abonnement updated successfully!";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error while updating abonnement: " + e.getMessage();
        }
    }

    public String deleteAbonnement(Abonnement abonnement){
        if(abonnement.getId().toString().isEmpty()){return "entrer une valide id";}

        abonnementImpl.delete(abonnement);
        return "Abonnement supprimer avec sucess!";
    }

    public Map<String, Abonnement> findAllAbonnements(){
        return abonnementImpl.findAll();
    }
}
