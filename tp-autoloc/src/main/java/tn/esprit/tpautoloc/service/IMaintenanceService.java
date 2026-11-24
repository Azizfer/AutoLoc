
package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.MaintenanceDTO;

public interface IMaintenanceService {

    MaintenanceDTO create(MaintenanceDTO dto);

    MaintenanceDTO findById(Long id);

    List<MaintenanceDTO> findAll();

    MaintenanceDTO update(Long id, MaintenanceDTO dto);

    void deleteById(Long id);

    long count();
}
