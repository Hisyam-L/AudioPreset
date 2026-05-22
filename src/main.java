/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author hisyamlbaihaqi
 */
import view.IEMView;
import controller.ControllerAudioPreset;
import javax.swing.SwingUtilities;

public class main {
    public static void main(String[] args) {
        // Memastikan GUI berjalan di Event Dispatch Thread (Bagian dari Multithreading standar Swing)
        SwingUtilities.invokeLater(() -> {
            IEMView view = new IEMView();
            new ControllerAudioPreset(view);
            view.setLocationRelativeTo(null);
            view.setVisible(true);
        });
    }
}
