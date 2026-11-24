
package tn.esprit.tpautoloc.service.impl;

import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Vehicule;

import tn.esprit.tpautoloc.exception.ResourceNotFoundException;

import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;

import tn.esprit.tpautoloc.service.IVehiculeService;

import tn.esprit.tpautoloc.web.dto.VehiculeDTO;
import tn.esprit.tpautoloc.web.mapper.VehiculeMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private static final String VEHICULE = "Vehicule";
    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public VehiculeDTO create(VehiculeDTO dto) {

        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau véhicule ne doit pas avoir d'identifiant");
        }

        // Conversion DTO vers entité
        Vehicule vehicule = vehiculeMapper.toEntity(dto);

        // Association à une agence existante
        if (dto.getAgenceId() != null) {

            Agence agence = agenceRepository
                    .findById(dto.getAgenceId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Agence", dto.getAgenceId()));

            vehicule.setAgence(agence);
        }

        // Validation métier
        validate(vehicule);

        // Enregistrement
        Vehicule saved = vehiculeRepository.save(vehicule);

        // Conversion entité vers DTO
        return vehiculeMapper.toDto(saved);
    }

    // ================================
    // READ BY ID
    // ================================

    @Override
    @Transactional(readOnly = true)
    public VehiculeDTO findById(Long id) {

        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(VEHICULE, id));

        return vehiculeMapper.toDto(vehicule);
    }

    // ================================
    // READ ALL
    // ================================

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> findAll() {

        return vehiculeRepository.findAll()
                .stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    // ================================
    // UPDATE
    // ================================

    @Override
    public VehiculeDTO update(Long id, VehiculeDTO dto) {

        Vehicule existant = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(VEHICULE, id));

        // Conversion des nouveaux champs
        Vehicule modifications = vehiculeMapper.toEntity(dto);

        // Modification des attributs simples
        existant.setImmatriculation(
                modifications.getImmatriculation());

        existant.setMarque(modifications.getMarque());
        existant.setModele(modifications.getModele());

        existant.setTarifJournalier(
                modifications.getTarifJournalier());

        existant.setCategorie(modifications.getCategorie());
        existant.setStatut(modifications.getStatut());

        // L'agence et les relations existantes
        // ne sont pas modifiées par cette méthode

        validate(existant);

        Vehicule saved = vehiculeRepository.save(existant);

        return vehiculeMapper.toDto(saved);
    }

    // ================================
    // DELETE
    // ================================

    @Override
    public void deleteById(Long id) {

        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException(VEHICULE, id);
        }

        vehiculeRepository.deleteById(id);
    }

    // ================================
    // COUNT
    // ================================

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return vehiculeRepository.count();
    }

    // ================================
    // AFFECTATION A UNE AGENCE
    // ================================

    @Override
    public void affecterVehiculeAAgence(
            Long vehiculeId,
            Long agenceId) {

        Vehicule vehicule = vehiculeRepository
                .findById(vehiculeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                VEHICULE, vehiculeId));

        Agence agence = agenceRepository
                .findById(agenceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Agence", agenceId));

        // Affectation ou transfert
        vehicule.setAgence(agence);

        vehiculeRepository.save(vehicule);
    }

    // ================================
    // DESAFFECTATION
    // ================================

    @Override
    public void desaffecterVehiculeDeAgence(Long vehiculeId) {

        Vehicule vehicule = vehiculeRepository
                .findById(vehiculeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                VEHICULE, vehiculeId));

        if (vehicule.getAgence() == null) {
            throw new IllegalArgumentException(
                    "Ce véhicule n'est affecté à aucune agence");
        }

        // Suppression de l'association
        vehicule.setAgence(null);

        vehiculeRepository.save(vehicule);
    }

    // ================================
    // VALIDATION METIER
    // ================================

    private void validate(Vehicule vehicule) {

        if (vehicule.getTarifJournalier() != null
                && vehicule.getTarifJournalier()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Le tarif journalier ne peut pas être négatif");
        }
    }
}
