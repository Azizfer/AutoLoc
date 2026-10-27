package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.service.IAgenceService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgencyServiceTest {

    @Autowired
    private IAgenceService agenceService;

    @Test
    void testAgencyService() {
        long countAvant = agenceService.count();

        // 1. Créer et enregistrer une agence
        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");   // à compléter avec les autres attributs de votre entité

        Agence saved = agenceService.create(agence);

        // 2. Afficher l'objet retourné
        System.out.println("=== Agence enregistrée ===");
        System.out.println("ID : " + saved.getIdAgence() + " | Nom : " + saved.getNom());
        assertNotNull(saved);
        assertNotNull(saved.getIdAgence());
        assertEquals("Agence Tunis Centre", saved.getNom());

        // 3. findAll()
        List<Agence> agences = agenceService.findAll();
        System.out.println("=== Liste des agences ===");
        agences.forEach(a -> System.out.println("ID : " + a.getIdAgence() + " | Nom : " + a.getNom()));
        assertFalse(agences.isEmpty());

        // 4. findById()
        Agence trouvee = agenceService.findById(saved.getIdAgence());
        System.out.println("=== Agence trouvée par id ===");
        System.out.println("ID : " + trouvee.getIdAgence() + " | Nom : " + trouvee.getNom());
        assertNotNull(trouvee);
        assertEquals(saved.getIdAgence(), trouvee.getIdAgence());

        // 5. count()
        long countApres = agenceService.count();
        System.out.println("=== Nombre d'agences : " + countApres + " ===");
        assertEquals(countAvant + 1, countApres);

        // 6. deleteById() puis vérification
        agenceService.deleteById(saved.getIdAgence());
        System.out.println("=== Agence supprimée (id " + saved.getIdAgence() + ") ===");

        assertThrows(ResourceNotFoundException.class,
                () -> agenceService.findById(saved.getIdAgence()));
        assertEquals(countAvant, agenceService.count());
        System.out.println("=== Vérification : l'agence n'existe plus ===");
    }
}