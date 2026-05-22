/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

public class IEM extends PerangkatAudio {
    private String driverType; // Contoh: "Planar", "Dynamic"

    public IEM(String merk, String driverType, Equalizer eq) {
        super(merk, eq); // Memanggil konstruktor parent
        this.driverType = driverType;
    }

    // Polymorphism: Method Overriding
    @Override
    public void playAudio(String filePath) {
        System.out.println("Memutar audio via IEM " + merk + " (Driver: " + driverType + ")...");
        // Logika Audio Engine dipanggil via Controller
    }

    @Override
    public void applyEqualizer(Equalizer eq) {
        this.currentEq = eq;
        System.out.println("Menerapkan preset " + eq.getNamaPreset() + " ke IEM dengan tuning presisi.");
    }
}
