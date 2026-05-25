package model.dao;
import model.entity.Equalizer;
import java.util.List;

public interface InterfaceDAOAudio {
    void insert(Equalizer eq);
    void update(Equalizer eq);
    void delete(int id);
    List<Equalizer> getAll();
}