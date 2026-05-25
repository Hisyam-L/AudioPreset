package controller;

import model.dao.InterfaceDAOAudio;
import model.dao.PerangkatAudioDAOImpl;
import model.entity.Equalizer;
import model.entity.PerangkatAudio;
import model.entity.IEM;
import model.entity.WirelessTWS;
import view.IEMView;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

public class ControllerAudioPreset {

    private final IEMView halamanUtama;
    private final InterfaceDAOAudio daoAudio;
    private List<Equalizer> daftarPreset;
    
    private int activePresetId = -1;
    private boolean isUpdatingUI = false;

    private Thread audioThread;
    private boolean isPlaying = false;
    private String currentAudioFilePath = null;
    private volatile float bassGain = 1.0f;
    private float[] prevBass = new float[2];
    private final float ALPHA = 0.05f;

    public ControllerAudioPreset(IEMView halamanUtama) {
        this.halamanUtama = halamanUtama;
        this.daoAudio = new PerangkatAudioDAOImpl();
        initListeners(); 
        showAllPreset();
    }

    // 1. FUNGSI READ: Mengisi data ComboBox DAN JTable visualisasi GUI secara bersamaan
    public void showAllPreset() {
        isUpdatingUI = true;
        halamanUtama.getComboPreset().removeAllItems();
        halamanUtama.getTableModel().setRowCount(0); // Bersihkan sisa tabel lama
        
        daftarPreset = daoAudio.getAll();
        for (Equalizer eq : daftarPreset) {
            halamanUtama.getComboPreset().addItem(eq.getNamaPreset());
            
            // Masukkan data baris demi baris ke JTable agar fungsi READ bernilai penuh di UI
            halamanUtama.getTableModel().addRow(new Object[]{
                eq.getId(), eq.getNamaPreset(), eq.getHz115(), eq.getHz250(), eq.getHz450(), eq.getHz13k()
            });
        }
        
        isUpdatingUI = false;

        if (halamanUtama.getComboPreset().getItemCount() > 0) {
            halamanUtama.getComboPreset().setSelectedIndex(0);
        }
    }

