
package tn.esprit.tpautoloc.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.tpautoloc.domain.Maintenance;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IMaintenanceRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IMaintenanceService;
import tn.esprit.tpautoloc.web.dto.MaintenanceDTO;
import tn.esprit.tpautoloc.web.mapper.MaintenanceMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements IMaintenanceService {

    private static final String MAINTENANCE = "Maintenance";

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final MaintenanceMapper maintenanceMapper;

    @Override
    public MaintenanceDTO create(MaintenanceDTO dto) {

        Maintenance maintenance = maintenanceMapper.toEntity(dto);

        verifierMaintenance(maintenance);

        Vehicule vehicule = vehiculeRepository
                .findById(dto.getVehiculeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicule", dto.getVehiculeId()));

        maintenance.setVehicule(vehicule);

        return maintenanceMapper.toDto(
                maintenanceRepository.save(maintenance));
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceDTO findById(Long id) {

        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(MAINTENANCE, id));

        return maintenanceMapper.toDto(maintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceDTO> findAll() {

        return maintenanceRepository.findAll()
                .stream()
                .map(maintenanceMapper::toDto)
                .toList();
    }

    @Override
    public MaintenanceDTO update(Long id, MaintenanceDTO dto) {

        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(MAINTENANCE, id));

        Maintenance donnees = maintenanceMapper.toEntity(dto);

        verifierMaintenance(donnees);

        maintenance.setDescription(donnees.getDescription());
        maintenance.setDateDebut(donnees.getDateDebut());
        maintenance.setDateFin(donnees.getDateFin());

        if (dto.getVehiculeId() != null) {

            Vehicule vehicule = vehiculeRepository
                    .findById(dto.getVehiculeId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Vehicule", dto.getVehiculeId()));

            maintenance.setVehicule(vehicule);
        }

        return maintenanceMapper.toDto(
                maintenanceRepository.save(maintenance));
    }

    @Override
    public void deleteById(Long id) {

        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException(MAINTENANCE, id);
        }

        maintenanceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return maintenanceRepository.count();
    }

    private void verifierMaintenance(Maintenance maintenance) {
        if (maintenance.getDateDebut() == null
                || maintenance.getDateFin() == null) {

            throw new IllegalArgumentException(
                    "Les dates sont obligatoires");
        }

        if (maintenance.getDateFin()
                .isBefore(maintenance.getDateDebut())) {

            throw new IllegalArgumentException(
                    "La date de fin ne peut pas précéder la date de début");
        }
    }
}
