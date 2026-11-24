
package tn.esprit.tpautoloc.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IEmployeRepository;
import tn.esprit.tpautoloc.service.IEmployeService;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;
import tn.esprit.tpautoloc.web.mapper.EmployeMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {
    private static final String AGENCE = "Agence";
    private static final String EMPLOYE = "Employe";

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    @Override
    public EmployeDTO create(EmployeDTO dto) {

        Employe employe = employeMapper.toEntity(dto);

        verifierEmploye(employe);

        if (dto.getAgenceId() != null) {

            Agence agence = agenceRepository
                    .findById(dto.getAgenceId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    AGENCE, dto.getAgenceId()));

            employe.setAgence(agence);
        }

        Employe saved = employeRepository.save(employe);

        return employeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeDTO findById(Long id) {

        Employe employe = employeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(EMPLOYE, id));

        return employeMapper.toDto(employe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> findAll() {

        return employeRepository.findAll()
                .stream()
                .map(employeMapper::toDto)
                .toList();
    }

    @Override
    public EmployeDTO update(Long id, EmployeDTO dto) {

        Employe employe = employeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(EMPLOYE, id));

        Employe donnees = employeMapper.toEntity(dto);

        verifierEmploye(donnees);

        employe.setNom(donnees.getNom());
        employe.setPrenom(donnees.getPrenom());
        employe.setRole(donnees.getRole());

        if (dto.getAgenceId() != null) {

            Agence agence = agenceRepository
                    .findById(dto.getAgenceId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    AGENCE, dto.getAgenceId()));

            employe.setAgence(agence);
        }

        return employeMapper.toDto(
                employeRepository.save(employe));
    }

    @Override
    public void deleteById(Long id) {

        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException(EMPLOYE, id);
        }

        employeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {

        return employeRepository.count();
    }

    @Override
    public void affecterEmployeAAgence(
            Long employeId, Long agenceId) {

        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                EMPLOYE, employeId));

        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                AGENCE, agenceId));

        employe.setAgence(agence);

        employeRepository.save(employe);
    }

    @Override
    public void desaffecterEmployeDeAgence(Long employeId) {

        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                EMPLOYE, employeId));

        if (employe.getAgence() == null) {
            throw new IllegalArgumentException(
                    "Employé déjà sans agence");
        }

        employe.setAgence(null);

        employeRepository.save(employe);
    }

    private void verifierEmploye(Employe employe) {

        if (employe.getNom() == null
                || employe.getNom().isBlank()) {

            throw new IllegalArgumentException(
                    "Le nom est obligatoire");
        }

        if (employe.getPrenom() == null
                || employe.getPrenom().isBlank()) {

            throw new IllegalArgumentException(
                    "Le prénom est obligatoire");
        }

        if (employe.getRole() == null) {

            throw new IllegalArgumentException(
                    "Le rôle est obligatoire");
        }
    }
}
