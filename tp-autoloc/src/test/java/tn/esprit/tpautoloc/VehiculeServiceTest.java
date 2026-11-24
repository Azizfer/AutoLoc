
package tn.esprit.tpautoloc;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

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
        AgenceDTO agence = new AgenceDTO();
        agence.setNom("Agence Test Vehicule");
        agence.setVille("Tunis");
        agence.setAdresse("Centre-ville Tunis");
        agence.setTelephone("71234567");

        AgenceDTO agenceSaved = agenceService.create(agence);

        assertNotNull(agenceSaved);
        assertNotNull(agenceSaved.getId());

        Long agenceId = agenceSaved.getId();

        // 2. Créer un véhicule
        VehiculeDTO vehicule = new VehiculeDTO();

        vehicule.setMarque("Toyota");
        vehicule.setModele("Yaris");
        vehicule.setImmatriculation("123 TU 4567");
        vehicule.setCategorie("CITADINE");
        vehicule.setStatut("DISPONIBLE");
        vehicule.setTarifJournalier(
                new BigDecimal("100.00"));
        vehicule.setAgenceId(agenceId);

        VehiculeDTO saved = vehiculeService.create(vehicule);

        assertNotNull(saved);
        assertNotNull(saved.getId());

        Long vehiculeId = saved.getId();

        System.out.println("=== Véhicule enregistré ===");
        System.out.println(saved.getId()
                + " - " + saved.getMarque()
                + " " + saved.getModele());

        // 3. findAll()
        List<VehiculeDTO> vehicules =
                vehiculeService.findAll();

        assertFalse(vehicules.isEmpty());

        System.out.println("=== Liste des véhicules ===");

        vehicules.forEach(v ->
                System.out.println(v.getId()
                        + " - " + v.getMarque()
                        + " " + v.getModele()));

        // 4. findById()
        VehiculeDTO found =
                vehiculeService.findById(vehiculeId);

        assertNotNull(found);
        assertEquals(vehiculeId, found.getId());
        assertEquals("Toyota", found.getMarque());
        assertEquals(agenceId, found.getAgenceId());

        // 5. count()
        assertEquals(
                countAvant + 1,
                vehiculeService.count());

        // 6. update()
        saved.setTarifJournalier(
                new BigDecimal("150.00"));

        VehiculeDTO updated =
                vehiculeService.update(vehiculeId, saved);

        assertNotNull(updated);

        assertEquals(
                0,
                new BigDecimal("150.00")
                        .compareTo(updated.getTarifJournalier()));

        System.out.println("=== Véhicule modifié ===");
        System.out.println("Tarif : "
                + updated.getTarifJournalier());

        // 7. deleteById()
        vehiculeService.deleteById(vehiculeId);

        assertThrows(
                ResourceNotFoundException.class,
                () -> vehiculeService.findById(vehiculeId));

        assertEquals(
                countAvant,
                vehiculeService.count());

        // 8. Supprimer l'agence de test
        agenceService.deleteById(agenceId);

        System.out.println("=== Tests véhicule terminés ===");
    }

    @Test
    void testValidationVehicule() {

        VehiculeDTO vehicule = new VehiculeDTO();

        vehicule.setMarque("Renault");
        vehicule.setModele("Clio");
        vehicule.setImmatriculation("999 TU 9999");
        vehicule.setCategorie("CITADINE");
        vehicule.setStatut("DISPONIBLE");
        vehicule.setTarifJournalier(
                new BigDecimal("-50.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> vehiculeService.create(vehicule));

        System.out.println(
                "=== Validation : tarif négatif refusé ===");
    }
}