    // 2. FUNGSI TOMBOL ADD / SAVE
    public void insertPreset() {
        try {
            String nama = halamanUtama.getTxtPresetName().getText();
            
            if (nama.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama preset tidak boleh kosong!");
                return;
            }

            int val115 = halamanUtama.getSlider115().getValue();
            int val250 = halamanUtama.getSlider250().getValue();
            int val450 = halamanUtama.getSlider450().getValue();
            int val13k = halamanUtama.getSlider13k().getValue();

            Equalizer eq = new Equalizer(0, nama, val115, val250, val450, val13k);
            String tipePerangkat = halamanUtama.getComboTipePerangkat().getSelectedItem().toString();
            
            daoAudio.insert(eq, tipePerangkat);

            JOptionPane.showMessageDialog(null, "Preset Baru Berhasil Ditambahkan!");
            showAllPreset(); 

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // 3. FUNGSI TOMBOL UPDATE
    public void updatePreset() {
        try {
            if (activePresetId == -1) {
                JOptionPane.showMessageDialog(null, "Pilih preset dari dropdown/tabel dulu buat diupdate!");
                return;
            }

            String nama = halamanUtama.getTxtPresetName().getText();
            int val115 = halamanUtama.getSlider115().getValue();
            int val250 = halamanUtama.getSlider250().getValue();
            int val450 = halamanUtama.getSlider450().getValue();
            int val13k = halamanUtama.getSlider13k().getValue();

            Equalizer eq = new Equalizer(activePresetId, nama, val115, val250, val450, val13k);
            daoAudio.update(eq);

            JOptionPane.showMessageDialog(null, "Preset Berhasil Diupdate!");
            showAllPreset();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // 4. FUNGSI TOMBOL DELETE
    public void deletePreset() {
        if (activePresetId == -1) {
            JOptionPane.showMessageDialog(null, "Pilih preset dari dropdown/tabel dulu buat dihapus!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(null, "Yakin mau hapus preset ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            daoAudio.delete(activePresetId);
            activePresetId = -1;
            
            JOptionPane.showMessageDialog(null, "Preset Berhasil Dihapus!");
            clearForm();
            showAllPreset();
        }
    }

    // 5. FUNGSI TOMBOL CLEAR (Reset total text input dan slider)
    public void clearForm() {
        activePresetId = -1;
        halamanUtama.getTxtPresetName().setText("");
        halamanUtama.getSlider115().setValue(0);
        halamanUtama.getSlider250().setValue(0);
        halamanUtama.getSlider450().setValue(0);
        halamanUtama.getSlider13k().setValue(0);
        halamanUtama.getTabelPreset().clearSelection(); // Hilangkan seleksi warna biru di tabel
        bassGain = 1.0f; 
    }

    // 6. FUNGSI UNTUK MENGISI FIELD FORM SAAT COMBOBOX DIPILIH
    public void isiFieldDariComboBox() {
        if (isUpdatingUI) return;

        Object selectedObj = halamanUtama.getComboPreset().getSelectedItem();
        if (selectedObj != null) {
            String selectedNama = selectedObj.toString();
            
            for (Equalizer eq : daftarPreset) {
                if (eq.getNamaPreset().equals(selectedNama)) {
                    activePresetId = eq.getId(); 
                    
                    halamanUtama.getTxtPresetName().setText(eq.getNamaPreset());
                    halamanUtama.getSlider115().setValue((int) eq.getHz115());
                    halamanUtama.getSlider250().setValue((int) eq.getHz250());
                    halamanUtama.getSlider450().setValue((int) eq.getHz450());
                    halamanUtama.getSlider13k().setValue((int) eq.getHz13k());
                    
                    updateBassGain((int) eq.getHz115());
                    break;
                }
            }
        }
    }

    public void pilihFileAudio() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio Files (WAV & MP3)", "wav", "mp3");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(halamanUtama);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            currentAudioFilePath = selectedFile.getAbsolutePath();
            halamanUtama.getLblCurrentFile().setText("File: " + selectedFile.getName());
        }
    }

    public void updateBassGain(float sliderValue) {
        if (sliderValue >= 0) {
            bassGain = 1.0f + (sliderValue / 10.0f); 
        } else {
            bassGain = 1.0f + (sliderValue / 20.0f); 
        }
    }

    private void initListeners() {
        halamanUtama.getBtnSave().addActionListener(e -> insertPreset());
        halamanUtama.getBtnUpdate().addActionListener(e -> updatePreset());
        halamanUtama.getBtnDelete().addActionListener(e -> deletePreset());
        halamanUtama.getBtnClear().addActionListener(e -> clearForm()); // Listener tombol clear baru
        halamanUtama.getComboPreset().addActionListener(e -> isiFieldDariComboBox());
        halamanUtama.getBtnChooseFile().addActionListener(e -> pilihFileAudio());
        
        halamanUtama.getBtnPlay().addActionListener(e -> playAudio());
        halamanUtama.getBtnStop().addActionListener(e -> stopAudio());
        
        halamanUtama.getSlider115().addChangeListener(e -> {
            if (!isUpdatingUI) updateBassGain(halamanUtama.getSlider115().getValue());
        });

        // ACTION BARU: Sinkronisasi aksi klik baris JTable agar otomatis mengisi form slider & textfield
        halamanUtama.getTabelPreset().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int rowTerpilih = halamanUtama.getTabelPreset().getSelectedRow();
                if (rowTerpilih != -1) {
                    int targetId = (int) halamanUtama.getTabelPreset().getValueAt(rowTerpilih, 0);
                    for (Equalizer eq : daftarPreset) {
                        if (eq.getId() == targetId) {
                            activePresetId = eq.getId();
                            halamanUtama.getTxtPresetName().setText(eq.getNamaPreset());
                            halamanUtama.getSlider115().setValue((int) eq.getHz115());
                            halamanUtama.getSlider250().setValue((int) eq.getHz250());
                            halamanUtama.getSlider450().setValue((int) eq.getHz450());
                            halamanUtama.getSlider13k().setValue((int) eq.getHz13k());
                            updateBassGain((int) eq.getHz115());
                            break;
                        }
                    }
                }
            }
        });
    }

