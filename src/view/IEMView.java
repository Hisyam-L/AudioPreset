package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IEMView extends JFrame {
    private JComboBox<String> comboPreset;
    private JTextField txtPresetName;
    private JSlider slider115, slider250, slider450, slider13k;
    private JButton btnSave, btnUpdate, btnDelete, btnPlay, btnStop, btnChooseFile;
    private JLabel lblCurrentFile; 
    private JComboBox<String> comboTipePerangkat; 
    
    private JTable tabelPreset;
    private DefaultTableModel tableModel;
    private JButton btnClear;

    // TAMBAHAN: Komponen untuk detail perangkat (Driver/Codec)
    private JTextField txtDetailPerangkat;
    private JLabel lblDetailPerangkat;

    public IEMView() {
        setTitle("Java MVC Audio Equalizer - OOP Edition");
        setSize(1050, 450); // Lebarin dikit agar kolom input tambahannya muat
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Atas ---
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

        // --- PANEL KANAN: JTable Visualisasi Read Data CRUD ---
        String[] kolom = {"ID", "Nama", "115Hz", "250Hz", "450Hz", "13kHz", "Tipe", "Driver/Codec"};
        tableModel = new DefaultTableModel(kolom, 0);
        tabelPreset = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(tabelPreset);
        scrollTable.setPreferredSize(new Dimension(420, 0));
        add(scrollTable, BorderLayout.EAST);

        // --- Panel Bawah (CRUD Controls) ---
        JPanel bottomPanel = new JPanel(new FlowLayout());
        txtPresetName = new JTextField(8);
        comboTipePerangkat = new JComboBox<>(new String[]{"IEM", "Wireless TWS"});
        
        txtDetailPerangkat = new JTextField(8);
        lblDetailPerangkat = new JLabel("Driver Type:");
        
        // Listener supaya text label menyesuaikan tipe yang dipilih
        comboTipePerangkat.addActionListener(e -> {
            if (comboTipePerangkat.getSelectedItem().equals("IEM")) {
                lblDetailPerangkat.setText("Driver Type:");
            } else {
                lblDetailPerangkat.setText("Codec Bluetooth:");
            }
        });

        btnSave = new JButton("Save Preset");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear"); 
        
        bottomPanel.add(new JLabel("Nama:"));
        bottomPanel.add(txtPresetName);
        bottomPanel.add(new JLabel("Tipe:")); 
        bottomPanel.add(comboTipePerangkat);
        bottomPanel.add(lblDetailPerangkat);
        bottomPanel.add(txtDetailPerangkat);  
        bottomPanel.add(btnSave);
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnClear); 
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
    public JComboBox<String> getComboTipePerangkat() { return comboTipePerangkat; }
    
    // Getters Baru 
    public JTable getTabelPreset() { return tabelPreset; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JButton getBtnClear() { return btnClear; }
    public JTextField getTxtDetailPerangkat() { return txtDetailPerangkat; }
    public JLabel getLblDetailPerangkat() { return lblDetailPerangkat; }
}