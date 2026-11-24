
package tn.esprit.tpautoloc.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;
import tn.esprit.tpautoloc.web.mapper.AgenceMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final AgenceMapper agenceMapper;
    private final IAgenceRepository agenceRepository;

    @Override
    public AgenceDTO create(AgenceDTO dto) {

        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle agence ne doit pas avoir d'identifiant");
        }

        Agence agence = agenceMapper.toEntity(dto);

        verifierAgence(agence);

        Agence saved = agenceRepository.save(agence);

        return agenceMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AgenceDTO findById(Long id) {

        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agence", id));

        return agenceMapper.toDto(agence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgenceDTO> findAll() {

        return agenceRepository.findAll()
                .stream()
                .map(agenceMapper::toDto)
                .toList();
    }

    @Override
    public AgenceDTO update(Long id, AgenceDTO dto) {

        Agence existante = agenceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agence", id));

        existante.setNom(dto.getNom());
        existante.setVille(dto.getVille());
        existante.setAdresse(dto.getAdresse());
        existante.setTelephone(dto.getTelephone());

        // Validation métier conservée
        verifierAgence(existante);

        // Les collections employes et vehicules
        // ne sont pas modifiées

        Agence saved = agenceRepository.save(existante);

        return agenceMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {

        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }

        agenceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return agenceRepository.count();
    }

    private void verifierAgence(Agence agence) {

        if (agence.getNom() == null
                || agence.getNom().isBlank()) {

            throw new IllegalArgumentException(
                    "Le nom de l'agence est obligatoire");
        }
    }
}
