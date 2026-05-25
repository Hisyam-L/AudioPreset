package model.entity;

public class IEM extends PerangkatAudio {

    private String driverType; // Contoh: "Planar", "Dynamic"

    public IEM(String merk, String driverType, Equalizer eq) {
        super(merk, eq);
        this.driverType = driverType;
    }

    // Polymorphism: Method Overriding
    @Override
    public String playAudio(String filePath) {
        String pesan = "Memutar audio via IEM " + getMerk() + " (Driver: " + driverType + ")...";
        System.out.println(pesan);
        return pesan; 
    }

    @Override
    public void applyEqualizer(Equalizer eq) {
        setEq(eq);
        System.out.println("Menerapkan preset " + eq.getNamaPreset() + " ke IEM dengan tuning presisi.");
    }
}
