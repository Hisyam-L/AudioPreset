package model.entity;

import javax.swing.JOptionPane;

public class WirelessTWS extends PerangkatAudio {
    private String bluetoothCodec;

    public WirelessTWS(String merk, String codec, Equalizer eq) {
        super(merk, eq);
        this.bluetoothCodec = codec;
    }

    @Override
    public void playAudio(String filePath) {
        // Menggunakan getMerk() karena variabel di parent sudah di-enkapsulasi menjadi private
        String pesan = "Streaming via TWS " + getMerk() + " (Codec: " + bluetoothCodec + ")...";
        System.out.println(pesan);
        
        // AGAR TERLIHAT DI UI: Munculkan pesan pop-up informasi perangkat di GUI
        JOptionPane.showMessageDialog(null, pesan, "Playback Info (Wireless TWS)", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void applyEqualizer(Equalizer eq) {
        setEq(eq);
        System.out.println("Menerapkan EQ dengan kompensasi delay Bluetooth.");
    }
}