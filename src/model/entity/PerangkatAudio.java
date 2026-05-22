/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

public abstract class PerangkatAudio {
    protected String merk;
    protected Equalizer currentEq;

    // Polymorphism: Method Overloading (Konstruktor)
    public PerangkatAudio() {
        this.merk = "Unknown";
    }

    public PerangkatAudio(String merk, Equalizer eq) {
        this.merk = merk;
        this.currentEq = eq;
    }

    // Abstract methods untuk di-override child class
    public abstract void playAudio(String filePath);
    public abstract void applyEqualizer(Equalizer eq);
    
    public Equalizer getEq() { return currentEq; }
}