    public void stopAudio() {
        isPlaying = false;
    }

    public void playAudio() {
        if (isPlaying) return;
        if (currentAudioFilePath == null) {
            JOptionPane.showMessageDialog(halamanUtama, "Pilih file audio dulu cuy!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File audioFile = new File(currentAudioFilePath);
        if (!audioFile.exists()) return;

        String tipeAktif = halamanUtama.getComboTipePerangkat().getSelectedItem().toString();
        
        Equalizer eqAktif = new Equalizer(activePresetId, 
            halamanUtama.getTxtPresetName().getText(),
            halamanUtama.getSlider115().getValue(),
            halamanUtama.getSlider250().getValue(),
            halamanUtama.getSlider450().getValue(),
            halamanUtama.getSlider13k().getValue()
        );

        // =========================================================================
        // IMPLEMENTASI PILAR POLYMORPHISM (Dynamic Dispatch / Runtime Polymorphism)
        // Objek bertipe Kelas Parent (PerangkatAudio), namun di-instansiasi secara
        // dinamis pada runtime menggunakan Kelas Anak (IEM atau WirelessTWS)
        // =========================================================================
        PerangkatAudio perangkatAktif; 

        if (tipeAktif.equals("IEM")) {
            perangkatAktif = new IEM("Moondrop", "Dynamic Driver", eqAktif);
        } else {
            perangkatAktif = new WirelessTWS("Sony", "LDAC", eqAktif);
        }

        System.out.println("-------------------------------------------------");
        // Metode ini akan dipanggil secara polimorfis sesuai objek aslinya
        perangkatAktif.applyEqualizer(eqAktif);
        perangkatAktif.playAudio(currentAudioFilePath);
        System.out.println("-------------------------------------------------");

        audioThread = new Thread(() -> {
            try {
                AudioInputStream in = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat baseFormat = in.getFormat();
                
                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(), 16, 
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        baseFormat.getSampleRate(), false 
                );

                AudioInputStream ais = AudioSystem.getAudioInputStream(decodedFormat, in);
                int numChannels = decodedFormat.getChannels();

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                
                line.open(decodedFormat);
                line.start();
                isPlaying = true;

                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];
                int bytesRead = -1;
                int channel = 0; 

                while (isPlaying && (bytesRead = ais.read(buffer)) != -1) {
                    for (int i = 0; i < bytesRead; i += 2) { 
                        short sample = (short) ((buffer[i + 1] << 8) | (buffer[i] & 0xFF));
                        
                        float currentBass = (ALPHA * sample) + ((1.0f - ALPHA) * prevBass[channel]);
                        prevBass[channel] = currentBass; 
                        
                        float midTreble = sample - currentBass;
                        
                        float currentMidGain = 1.0f;
                        if (bassGain > 1.0f) {
                            currentMidGain = 1.0f - ((bassGain - 1.0f) * 0.15f); 
                            if (currentMidGain < 0.6f) currentMidGain = 0.6f; 
                        }
                        
                        float finalSampleFloat = (currentBass * bassGain) + (midTreble * currentMidGain);
                        int modifiedSample = (int) finalSampleFloat;
                        
                        if (modifiedSample > 32767) modifiedSample = 32767;
                        if (modifiedSample < -32768) modifiedSample = -32768;
                        
                        buffer[i] = (byte) (modifiedSample & 0xFF);
                        buffer[i + 1] = (byte) ((modifiedSample >> 8) & 0xFF);
                        
                        channel = (channel + 1) % numChannels; 
                    }
                    line.write(buffer, 0, bytesRead);
                }
                
                line.drain();
                line.close();
                ais.close();
                isPlaying = false;
            } catch (Exception ex) {
                isPlaying = false;
                ex.printStackTrace();
            }
        });
        audioThread.start();
    }
}