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
    
    // Variabel Pelacak Database
    private int activePresetId = -1;
    private boolean isUpdatingUI = false;

    // Variabel Audio & DSP
    private Thread audioThread;
    private boolean isPlaying = false;
    private String currentAudioFilePath = null;
    private volatile float bassGain = 1.0f;
    private float[] prevBass = new float[2];
    private final float ALPHA = 0.05f;

    public ControllerAudioPreset(IEMView halamanUtama) {
        this.halamanUtama = halamanUtama;
        this.daoAudio = new PerangkatAudioDAOImpl();
        initListeners(); // Memasang tombol ke fungsi-fungsi di bawah
        showAllPreset();
    }

    // 1. FUNGSI UNTUK MENAMPILKAN DATA KE COMBOBOX (Menggantikan tabel)
    public void showAllPreset() {
        isUpdatingUI = true;
        halamanUtama.getComboPreset().removeAllItems();
        
        daftarPreset = daoAudio.getAll();
        for (Equalizer eq : daftarPreset) {
            halamanUtama.getComboPreset().addItem(eq.getNamaPreset());
        }
        
        isUpdatingUI = false;

        // Otomatis pilih item teratas jika ada datanya
        if (halamanUtama.getComboPreset().getItemCount() > 0) {
            halamanUtama.getComboPreset().setSelectedIndex(0);
        }
    }

    // 2. FUNGSI UNTUK TOMBOL ADD / SAVE
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
            
            // PERBAIKAN: Ambil tipe perangkat dari combobox yang baru kita buat
            String tipePerangkat = halamanUtama.getComboTipePerangkat().getSelectedItem().toString();
            
            // Simpan sesuai dengan tipe yang dipilih (bukan di-hardcode "IEM" lagi)
            daoAudio.insert(eq, tipePerangkat);

            JOptionPane.showMessageDialog(null, "Preset Baru Berhasil Ditambahkan!");
            showAllPreset(); // Refresh combobox

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // 3. FUNGSI UNTUK TOMBOL UPDATE
    public void updatePreset() {
        try {
            if (activePresetId == -1) {
                JOptionPane.showMessageDialog(null, "Pilih preset dari dropdown dulu buat diupdate!");
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

    // 4. FUNGSI UNTUK TOMBOL DELETE
    public void deletePreset() {
        if (activePresetId == -1) {
            JOptionPane.showMessageDialog(null, "Pilih preset dari dropdown dulu buat dihapus!");
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

    // 5. FUNGSI UNTUK TOMBOL CLEAR (Reset input)
    public void clearForm() {
        activePresetId = -1;
        halamanUtama.getTxtPresetName().setText("");
        halamanUtama.getSlider115().setValue(0);
        halamanUtama.getSlider250().setValue(0);
        halamanUtama.getSlider450().setValue(0);
        halamanUtama.getSlider13k().setValue(0);
        bassGain = 1.0f; // Reset gain DSP
    }

    // 6. FUNGSI UNTUK MENGISI FORM SAAT COMBOBOX DIPILIH
    public void isiFieldDariComboBox() {
        if (isUpdatingUI) return;

        Object selectedObj = halamanUtama.getComboPreset().getSelectedItem();
        if (selectedObj != null) {
            String selectedNama = selectedObj.toString();
            
            for (Equalizer eq : daftarPreset) {
                if (eq.getNamaPreset().equals(selectedNama)) {
                    activePresetId = eq.getId(); // Simpan ID untuk update/delete
                    
                    // Masukkan datanya ke komponen View
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

    // 7. FUNGSI UNTUK MEMILIH FILE AUDIO
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

    // 8. FUNGSI UNTUK KALKULASI DSP SAAT SLIDER DIGESER
    public void updateBassGain(float sliderValue) {
        if (sliderValue >= 0) {
            bassGain = 1.0f + (sliderValue / 10.0f); 
        } else {
            bassGain = 1.0f + (sliderValue / 20.0f); 
        }
    }


    // Fungsi ini berfungsi menyambungkan tombol View dengan fungsi-fungsi publik di atas
    private void initListeners() {
        halamanUtama.getBtnSave().addActionListener(e -> insertPreset());
        halamanUtama.getBtnUpdate().addActionListener(e -> updatePreset());
        halamanUtama.getBtnDelete().addActionListener(e -> deletePreset());
        halamanUtama.getComboPreset().addActionListener(e -> isiFieldDariComboBox());
        halamanUtama.getBtnChooseFile().addActionListener(e -> pilihFileAudio());
        
        halamanUtama.getBtnPlay().addActionListener(e -> playAudio());
        halamanUtama.getBtnStop().addActionListener(e -> stopAudio());
        
        halamanUtama.getSlider115().addChangeListener(e -> {
            if (!isUpdatingUI) updateBassGain(halamanUtama.getSlider115().getValue());
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

        PerangkatAudio perangkatAktif; // Variabel Parent (Abstract)

        // Instansiasi objek Child berdasarkan pilihan UI
        if (tipeAktif.equals("IEM")) {
            perangkatAktif = new IEM("Moondrop", "Dynamic Driver", eqAktif);
        } else {
            perangkatAktif = new WirelessTWS("Sony", "LDAC", eqAktif);
        }

        // Panggil method overriding (Outputnya akan muncul di Terminal/Console IDE)
        System.out.println("-------------------------------------------------");
        perangkatAktif.applyEqualizer(eqAktif);
        perangkatAktif.playAudio(currentAudioFilePath);
        System.out.println("-------------------------------------------------");
        // =========================================================================

        audioThread = new Thread(() -> {
            try {
                AudioInputStream in = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat baseFormat = in.getFormat();
                
                // ... (SISA KODE KE BAWAHNYA TETAP SAMA SEPERTI SEBELUMNYA) ...
                
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