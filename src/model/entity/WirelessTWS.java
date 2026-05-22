/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

public class WirelessTWS extends PerangkatAudio {
    private String bluetoothCodec;

    public WirelessTWS(String merk, String codec, Equalizer eq) {
        super(merk, eq);
        this.bluetoothCodec = codec;
    }

    @Override
    public void playAudio(String filePath) {
        System.out.println("Streaming via TWS " + merk + " (Codec: " + bluetoothCodec + ")...");
    }

    @Override
    public void applyEqualizer(Equalizer eq) {
        this.currentEq = eq;
        System.out.println("Menerapkan EQ dengan kompensasi delay Bluetooth.");
    }
}