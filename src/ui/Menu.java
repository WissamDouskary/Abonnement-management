package ui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import config.DBconnection;
import dao.impl.AbonnementDAOImpl;
import dao.impl.PaimentDAOImpl;
import entity.Abonnement;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;
import entity.Paiement;
import entity.enums.*;
import services.AbonnementService;
import services.PaiementService;

import java.util.*;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);
    private static DBconnection dbConnection = DBconnection.getInstance();
    private static PaiementService paiementService = new PaiementService(new PaimentDAOImpl(dbConnection));
    private static AbonnementService abonnementService = new AbonnementService(new AbonnementDAOImpl(dbConnection), paiementService);

    public void afficherMenu() {
        System.out.println("=====================================");
        System.out.println("     Menu Principal des Abonnements  ");
        System.out.println("=====================================");
        System.out.println("1. Créer un abonnement (avec/sans engagement)");
        System.out.println("2. Modifier un abonnement");
        System.out.println("3. Supprimer un abonnement");
        System.out.println("4. Consulter la liste des abonnements");
        System.out.println("5. Afficher les paiements d'un abonnement");
        System.out.println("6. Crée un paiement");
        System.out.println("7. Modifier un paiement");
        System.out.println("8. Supprimer un paiement");
        System.out.println("9. Consulter les paiements manqués");
        System.out.println("10. Afficher la somme payée d'un abonnement");
        System.out.println("11. Afficher les 5 derniers paiements");
        System.out.println("12. Générer des rapports financiers");
        System.out.println("13. Quitter");
        System.out.println("=====================================");
    }

    public int saisirChoix() {
        System.out.print("Veuillez entrer votre choix: ");
        return scanner.nextInt();
    }

    public void traiterChoix(int choix) throws SQLException {
        switch (choix) {
            case 1:
                creerAbonnement();
                break;
            case 2:
                modifierAbonnement();
                break;
            case 3:
                supprimerAbonnement();
                break;
            case 4:
                consulterAbonnements();
                break;
            case 5:
                afficherPaiements();
                break;
            case 6:
                creerPaiement();
                break;
            case 7:
                modifierPaiement();
                break;
            case 8:
                supprimerPaiement();
                break;
            case 9:
                consulterPaiementsManques();
                break;
            case 10:
                afficherSommePayee();
                break;
            case 11:
                afficher5DerniersPaiements();
                break;
            case 12:
                genererRapportFinancier();
                break;
            case 13:
                quitter();
                break;
            default:
                optionInvalide();
                break;
        }
    }

    private void creerAbonnement() {
        System.out.println("Crée Abonnement ========================");
        System.out.println("1. Avec Engagement");
        System.out.println("2. Sans Engagement");

        int typeChoice = saisirInt();
        scanner.nextLine();

        System.out.println("Entrer le nom de service: ");
        String serviceNom = scanner.nextLine();

        System.out.println("Entrer le montant mensuel: ");
        double montantMonsuel = scanner.nextDouble();
        scanner.nextLine();

        // choix de date de début
        System.out.println("Entrer la Date de Debut: ");
        System.out.println("1. Utiliser la date d'aujourd'hui");
        System.out.println("2. Entrer une date spécifique");
        int dateChoix = saisirInt();
        scanner.nextLine();
        LocalDate startDate;

        if (dateChoix == 1) {
            startDate = LocalDate.now();
        } else {
            System.out.println("Entrez la date sous la forme (yyyy-MM-dd): ");
            String inputDate = scanner.nextLine();
            try {
                startDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                System.out.println("Date invalide, la date d'aujourd'hui sera utilisée.");
                startDate = LocalDate.now();
            }
        }

        statut_abonnement status_abo = statut_abonnement.Active;
        type_abonnement type_abo;
        Optional<Integer> dureeEngagementMois = Optional.empty();
        Optional<LocalDate> dateFin = Optional.empty();

        // type d'abonnement
        if (typeChoice == 1) {
            type_abo = type_abonnement.AVEC_ENGAGEMENT;
            System.out.println("Entrer la durée d'engagement en mois: ");
            int duree = saisirInt();
            dureeEngagementMois = Optional.of(duree);
            dateFin = Optional.of(startDate.plusMonths(duree));
        } else if (typeChoice == 2) {
            type_abo = type_abonnement.SANS_ENGAGEMENT;
            System.out.println("Souhaitez-vous définir une date de fin ? (oui/non)");
            String choix = scanner.nextLine();
            if (choix.equalsIgnoreCase("oui")) {
                System.out.println("Entrez la date de fin (yyyy-MM-dd) : ");
                String dateFinStr = scanner.nextLine();
                dateFin = Optional.of(LocalDate.parse(dateFinStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        } else {
            System.out.println("Choix invalide, abonnement annulé.");
            return;
        }

        try {
            String idAbonnement = abonnementService.createAbonnement(
                    serviceNom,
                    montantMonsuel,
                    status_abo,
                    type_abo,
                    startDate,
                    dateFin,
                    dureeEngagementMois
            );

            System.out.println("Abonnement créé avec succès: " + idAbonnement);

            if (type_abo == type_abonnement.AVEC_ENGAGEMENT && dureeEngagementMois.isPresent()) {

                int duree = dureeEngagementMois.get();
                    for (int i = 0; i < duree; i++) {
                        LocalDate dateEcheance = startDate.plusMonths(i + 1);
                        Paiement paiement = new Paiement(
                                UUID.fromString(idAbonnement),
                                dateEcheance,
                                null,
                                type_paiement.Autre,
                                statut_paiement.NON_PAYE.getDisplayName());
                        paiementService.createPaiement(paiement);
                    }
                    System.out.println(duree + " paiements générés automatiquement.");

            }

        } catch (SQLException e) {
            System.out.println("Erreur de base de données: " + e.getMessage());
        }
    }

    private void modifierAbonnement() throws SQLException {
        System.out.println("Modification d'un abonnement ========================");

        // showing abonnements
        consulterAbonnements();

        System.out.println("Choisissez l'ID dans cette liste pour l'abonnement que vous souhaitez modifier: ");

        scanner.nextLine();
        String id = scanner.nextLine();

        if (abonnementService.findById(id) != null) {
            Abonnement abonnement = abonnementService.findById(id);

            while (true) {
                System.out.println("1. Modifier le nom de service");
                System.out.println("2. Modifier le montant mensuel");
                System.out.println("3. Modifier la date de debut");
                System.out.println("4. Modifier la date de fin");
                System.out.println("5. Modifier le statut d'abonnement");
                System.out.println("6. Modifier le type d'abonnement");
                if (abonnement instanceof AbonnementAvecEngagement && abonnement.getType_abonnement() == type_abonnement.AVEC_ENGAGEMENT) {
                    System.out.println("7. Modifier la duree Engagement mois");
                }
                System.out.println("0. Save Changes");

                int updateChoice = saisirChoix();
                scanner.nextLine();

                if (updateChoice == 0) {
                    break;
                }

                switch (updateChoice) {
                    case 1:
                        System.out.println("Entrer le neauvaux nom de service :");
                        String newService = scanner.nextLine();
                        abonnement.setNomService(newService);
                        System.out.println("nom de service modifie avec success");
                        break;
                    case 2:
                        System.out.println("Entrer le neauvaux montant Monsieul :");
                        double newMontant = scanner.nextDouble();
                        abonnement.setMontantMensuel(newMontant);
                        System.out.println("montant Monsieul modifie avec success");
                        break;
                    case 3:
                        System.out.println("Entrer le neauveau date de debut (yyyy-mm-dd): ");
                        String debutdate = scanner.nextLine();
                        abonnement.setDateDebut(LocalDate.parse(debutdate));
                        System.out.println("date de debut modifie avec success");
                        break;
                    case 4:
                        System.out.println("Entrer le neauveau date de fin (yyyy-mm-dd): ");
                        String dateFin = scanner.nextLine();
                        abonnement.setDatefin(LocalDate.parse(dateFin));
                        System.out.println("date de fin modifie avec success");
                        break;
                    case 5:
                        System.out.println("statut actuel: "+ abonnement.getStatus());
                        System.out.println("Entrer le neauveau status: ");

                        System.out.println("1. Active");
                        System.out.println("2. Suspendu");
                        System.out.println("3. Resilie");

                        System.out.println("Ton choix: ");
                        int choice = saisirInt();

                        if(choice == 1){
                            abonnement.setStatus(statut_abonnement.Active);
                            System.out.println("Abonnement statut modifie par: " + statut_abonnement.Active);
                        }else if(choice == 2){
                            abonnement.setStatus(statut_abonnement.Suspendu);
                            System.out.println("Abonnement statut modifie par: " + statut_abonnement.Suspendu);
                        }else if (choice == 3){
                            abonnement.setStatus(statut_abonnement.Resilie);
                            System.out.println("Abonnement statut modifie par: " + statut_abonnement.Resilie);
                        }else{
                            System.out.println("Invalid selection!");
                        }
                        break;
                    case 6:
                        System.out.println("Type actuel: " + abonnement.getType_abonnement());
                        System.out.println("Entrer le nouveau type d'abonnement: ");
                        System.out.println("1. Avec Engagement");
                        System.out.println("2. Sans Engagement");

                        int typeChoice = saisirInt();

                        if (typeChoice == 1) {
                            if (abonnement.getType_abonnement() == type_abonnement.SANS_ENGAGEMENT) {
                                System.out.println("Durée d'engagement (en mois): ");
                                int duree = saisirInt();

                                abonnement = new AbonnementAvecEngagement(
                                        abonnement.getId().toString(),
                                        abonnement.getNomService(),
                                        abonnement.getMontantMensuel(),
                                        abonnement.getDateDebut(),
                                        abonnement.getDatefin() != null ? abonnement.getDatefin() : abonnement.getDateDebut().plusMonths(duree),
                                        abonnement.getStatus(),
                                        duree,
                                        type_abonnement.AVEC_ENGAGEMENT
                                );

                                System.out.println("Type changé en: AVEC_ENGAGEMENT avec durée " + duree + " mois");

                            } else {
                                System.out.println("Cet abonnement est déjà Avec Engagement!");
                            }

                        } else if (typeChoice == 2) {
                            if (abonnement.getType_abonnement() == type_abonnement.AVEC_ENGAGEMENT) {

                                abonnement = new AbonnementSansEngagement(
                                        abonnement.getId().toString(),
                                        abonnement.getNomService(),
                                        abonnement.getMontantMensuel(),
                                        abonnement.getDateDebut(),
                                        abonnement.getDatefin(),
                                        abonnement.getStatus(),
                                        type_abonnement.SANS_ENGAGEMENT
                                );

                                System.out.println("Type changé en: SANS_ENGAGEMENT");

                            } else {
                                System.out.println("Cet abonnement est déjà Sans Engagement!");
                            }
                        } else {
                            System.out.println("Choix invalide");
                        }
                        break;
                    case 7:
                        System.out.println("Entrer le neauveau duree d'engagement");

                        int duree = saisirInt();

                        if(abonnement instanceof AbonnementAvecEngagement){
                            AbonnementAvecEngagement abo = (AbonnementAvecEngagement) abonnement;
                            abo.setDureeEngagementMois(duree);
                            abo.setDatefin(abo.getDatefin().plusMonths(duree));
                            System.out.println("Duree modifie avec sucess!");
                        }

                        break;
                    default:
                        System.out.println("invalid Selection!");
                        break;
                }

            }

            Optional<Integer> dureeEngagementMois;

            if (abonnement instanceof AbonnementAvecEngagement) {
                AbonnementAvecEngagement aboEngagement = (AbonnementAvecEngagement) abonnement;
                dureeEngagementMois = Optional.of(aboEngagement.getDureeEngagementMois());
            } else {
                dureeEngagementMois = Optional.empty();
            }

            abonnementService.updateAbonnement(
                    abonnement.getId().toString(),
                    abonnement.getNomService(),
                    abonnement.getMontantMensuel(),
                    abonnement.getStatus(),
                    abonnement.getType_abonnement(),
                    abonnement.getDateDebut(),
                    Optional.ofNullable(abonnement.getDatefin()),
                    dureeEngagementMois
            );

            Paiement paiement = paiementService.findByAbonnementId(id);
            if (paiement != null) {
                LocalDate newEcheance;

                if (abonnement.getDatefin() != null) {
                    newEcheance = abonnement.getDatefin();
                } else {
                    newEcheance = abonnement.getDateDebut().plusMonths(1);
                }

                paiement.setDateEcheance(newEcheance);
                paiementService.updatePaiement(paiement);
            }

            System.out.println("Abonnement updated successfully! Paiement échéance updated.");

        } else {
            System.out.println("aucun abonnement avec cette id!");
        }
    }

    private void supprimerAbonnement() throws SQLException {
        System.out.println("Supression d'un abonnement ========================");

        System.out.println("ABONNEMENTS : ---------------------------");
        Map<String, Abonnement> abonnements = abonnementService.findAllAbonnements();

        System.out.println("====================");
        abonnements.forEach((key, abo) -> System.out.println(abo.toString() + "\n"));
        System.out.println("==================== \n");

        System.out.println("Entrer Abonnement ID pour la supprission: ");
        scanner.nextLine();
        String id = scanner.nextLine();

        Abonnement abonnement = abonnementService.findById(id);

        if(abonnement == null){
            System.out.println("Aucun abonnement avec ce ID!");
            return;
        }

        System.out.println("Abonnement found!");
        System.out.println("etes-vous sur de vouloir supprimer cet abonnement ( 1. Oui / 2. Non) :");
        int deletionChoice = saisirInt();

        if(deletionChoice == 1){
            abonnementService.deleteAbonnement(abonnement);
            System.out.println("Abonnement supprimer avec sucess");
        }else if(deletionChoice == 2){
            System.out.println("Ok!");
        }else{
            System.out.println("Invalid selection");
        }
    }

    private void consulterAbonnements() {
        System.out.println("la liste des abonnements ========================");

        Map<String, Abonnement> abonnements = abonnementService.findAllAbonnements();

        System.out.println("====================");
        abonnements.forEach((key, abo) -> System.out.println(abo.toString() + "\n"));
        System.out.println("==================== \n");
    }

    private void afficherPaiements() {
        System.out.println("Affichage des paiements d'un abonnement ==============");

        Map<String, Paiement> paiements = paiementService.findAll();
        Map<String, Abonnement> abonnements = abonnementService.findAllAbonnements();

        Map<Abonnement, List<Paiement>> abonnementPaiementsMap = new HashMap<>();

        for (Paiement p : paiements.values()) {
            for (Abonnement a : abonnements.values()) {
                if (p.getIdAbonnement().equals(a.getId())) {
                    if (!abonnementPaiementsMap.containsKey(a)) {
                        abonnementPaiementsMap.put(a, new ArrayList<>());
                    }
                    abonnementPaiementsMap.get(a).add(p);
                }
            }
        }

        for (Abonnement a : abonnementPaiementsMap.keySet()) {
            System.out.println("\n--------------------------------------");
            System.out.println("Abonnement ID        : " + a.getId());
            System.out.println("Nom du service        : " + a.getNomService());
            System.out.println("Type                 : " + a.getType_abonnement());
            System.out.println("Date de début        : " + a.getDateDebut());
            System.out.println("Date de fin          : " + a.getDatefin());
            System.out.println("Montant total        : " + a.getMontantMensuel() + " DH");
            System.out.println("----------------------------------------");
            System.out.println("PAIEMENTS ASSOCIÉS :");

            List<Paiement> paiementsList = abonnementPaiementsMap.get(a);
            for (Paiement p : paiementsList) {
                System.out.println("   ------------------------------");
                System.out.println("   Paiement ID        : " + p.getIdPaiement());
                System.out.println("   Date de paiement   : " + p.getDatePaiement());
                System.out.println("   Date d'echeance       : " + p.getDateEcheance());
                System.out.println("   Type de paiement  : " + p.getTypePaiement());
                System.out.println("   Statut  : " + p.getStatus_paiment());
            }

            System.out.println("=========================================\n");
        }
    }

    private void creerPaiement(){

    }

    private void modifierPaiement() {
        System.out.println("Modification d'un paiement ========================");
        Map<String, Paiement> paiments = paiementService.findAll();

        paiments.forEach((key, pai) -> System.out.println("==================\n"+pai+"\n==================\n"));

        System.out.println("Entrer l'ID du paiement à modifier :");
        scanner.nextLine();
        String idPaiement = scanner.nextLine();

        Paiement paiement = paiementService.findById(idPaiement);
        if (paiement == null) {
            System.out.println("Aucun paiement trouvé avec cet ID.");
            return;
        }

        while (true) {
            System.out.println("1. Modifier date échéance");
            System.out.println("2. Modifier date paiement");
            System.out.println("3. Modifier type paiement");
            System.out.println("4. Modifier statut");
            System.out.println("0. Sauvegarder");

            int choix = saisirInt();
            scanner.nextLine();

            if (choix == 0) break;

            switch (choix) {
                case 1:
                    System.out.println("Nouvelle date échéance (yyyy-MM-dd):");
                    String newEch = scanner.nextLine();
                    paiement.setDateEcheance(LocalDate.parse(newEch));
                    break;
                case 2:
                    System.out.println("Nouvelle date paiement (yyyy-MM-dd):");
                    String newPaiement = scanner.nextLine();
                    paiement.setDatePaiement(LocalDate.parse(newPaiement));
                    break;
                case 3:
                    System.out.println("Nouveau type (1.Carte 2.Virement 3.Cheque 4.Autre):");
                    int type = saisirInt();
                    paiement.setTypePaiement(type == 1 ? type_paiement.Carte :
                            type == 2 ? type_paiement.Virement :
                                    type == 3 ? type_paiement.Cheque :
                                            type_paiement.Autre);
                    break;
                case 4:
                    System.out.println("Nouveau statut (1.PAYÉ 2.NON_PAYÉ 3.EN_RETARD):");
                    int st = saisirInt();
                    paiement.setStatus_paiment(
                            st == 1 ? statut_paiement.PAYE.getDisplayName() :
                                    st == 2 ? statut_paiement.NON_PAYE.getDisplayName() :
                                            statut_paiement.EN_RETARD.getDisplayName()
                    );
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }

        paiementService.updatePaiement(paiement);
    }

    private void supprimerPaiement() {
        System.out.println("Suppression d'un paiement...");
    }

    private void consulterPaiementsManques() {
        System.out.println("Consultation des paiements manqués et du montant total impayé...");
    }

    private void afficherSommePayee() {
        System.out.println("Affichage de la somme payée d'un abonnement...");
    }

    private void afficher5DerniersPaiements() {
        System.out.println("Affichage des 5 derniers paiements...");
    }

    private void genererRapportFinancier() {
        System.out.println("Génération d'un rapport financier...");
    }

    private void quitter() {
        System.out.println("Merci et à bientôt !");
        System.exit(0);
    }

    private void optionInvalide() {
        System.out.println("Option invalide, veuillez réessayer.");
    }

    public int saisirInt() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Entrée invalide, veuillez entrer un numéro entier valide.");
            }
        }
    }
}

