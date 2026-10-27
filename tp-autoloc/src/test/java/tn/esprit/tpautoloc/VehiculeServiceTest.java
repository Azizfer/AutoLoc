
package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.service.IVehiculeService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VehiculeServiceTest {

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IAgenceService agenceService;

    @Test
    void testVehiculeService() {

        long countAvant = vehiculeService.count();

        // 1. Créer une agence
        Agence agence = new Agence();
        agence.setNom("Agence AutoLoc Tunis");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("71123456");

        Agence savedAgence = agenceService.create(agence);

        // 2. Créer un véhicule
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("123 TUN 4567");
        vehicule.setMarque("Peugeot");
        vehicule.setModele("208");
        vehicule.setTarifJournalier(
                new BigDecimal("85.00"));
        vehicule.setCategorie(
                CategorieVehicule.CITADINE);
        vehicule.setStatut(
                StatutVehicule.DISPONIBLE);
        vehicule.setAgence(savedAgence);

        // 3. Enregistrer le véhicule
        Vehicule saved = vehiculeService.create(vehicule);

        assertNotNull(saved);
        assertNotNull(saved.getIdVehicule());

        Long id = saved.getIdVehicule();

        System.out.println("=== Véhicule enregistré ===");
        System.out.println("ID : " + id
                + " | Marque : " + saved.getMarque()
                + " | Modèle : " + saved.getModele());

        // 4. findAll()
        List<Vehicule> vehicules =
                vehiculeService.findAll();

        System.out.println("=== Liste des véhicules ===");

        vehicules.forEach(v ->
                System.out.println(v.getIdVehicule()
                        + " - " + v.getMarque()
                        + " " + v.getModele()));

        assertFalse(vehicules.isEmpty());

        // 5. findById()
        Vehicule found = vehiculeService.findById(id);

        assertNotNull(found);
        assertEquals("Peugeot", found.getMarque());

        System.out.println("=== Véhicule trouvé ===");
        System.out.println(found.getMarque()
                + " " + found.getModele());

        // 6. count()
        long countApres = vehiculeService.count();

        assertEquals(countAvant + 1, countApres);

        System.out.println("=== Nombre de véhicules : "
                + countApres + " ===");

        // 7. update()
        saved.setModele("308");
        saved.setTarifJournalier(
                new BigDecimal("100.00"));

        Vehicule updated =
                vehiculeService.update(id, saved);

        assertEquals("308", updated.getModele());
        assertEquals(0, new BigDecimal("100.00")
                .compareTo(updated.getTarifJournalier()));

        System.out.println("=== Véhicule modifié ===");
        System.out.println("Nouveau modèle : "
                + updated.getModele());
        System.out.println("Nouveau tarif : "
                + updated.getTarifJournalier());

        // 8. deleteById()
        vehiculeService.deleteById(id);

        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.findById(id));

        assertEquals(countAvant, vehiculeService.count());

        System.out.println("=== Véhicule supprimé ===");

        // 9. Supprimer l'agence créée pour le test
        agenceService.deleteById(savedAgence.getIdAgence());

        System.out.println("=== Agence de test supprimée ===");
    }

    @Test
    void testValidationVehicule() {

        // Vérifier qu'un tarif négatif est refusé
        Vehicule vehicule = new Vehicule();
        vehicule.setTarifJournalier(
                new BigDecimal("-50.00"));

        assertThrows(IllegalArgumentException.class,
                () -> vehiculeService.create(vehicule));

        System.out.println(
                "=== Validation : tarif négatif refusé ===");
    }
}
