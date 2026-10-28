
package tn.esprit.tpautoloc.service.impl;

import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IContratRepository;
import tn.esprit.tpautoloc.service.IContratService;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat create(Contrat contrat) {

        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }

        verifierContrat(contrat);

        // Relier chaque paiement au contrat parent
        if (contrat.getPaiements() != null) {
            for (Paiement paiement : contrat.getPaiements()) {
                paiement.setContrat(contrat);
            }
        }

        // La cascade JPA enregistre aussi les paiements
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {

        Contrat existant = findById(id);

        verifierContrat(contrat);

        existant.setMontantTotal(contrat.getMontantTotal());
        existant.setDateSignature(contrat.getDateSignature());
        existant.setValide(contrat.getValide());

        // Ne pas remplacer la collection des paiements
        // Ne pas modifier la relation avec Reservation

        return contratRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {

        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }

        // La cascade JPA supprimera aussi les paiements
        contratRepository.deleteById(id);
    }

    private void verifierContrat(Contrat contrat) {

        if (contrat.getMontantTotal() != null
                && contrat.getMontantTotal()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Le montant total du contrat ne peut pas être négatif");
        }
    }
}
