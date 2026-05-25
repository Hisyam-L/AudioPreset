package model.entity;

public abstract class PerangkatAudio {

    // ENKAPSULASI: Diubah dari protected menjadi private agar data tidak bocor langsung ke child class
    private String merk;
    private Equalizer currentEq;

    // Polymorphism: Method Overloading (Konstruktor)
    public PerangkatAudio() {
        this.merk = "Unknown";
    }

    public PerangkatAudio(String merk, Equalizer eq) {
        this.merk = merk;
        this.currentEq = eq;
    }

    // Abstract methods untuk di-override child class
    public abstract String playAudio(String filePath);

    public abstract void applyEqualizer(Equalizer eq);

    // Getter & Setter Tambahan untuk mendukung Enkapsulasi penuh
    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public Equalizer getEq() {
        return currentEq;
    }

    public void setEq(Equalizer currentEq) {
        this.currentEq = currentEq;
    }
}
