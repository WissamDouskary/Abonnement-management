package dao.impl;

import config.DBconnection;
import dao.AbonnementDAO;
import entity.Abonnement;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AbonnementDAOImpl implements AbonnementDAO{
    private final Connection conn;

    public AbonnementDAOImpl(DBconnection dBconnection) {
        this.conn = dBconnection.getConnection();
    }

    @Override
    public void create(Abonnement abonnement) throws SQLException {
        String sql = "INSERT INTO abonnement (id, nom_service, montant_mensuel, date_debut, date_fin, statut, type_abonnement, duree_engagement_mois) VALUES (?,?,?,?,?,?,?,?)";

        try(PreparedStatement st = conn.prepareStatement(sql)){

            st.setString(1, abonnement.getId().toString());
            st.setString(2, abonnement.getNomService());
            st.setDouble(3, abonnement.getMontantMensuel());
            st.setDate(4, Date.valueOf(abonnement.getDateDebut()));

            if (abonnement.getDatefin() != null) {
                st.setDate(5, Date.valueOf(abonnement.getDatefin()));
            } else {
                st.setNull(5, java.sql.Types.DATE);
            }

            st.setObject(6, abonnement.getStatus().name(), java.sql.Types.OTHER);

            if (abonnement instanceof AbonnementAvecEngagement) {

                AbonnementAvecEngagement aboAvecEngagement = (AbonnementAvecEngagement) abonnement;
                st.setObject(7, "Avec Engagement", java.sql.Types.OTHER);
                st.setInt(8, aboAvecEngagement.getDureeEngagementMois());

            } else if(abonnement instanceof AbonnementSansEngagement){

                st.setObject(7, "Sans Engagement", java.sql.Types.OTHER);
                st.setNull(8, java.sql.Types.INTEGER);

            }

            st.executeUpdate();
        }
        catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void update(Abonnement abonnement) {

    }

    @Override
    public void delete(Abonnement abonnement) {

    }

    @Override
    public List<Abonnement> findAll() {
        return List.of();
    }

    @Override
    public Abonnement findbyId(String id) throws SQLException {
        return null;
    }

}
