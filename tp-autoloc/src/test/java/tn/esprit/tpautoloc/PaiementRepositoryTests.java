package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.repository.IPaiementRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaiementRepositoryTests {

    @Autowired
    private IPaiementRepository paiementRepository;

    @Test
    void testPaiementRepository() {
        // 1. save()
        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("250.00"));
        paiement.setDatePaiement(LocalDate.now());
        // paiement.setModePaiement(ModePaiement.XXX);  // à ajouter si la colonne est obligatoire

        Paiement saved = paiementRepository.save(paiement);
        assertNotNull(saved.getIdPaiement());
        System.out.println("Paiement enregistré : id=" + saved.getIdPaiement()
                + ", montant=" + saved.getMontant());

        // 2. findAll()
        paiementRepository.findAll().forEach(p ->
                System.out.println("Paiement en base : " + p.getIdPaiement()
                        + " - " + p.getMontant() + " le " + p.getDatePaiement()));

        // 3. findById()
        Long id = saved.getIdPaiement();
        Optional<Paiement> found = paiementRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals(0, new BigDecimal("250.00").compareTo(found.get().getMontant()));
        System.out.println("findById : " + found.get().getMontant());

        // 4. existsById()
        assertTrue(paiementRepository.existsById(id));
        assertFalse(paiementRepository.existsById(9999L));

        // 5. deleteById()
        paiementRepository.deleteById(id);
        assertFalse(paiementRepository.existsById(id));
        System.out.println("Paiement supprimé : " + id);
    }
}