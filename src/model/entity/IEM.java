package model.entity;

import javax.swing.JOptionPane;

public class IEM extends PerangkatAudio {
    private String driverType; // Contoh: "Planar", "Dynamic"

    public IEM(String merk, String driverType, Equalizer eq) {
        super(merk, eq); 
        this.driverType = driverType;
    }

    // Polymorphism: Method Overriding
    @Override
    public void playAudio(String filePath) {
        // Menggunakan getMerk() karena variabel di parent sudah di-enkapsulasi menjadi private
        String pesan = "Memutar audio via IEM " + getMerk() + " (Driver: " + driverType + ")...";
        System.out.println(pesan);
        
        // AGAR TERLIHAT DI UI: Munculkan pesan pop-up informasi perangkat di GUI
        JOptionPane.showMessageDialog(null, pesan, "Playback Info (IEM)", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void applyEqualizer(Equalizer eq) {
        setEq(eq);
        System.out.println("Menerapkan preset " + eq.getNamaPreset() + " ke IEM dengan tuning presisi.");
    }
}