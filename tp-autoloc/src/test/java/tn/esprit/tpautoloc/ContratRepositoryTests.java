
package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.repository.IContratRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContratRepositoryTests {

    @Autowired
    private IContratRepository contratRepository;

    @Test
    void testContratRepository() {

        long countAvant = contratRepository.count();

        // 1. Créer un contrat
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("1500.00"));
        contrat.setValide(true);

        // 2. save()
        Contrat saved = contratRepository.save(contrat);

        assertNotNull(saved);
        assertNotNull(saved.getIdContrat());

        System.out.println("=== Contrat enregistré ===");
        System.out.println("ID : " + saved.getIdContrat()
                + " | Montant : " + saved.getMontantTotal()
                + " | Valide : " + saved.getValide());

        // 3. findAll()
        System.out.println("=== Liste des contrats ===");

        contratRepository.findAll().forEach(c ->
                System.out.println("ID : " + c.getIdContrat()
                        + " | Montant : " + c.getMontantTotal()));

        // 4. findById()
        Long id = saved.getIdContrat();

        Optional<Contrat> found = contratRepository.findById(id);

        assertTrue(found.isPresent());
        assertEquals(id, found.get().getIdContrat());
        assertEquals(0, new BigDecimal("1500.00")
                .compareTo(found.get().getMontantTotal()));

        System.out.println("=== Contrat trouvé par ID ===");
        System.out.println("Montant : " + found.get().getMontantTotal());

        // 5. existsById()
        assertTrue(contratRepository.existsById(id));

        System.out.println("=== Le contrat existe ===");

        // 6. count()
        long countApres = contratRepository.count();

        assertEquals(countAvant + 1, countApres);

        System.out.println("=== Nombre de contrats : "
                + countApres + " ===");

        // 7. update avec save()
        saved.setMontantTotal(new BigDecimal("1800.00"));

        Contrat updated = contratRepository.save(saved);

        assertEquals(0, new BigDecimal("1800.00")
                .compareTo(updated.getMontantTotal()));

        System.out.println("=== Contrat modifié ===");
        System.out.println("Nouveau montant : "
                + updated.getMontantTotal());

        // 8. deleteById()
        contratRepository.deleteById(id);

        assertFalse(contratRepository.existsById(id));
        assertEquals(countAvant, contratRepository.count());

        System.out.println("=== Contrat supprimé : "
                + id + " ===");
    }
}
