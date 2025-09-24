package ui;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import config.DBconnection;
import dao.impl.AbonnementDAOImpl;
import entity.Abonnement;
import entity.enums.*;
import services.AbonnementService;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);

    public void afficherMenu() {
        System.out.println("=====================================");
        System.out.println("     Menu Principal des Abonnements  ");
        System.out.println("=====================================");
        System.out.println("1. Créer un abonnement (avec/sans engagement)");
        System.out.println("2. Modifier un abonnement");
        System.out.println("3. Supprimer un abonnement");
        System.out.println("4. Consulter la liste des abonnements");
        System.out.println("5. Afficher les paiements d'un abonnement");
        System.out.println("6. Enregistrer un paiement");
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

    public void traiterChoix(int choix) {
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
                enregistrerPaiement();
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

        System.out.println("Entrer la Date de Debut: ");
        System.out.println("1. Entrez Automatiquement la date d'aujourd'hui");
        System.out.println("2. Entrer une specifique date");

        int dateChoix = saisirInt();
        scanner.nextLine();
        LocalDate startDate = LocalDate.now();

        if (dateChoix == 1) {
            startDate = LocalDate.now();
        } else if (dateChoix == 2) {
            System.out.println("Entrez la date sous la forme (yyyy-MM-dd): ");
            String inputDate = scanner.nextLine();
            try {
                startDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                System.out.println("Date invalide, la date d'aujourd'hui sera utilisée.");
                startDate = LocalDate.now();
            }
        } else {
            System.out.println("Choix invalide, date par défaut aujourd'hui sera utilisée.");
            startDate = LocalDate.now();
        }

        statut_abonnement status_abo = statut_abonnement.Active;
        type_abonnement type_abo = null;
        Optional<Integer> dureeEngagementMois = Optional.empty();
        Optional<LocalDate> dateFin = Optional.empty();

        // engagement types
        if (typeChoice == 1) {
            type_abo = type_abonnement.AVEC_ENGAGEMENT;
            System.out.println("Entrer la durée d'engagement en mois: ");
            int duree = saisirInt();
            dureeEngagementMois = Optional.of(duree);
            if (startDate != null) {
                dateFin = Optional.of(startDate.plusMonths(duree));
            }
        } else if (typeChoice == 2) {
            type_abo = type_abonnement.SANS_ENGAGEMENT;
        } else {
            System.out.println("Choix invalide, abonnement annulé.");
            return;
        }

        if (type_abo == type_abonnement.SANS_ENGAGEMENT) {
            System.out.println("Souhaitez-vous définir une date de fin ? (oui/non)");
            String choix = scanner.nextLine();
            if (choix.equalsIgnoreCase("oui")) {
                System.out.println("Entrez la date de fin (yyyy-MM-dd) : ");
                String dateFinStr = scanner.nextLine();
                dateFin = Optional.of(LocalDate.parse(dateFinStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        }

        // create and applicate variables in service
        try {
            DBconnection dbConnection = DBconnection.getInstance();
            AbonnementService abonnementService = new AbonnementService(new AbonnementDAOImpl(dbConnection));
            String result = abonnementService.createAbonnement(
                    serviceNom,
                    montantMonsuel,
                    status_abo,
                    type_abo,
                    startDate,
                    dateFin,
                    dureeEngagementMois
            );
            System.out.println(result);
        } catch (SQLException e) {
            System.out.println("Erreur de base de données: " + e.getMessage());
        }
    }

    private void modifierAbonnement() {
        System.out.println("Modification d'un abonnement ========================");

    }

    private void supprimerAbonnement() {
        System.out.println("Suppression d'un abonnement...");
    }

    private void consulterAbonnements() {
        System.out.println("la liste des abonnements ========================");
        DBconnection dbConnection = DBconnection.getInstance();
        AbonnementService abonnementService = new AbonnementService(new AbonnementDAOImpl(dbConnection));

        Map<String, Abonnement> abonnements = abonnementService.findAllAbonnements();

        System.out.println("====================");
        abonnements.forEach((key, abo) -> System.out.println(abo.toString() + "\n"));
        System.out.println("==================== \n");
    }

    private void afficherPaiements() {
        System.out.println("Affichage des paiements d'un abonnement...");
    }

    private void enregistrerPaiement() {
        System.out.println("Enregistrement d'un paiement...");
    }

    private void modifierPaiement() {
        System.out.println("Modification d'un paiement...");
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

