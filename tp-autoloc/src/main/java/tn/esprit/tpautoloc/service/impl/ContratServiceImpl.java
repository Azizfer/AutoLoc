
package tn.esprit.tpautoloc.service.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.domain.Reservation;

import tn.esprit.tpautoloc.exception.ResourceNotFoundException;

import tn.esprit.tpautoloc.repository.IContratRepository;
import tn.esprit.tpautoloc.repository.IReservationRepository;

import tn.esprit.tpautoloc.service.IContratService;

import tn.esprit.tpautoloc.web.dto.ContratDTO;
import tn.esprit.tpautoloc.web.dto.ContratCreationDTO;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;

import tn.esprit.tpautoloc.web.mapper.ContratMapper;
import tn.esprit.tpautoloc.web.mapper.PaiementMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;

    private final ContratMapper contratMapper;
    private final PaiementMapper paiementMapper;

    @Override
    public ContratDTO create(ContratCreationDTO dto) {

        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }

        // DTO vers entité
        Contrat contrat = contratMapper.toEntity(dto);

        // Association éventuelle à une réservation
        if (dto.getReservationId() != null) {

            Reservation reservation = reservationRepository
                    .findById(dto.getReservationId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Reservation",
                                    dto.getReservationId()));

            contrat.setReservation(reservation);
        }

        // Vérification métier
        verifierContrat(contrat);

        // Préparer les paiements associés
        if (dto.getPaiements() != null) {

            if (contrat.getPaiements() == null) {
                contrat.setPaiements(new ArrayList<>());
            }

            for (PaiementDTO paiementDTO : dto.getPaiements()) {

                if (paiementDTO == null ||
                        paiementDTO.getId() != null) {
                    throw new IllegalArgumentException(
                            "Un nouveau paiement doit être valide et sans identifiant");
                }

                if (paiementDTO.getMontant() == null ||
                        paiementDTO.getMontant().signum() <= 0) {
                    throw new IllegalArgumentException(
                            "Le montant du paiement doit être positif");
                }

                Paiement paiement =
                        paiementMapper.toEntity(paiementDTO);

                // Côté propriétaire de la relation
                paiement.setContrat(contrat);

                contrat.getPaiements().add(paiement);
            }
        }

        // Enregistrement du contrat et des paiements
        // grâce à CascadeType.ALL
        Contrat saved = contratRepository.save(contrat);

        return contratMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ContratDTO findById(Long id) {

        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrat", id));

        return contratMapper.toDto(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratDTO> findAll() {

        return contratRepository.findAll()
                .stream()
                .map(contratMapper::toDto)
                .toList();
    }

    @Override
    public ContratDTO update(Long id, ContratDTO dto) {

        Contrat existant = contratRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrat", id));

        // Modifier uniquement les attributs simples
        existant.setDateSignature(dto.getDateSignature());
        existant.setMontantTotal(dto.getMontantTotal());
        existant.setValide(dto.getValide());

        // Conserver la réservation et les paiements
        verifierContrat(existant);

        Contrat saved = contratRepository.save(existant);

        return contratMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {

        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }

        contratRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return contratRepository.count();
    }

    private void verifierContrat(Contrat contrat) {

        if (contrat.getMontantTotal() == null ||
                contrat.getMontantTotal().signum() < 0) {

            throw new IllegalArgumentException(
                    "Le montant total ne peut pas être nul ou négatif");
        }
    }
}
