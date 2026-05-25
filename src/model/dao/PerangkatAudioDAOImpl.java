package model.dao;

import model.connector;
import model.entity.Equalizer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerangkatAudioDAOImpl implements InterfaceDAOAudio {

    @Override
    public void insert(Equalizer eq, String tipePerangkat) {
        String sql = "INSERT INTO preset_audio (nama_preset, hz_115, hz_250, hz_450, hz_13k, tipe_perangkat) VALUES (?, ?, ?, ?, ?, ?)";
        // Panggil DatabaseConnector di sini
        try (Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eq.getNamaPreset());
            ps.setFloat(2, eq.getHz115());
            ps.setFloat(3, eq.getHz250());
            ps.setFloat(4, eq.getHz450());
            ps.setFloat(5, eq.getHz13k());
            ps.setString(6, tipePerangkat);
            ps.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

 @Override
    public void update(Equalizer eq) {
        String sql = "UPDATE preset_audio SET nama_preset=?, hz_115=?, hz_250=?, hz_450=?, hz_13k=? WHERE id=?";
        
        try (Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
  
            ps.setString(1, eq.getNamaPreset());
            ps.setFloat(2, eq.getHz115());
            ps.setFloat(3, eq.getHz250());
            ps.setFloat(4, eq.getHz450());
            ps.setFloat(5, eq.getHz13k());
            ps.setInt(6, eq.getId()); 
            
            ps.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM preset_audio WHERE id=?";
        try (Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    @Override
    public List<Equalizer> getAll() {
        List<Equalizer> list = new ArrayList<>();
        String sql = "SELECT * FROM preset_audio";
        try (Connection conn = connector.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Equalizer(
                    rs.getInt("id"), rs.getString("nama_preset"),
                    rs.getFloat("hz_115"), rs.getFloat("hz_250"),
                    rs.getFloat("hz_450"), rs.getFloat("hz_13k")
                ));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }
}