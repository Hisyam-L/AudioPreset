package model.dao;

import model.Connector;
import model.entity.Equalizer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PerangkatAudioDAOImpl implements InterfaceDAOAudio {

    @Override
    public void insert(Equalizer eq) {
        String sql = "INSERT INTO preset_audio (nama_preset, hz_115, hz_250, hz_450, hz_13k, tipe_perangkat, detail_perangkat) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eq.getNamaPreset());
            ps.setFloat(2, eq.getHz115());
            ps.setFloat(3, eq.getHz250());
            ps.setFloat(4, eq.getHz450());
            ps.setFloat(5, eq.getHz13k());
            ps.setString(6, eq.getTipePerangkat());
            ps.setString(7, eq.getDetailPerangkat());
            ps.executeUpdate();
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, "Gagal simpan preset ke database: " + e.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Equalizer eq) {
        String sql = "UPDATE preset_audio SET nama_preset=?, hz_115=?, hz_250=?, hz_450=?, hz_13k=?, tipe_perangkat=?, detail_perangkat=? WHERE id=?";
        try (Connection conn = Connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eq.getNamaPreset());
            ps.setFloat(2, eq.getHz115());
            ps.setFloat(3, eq.getHz250());
            ps.setFloat(4, eq.getHz450());
            ps.setFloat(5, eq.getHz13k());
            ps.setString(6, eq.getTipePerangkat());
            ps.setString(7, eq.getDetailPerangkat());
            ps.setInt(8, eq.getId()); 
            ps.executeUpdate();
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, "Gagal update preset ke database: " + e.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM preset_audio WHERE id=?";
        try (Connection conn = Connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, "Gagal hapus preset: " + e.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Equalizer> getAll() {
        List<Equalizer> list = new ArrayList<>();
        String sql = "SELECT * FROM preset_audio";
        try (Connection conn = Connector.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Equalizer(
                    rs.getInt("id"), rs.getString("nama_preset"),
                    rs.getFloat("hz_115"), rs.getFloat("hz_250"),
                    rs.getFloat("hz_450"), rs.getFloat("hz_13k"),
                    rs.getString("tipe_perangkat"), rs.getString("detail_perangkat")
                ));
            }
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, "Gagal memuat preset: " + e.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }
}