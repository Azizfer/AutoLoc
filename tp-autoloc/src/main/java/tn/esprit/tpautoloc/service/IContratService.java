
package tn.esprit.tpautoloc.service;

import java.util.List;

import tn.esprit.tpautoloc.web.dto.ContratDTO;
import tn.esprit.tpautoloc.web.dto.ContratCreationDTO;

public interface IContratService {

    ContratDTO create(ContratCreationDTO dto);

    ContratDTO findById(Long id);

    List<ContratDTO> findAll();

    ContratDTO update(Long id, ContratDTO dto);

    void deleteById(Long id);

    long count();
}
