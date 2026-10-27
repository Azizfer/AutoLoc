package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.repository.IAgenceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgenceRepositoryTests {

    @Autowired
    private IAgenceRepository IAgenceRepository;

    @Test
    void testAgenceRepository() {
        // 1. Create
        Agence agence = new Agence();
        agence.setNom("AutoLoc Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("12 Avenue Habib Bourguiba");
        agence.setTelephone("71123456");

        // 2. save()
        Agence saved = IAgenceRepository.save(agence);
        System.out.println("Agence enregistrée : id=" + saved.getIdAgence()
                + ", nom=" + saved.getNom() + ", ville=" + saved.getVille());
        assertNotNull(saved);
        assertNotNull(saved.getIdAgence());
        assertEquals("Tunis", saved.getVille());

        // 3. findAll()
        IAgenceRepository.findAll().forEach(a ->
                System.out.println("Agence en base : " + a.getIdAgence() + " - " + a.getNom()));

        // 4. findById()
        Long id = saved.getIdAgence();
        Optional<Agence> found = IAgenceRepository.findById(id);
        assertTrue(found.isPresent());
        System.out.println("findById : " + found.get().getNom());

        // 5. existsById()
        assertTrue(IAgenceRepository.existsById(id));

        // 6. count()
        long total = IAgenceRepository.count();
        System.out.println("Nombre d'agences : " + total);
        assertTrue(total >= 1);

        // 7. deleteById() puis vérification
        IAgenceRepository.deleteById(id);
        assertFalse(IAgenceRepository.existsById(id));
        System.out.println("Agence supprimée : " + id);
    }
}