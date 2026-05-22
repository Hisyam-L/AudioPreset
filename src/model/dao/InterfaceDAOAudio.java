/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;
import model.entity.Equalizer;
import java.util.List;

public interface InterfaceDAOAudio {
    void insert(Equalizer eq, String tipePerangkat);
    void update(Equalizer eq);
    void delete(int id);
    List<Equalizer> getAll();
}