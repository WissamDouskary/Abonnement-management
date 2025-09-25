package dao.impl;

import config.DBconnection;
import dao.AbonnementDAO;
import entity.Abonnement;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;
import entity.enums.statut_abonnement;
import entity.enums.type_abonnement;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String sql = "UPDATE abonnement SET nom_service = ? , montant_mensuel = ? , date_debut = ? , date_fin = ? , statut = ? , type_abonnement = ? , duree_engagement_mois = ? WHERE id = ?";

        //create statement
        try(PreparedStatement st = conn.prepareStatement(sql)){
            st.setString(1, abonnement.getNomService());
            st.setDouble(2, abonnement.getMontantMensuel());
            st.setDate(3, Date.valueOf(abonnement.getDateDebut()));

            if(abonnement.getDatefin() != null){
                st.setDate(4, Date.valueOf(abonnement.getDatefin()));
            }else{
                st.setNull(4, Types.DATE);
            }

            st.setObject(5, abonnement.getStatus(), Types.OTHER);

            if(abonnement instanceof AbonnementAvecEngagement){
                AbonnementAvecEngagement abo = (AbonnementAvecEngagement) abonnement;
                st.setObject(6, "Avec Engagement", Types.OTHER);
                st.setInt(7, abo.getDureeEngagementMois());
            } else {
                st.setObject(6, "Sans Engagement", Types.OTHER);
                st.setNull(7, Types.INTEGER);
            }

            st.setString(8, abonnement.getId().toString());

            st.executeUpdate();
        }
        catch (SQLException e){
            System.err.println("SQL error " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Abonnement abonnement) {

    }

    @Override
    public Map<String, Abonnement> findAll() {
        Map<String, Abonnement> abonnements = new HashMap<>();

        String sql = "SELECT * FROM abonnement";

        try(PreparedStatement st = conn.prepareStatement(sql)){

            try(ResultSet rs = st.executeQuery()){

                Abonnement abonnement;

                while (rs.next()){
                    String id = rs.getString("id");
                    String nomService = rs.getString("nom_service");
                    double montantMonsuel = rs.getDouble("montant_mensuel");

                    LocalDate dateDebut = rs.getDate("date_debut").toLocalDate();

                    Date dateFinSql = rs.getDate("date_fin");
                    LocalDate dateFin = (dateFinSql != null) ? dateFinSql.toLocalDate() : null;

                    String abo_statut = rs.getString("statut");
                    statut_abonnement statut = statut_abonnement.valueOf(abo_statut);

                    String typeStr = rs.getString("type_abonnement");
                    type_abonnement type = typeStr.equalsIgnoreCase("Avec Engagement")
                            ? type_abonnement.AVEC_ENGAGEMENT
                            : type_abonnement.SANS_ENGAGEMENT;

                    int duree_Engagement = rs.getInt("duree_engagement_mois");

                    if (type == type_abonnement.AVEC_ENGAGEMENT){
                        abonnement = new AbonnementAvecEngagement(
                                id, nomService, montantMonsuel, dateDebut, dateFin, statut,
                                duree_Engagement, type_abonnement.AVEC_ENGAGEMENT
                        );
                    }else{
                        abonnement = new AbonnementSansEngagement(
                                id, nomService, montantMonsuel, dateDebut, dateFin, statut, type
                        );
                    }

                    // insert it into hashMap
                    abonnements.put(abonnement.getId().toString(), abonnement);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return abonnements;
    }

    @Override
    public Abonnement findbyId(String id) throws SQLException {
        String sql = "SELECT * FROM abonnement WHERE id = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String nomService = rs.getString("nom_service");
                    double montantMensuel = rs.getDouble("montant_mensuel");
                    LocalDate dateDebut = rs.getDate("date_debut").toLocalDate();

                    Date dateFinSql = rs.getDate("date_fin");
                    LocalDate dateFin = (dateFinSql != null) ? dateFinSql.toLocalDate() : null;

                    String statutStr = rs.getString("statut");
                    statut_abonnement statut = statut_abonnement.valueOf(statutStr);

                    String typeStr = rs.getString("type_abonnement");
                    type_abonnement type = typeStr.equalsIgnoreCase("Avec Engagement")
                            ? type_abonnement.AVEC_ENGAGEMENT
                            : type_abonnement.SANS_ENGAGEMENT;

                    int dureeEngagement = rs.getInt("duree_engagement_mois");
                    boolean hasEngagement = type == type_abonnement.AVEC_ENGAGEMENT;

                    if (hasEngagement) {
                        return new AbonnementAvecEngagement(
                                id, nomService, montantMensuel, dateDebut, dateFin, statut,
                                dureeEngagement, type_abonnement.AVEC_ENGAGEMENT
                        );
                    } else {
                        return new AbonnementSansEngagement(
                                id, nomService, montantMensuel, dateDebut, dateFin, statut,
                                type_abonnement.SANS_ENGAGEMENT
                        );
                    }
                }
            }
        }

        return null;
    }

}
