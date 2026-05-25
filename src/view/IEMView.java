package view;

import javax.swing.*;
import java.awt.*;

public class IEMView extends JFrame {
    private JComboBox<String> comboPreset;
    private JTextField txtPresetName;
    private JSlider slider115, slider250, slider450, slider13k;
    private JButton btnSave, btnUpdate, btnDelete, btnPlay, btnStop, btnChooseFile;
    private JLabel lblCurrentFile; 
    
    // TAMBAHAN 1: Deklarasi ComboBox untuk Tipe Perangkat
    private JComboBox<String> comboTipePerangkat; 

    public IEMView() {
        setTitle("Java MVC Audio Equalizer - OOP Edition");
        setSize(650, 450); // Diperlebar sedikit agar muat form baru
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Atas (File Chooser, Pemutar & Preset) ---
        JPanel topContainer = new JPanel(new GridLayout(2, 1));
        
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnChooseFile = new JButton("Pilih File .wav");
        lblCurrentFile = new JLabel("File: Belum dipilih");
        filePanel.add(btnChooseFile);
        filePanel.add(lblCurrentFile);

        JPanel playPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPlay = new JButton("Play");
        btnStop = new JButton("Stop");
        comboPreset = new JComboBox<>();
        playPanel.add(btnPlay);
        playPanel.add(btnStop);
        playPanel.add(new JLabel("  |  Load Preset:"));
        playPanel.add(comboPreset);

        topContainer.add(filePanel);
        topContainer.add(playPanel);
        add(topContainer, BorderLayout.NORTH);

        // --- Panel Tengah (Slider EQ) ---
        JPanel eqPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        slider115 = createEqSlider("115 Hz");
        slider250 = createEqSlider("250 Hz");
        slider450 = createEqSlider("450 Hz");
        slider13k = createEqSlider("13 kHz");
        
        eqPanel.add(createSliderPanel("115 Hz", slider115));
        eqPanel.add(createSliderPanel("250 Hz", slider250));
        eqPanel.add(createSliderPanel("450 Hz", slider450));
        eqPanel.add(createSliderPanel("13 kHz", slider13k));
        add(eqPanel, BorderLayout.CENTER);

        // --- Panel Bawah (CRUD Controls) ---
        JPanel bottomPanel = new JPanel(new FlowLayout());
        txtPresetName = new JTextField(10);
        
        // TAMBAHAN 2: Inisialisasi ComboBox Tipe Perangkat
        comboTipePerangkat = new JComboBox<>(new String[]{"IEM", "Wireless TWS"});
        
        btnSave = new JButton("Save Preset");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        
        bottomPanel.add(new JLabel("Nama:"));
        bottomPanel.add(txtPresetName);
        bottomPanel.add(new JLabel("Tipe:")); // Label Tipe Perangkat
        bottomPanel.add(comboTipePerangkat);  // ComboBox Tipe Perangkat masuk ke panel
        bottomPanel.add(btnSave);
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnDelete);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JSlider createEqSlider(String name) {
        JSlider slider = new JSlider(JSlider.VERTICAL, -20, 20, 0);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private JPanel createSliderPanel(String label, JSlider slider) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(slider, BorderLayout.CENTER);
        panel.add(new JLabel(label, SwingConstants.CENTER), BorderLayout.SOUTH);
        return panel;
    }

    public JButton getBtnChooseFile() { return btnChooseFile; }
    public JLabel getLblCurrentFile() { return lblCurrentFile; }
    public JSlider getSlider115() { return slider115; }
    public JSlider getSlider250() { return slider250; }
    public JSlider getSlider450() { return slider450; }
    public JSlider getSlider13k() { return slider13k; }
    public JComboBox<String> getComboPreset() { return comboPreset; }
    public JTextField getTxtPresetName() { return txtPresetName; }
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnPlay() { return btnPlay; }
    public JButton getBtnStop() { return btnStop; }
    
    // TAMBAHAN 3: Getter untuk ComboBox Tipe Perangkat
    public JComboBox<String> getComboTipePerangkat() { return comboTipePerangkat; }
}