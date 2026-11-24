
package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.ClientDTO;

public interface IClientService {

    ClientDTO create(ClientDTO dto);

    ClientDTO update(Long id, ClientDTO dto);

    ClientDTO findById(Long id);

    List<ClientDTO> findAll();

    void deleteById(Long id);

    long count();
}
