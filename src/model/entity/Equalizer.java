/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

public class Equalizer {
    // Encapsulation: Atribut private
    private int id;
    private String namaPreset;
    private float hz115, hz250, hz450, hz13k;

    public Equalizer() {}

    public Equalizer(int id, String namaPreset, float hz115, float hz250, float hz450, float hz13k) {
        this.id = id;
        this.namaPreset = namaPreset;
        this.hz115 = hz115;
        this.hz250 = hz250;
        this.hz450 = hz450;
        this.hz13k = hz13k;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNamaPreset() { return namaPreset; }
    public void setNamaPreset(String namaPreset) { this.namaPreset = namaPreset; }
    public float getHz115() { return hz115; }
    public void setHz115(float hz115) { this.hz115 = hz115; }
    public float getHz250() { return hz250; }
    public void setHz250(float hz250) { this.hz250 = hz250; }
    public float getHz450() { return hz450; }
    public void setHz450(float hz450) { this.hz450 = hz450; }
    public float getHz13k() { return hz13k; }
    public void setHz13k(float hz13k) { this.hz13k = hz13k; }
}