package app;

import entity.enums.*;
import services.AbonnementService;
import ui.Menu;

import java.sql.SQLException;


public class AbonnementApp {
    private static AbonnementService abonnementService;

    public static void main(String[] args) throws SQLException {
        Menu menu = new Menu();

        while (true) {
            menu.afficherMenu();
            int choix = menu.saisirChoix();
            menu.traiterChoix(choix);
        }

    }
}
