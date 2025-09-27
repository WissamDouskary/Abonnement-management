package app;

import config.DBconnection;
import dao.impl.PaimentDAOImpl;
import entity.enums.*;
import scheduler.PaiementScheduler;
import services.AbonnementService;
import services.PaiementService;
import ui.Menu;

import java.sql.SQLException;


public class AbonnementApp {
    private static AbonnementService abonnementService;

    public static void main(String[] args) throws SQLException {
        DBconnection db = DBconnection.getInstance();
        PaimentDAOImpl paiementDAO = new PaimentDAOImpl(db);
        PaiementService paiementService = new PaiementService(paiementDAO);

        PaiementScheduler scheduler = new PaiementScheduler(paiementService);
        scheduler.start();

        Menu menu = new Menu();

        while (true) {
            menu.afficherMenu();
            int choix = menu.saisirChoix();
            menu.traiterChoix(choix);
        }

    }
}
