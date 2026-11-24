
package tn.esprit.tpautoloc.service;

import java.util.List;

import tn.esprit.tpautoloc.web.dto.EmployeDTO;

public interface IEmployeService {

    EmployeDTO create(EmployeDTO dto);

    EmployeDTO findById(Long id);

    List<EmployeDTO> findAll();

    EmployeDTO update(Long id, EmployeDTO dto);

    void deleteById(Long id);

    long count();

    void affecterEmployeAAgence(Long employeId, Long agenceId);

    void desaffecterEmployeDeAgence(Long employeId);
}
