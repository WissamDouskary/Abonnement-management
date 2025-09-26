package services;

import dao.impl.*;
import entity.Paiement;
import entity.enums.statut_paiement;
import entity.enums.type_paiement;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class PaiementService {
    private PaimentDAOImpl paimentDAOimpl;

    public PaiementService (PaimentDAOImpl paimentDAOimpl){
        this.paimentDAOimpl = paimentDAOimpl;
    }

    public void createPaiement(Paiement paiement){
        if(paiement.getIdAbonnement().toString().isEmpty()){ System.out.println("you can't continue without abonnement id!");}
        if(paiement.getDateEcheance().toString().isEmpty()){ System.out.println("you can't continue without date!");}

        paimentDAOimpl.create(paiement);
        System.out.println("payment created successfully!");
    }

    public void createPaiement(UUID idAbonnement, LocalDate dateEcheance, Double montant,
                               type_paiement typePaiement, statut_paiement statutPaiement) {

        Paiement paiement = new Paiement(
                idAbonnement,
                dateEcheance,
                null,
                typePaiement,
                statutPaiement.getDisplayName()
        );

        paimentDAOimpl.create(paiement);
        System.out.println("Paiement créé pour l'abonnement " + idAbonnement);
    }

    public void updatePaiement(Paiement paiement) {
        if (paiement.getIdPaiement() == null) {
            System.out.println("Paiement ID is required for update!");
            return;
        }
        paimentDAOimpl.update(paiement);
        System.out.println("Paiement updated successfully!");
    }

    public Paiement findById(String idPaiement) {
        return paimentDAOimpl.findById(idPaiement);
    }

    public Map<String, Paiement> findAll(){
        return paimentDAOimpl.findAll();
    }

    public Paiement findByAbonnementId(String idAbonnement) {
        return paimentDAOimpl.findByAbonnementId(idAbonnement);
    }
}
