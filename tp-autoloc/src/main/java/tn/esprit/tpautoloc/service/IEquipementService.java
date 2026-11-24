
package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.EquipementDTO;

public interface IEquipementService {

    EquipementDTO create(EquipementDTO dto);

    EquipementDTO findById(Long id);

    List<EquipementDTO> findAll();

    EquipementDTO update(Long id, EquipementDTO dto);

    void deleteById(Long id);

    long count();
}
