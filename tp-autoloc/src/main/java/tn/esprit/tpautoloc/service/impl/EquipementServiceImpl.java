
package tn.esprit.tpautoloc.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.tpautoloc.domain.Equipement;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IEquipementRepository;
import tn.esprit.tpautoloc.service.IEquipementService;
import tn.esprit.tpautoloc.web.dto.EquipementDTO;
import tn.esprit.tpautoloc.web.mapper.EquipementMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementServiceImpl implements IEquipementService {

    private static final String EQUIPEMENT = "Equipement";

    private final IEquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;

    @Override
    public EquipementDTO create(EquipementDTO dto) {

        Equipement equipement = equipementMapper.toEntity(dto);

        verifierEquipement(equipement);

        return equipementMapper.toDto(
                equipementRepository.save(equipement));
    }

    @Override
    @Transactional(readOnly = true)
    public EquipementDTO findById(Long id) {

        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(EQUIPEMENT, id));

        return equipementMapper.toDto(equipement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipementDTO> findAll() {

        return equipementRepository.findAll()
                .stream()
                .map(equipementMapper::toDto)
                .toList();
    }

    @Override
    public EquipementDTO update(Long id, EquipementDTO dto) {

        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(EQUIPEMENT, id));

        Equipement donnees = equipementMapper.toEntity(dto);

        verifierEquipement(donnees);

        equipement.setLibelle(donnees.getLibelle());

        return equipementMapper.toDto(
                equipementRepository.save(equipement));
    }

    @Override
    public void deleteById(Long id) {

        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException(EQUIPEMENT, id);
        }

        equipementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return equipementRepository.count();
    }

    private void verifierEquipement(Equipement equipement) {

        if (equipement.getLibelle() == null
                || equipement.getLibelle().isBlank()) {

            throw new IllegalArgumentException(
                    "Le libellé est obligatoire");
        }
    }
}
