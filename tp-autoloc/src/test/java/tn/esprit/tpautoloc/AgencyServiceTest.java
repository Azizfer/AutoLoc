
package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;

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
        AgenceDTO agence = new AgenceDTO();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("Centre-ville Tunis");
        agence.setTelephone("71234567");

        AgenceDTO saved = agenceService.create(agence);

        // 2. Vérifier et afficher l'agence créée
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Agence Tunis Centre", saved.getNom());

        Long agenceId = saved.getId();

        System.out.println("=== Agence enregistrée ===");
        System.out.println("ID : " + agenceId
                + " | Nom : " + saved.getNom());

        // 3. findAll()
        List<AgenceDTO> agences = agenceService.findAll();

        System.out.println("=== Liste des agences ===");
        agences.forEach(a ->
                System.out.println("ID : " + a.getId()
                        + " | Nom : " + a.getNom()));

        assertFalse(agences.isEmpty());

        // 4. findById()
        AgenceDTO trouvee = agenceService.findById(agenceId);

        assertNotNull(trouvee);
        assertEquals(agenceId, trouvee.getId());
        assertEquals("Agence Tunis Centre", trouvee.getNom());

        System.out.println("=== Agence trouvée par ID ===");
        System.out.println("ID : " + trouvee.getId()
                + " | Nom : " + trouvee.getNom());

        // 5. count()
        long countApres = agenceService.count();

        assertEquals(countAvant + 1, countApres);

        System.out.println("=== Nombre d'agences : "
                + countApres + " ===");

        // 6. deleteById()
        agenceService.deleteById(agenceId);

        System.out.println("=== Agence supprimée (ID "
                + agenceId + ") ===");

        // 7. Vérifier que l'agence n'existe plus
        assertThrows(
                ResourceNotFoundException.class,
                () -> agenceService.findById(agenceId)
        );

        assertEquals(countAvant, agenceService.count());

        System.out.println(
                "=== Vérification : l'agence n'existe plus ===");
    }
}
