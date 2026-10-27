package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VehiculeRepositoryTests {

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Test
    void testVehiculeRepository() {
        // l'agence est enregistrée d'abord : le véhicule y fait référence (agence_id)
        Agence agence = new Agence();
        agence.setNom("Agence Sfax");
        agence.setVille("Sfax");
        agence.setAdresse("5 Rue de la Liberté");
        agence.setTelephone("74555666");
        Agence savedAgence = agenceRepository.save(agence);

        // 1. save()
        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Peugeot");
        v.setModele("208");
        v.setTarifJournalier(new BigDecimal("85.00"));
        v.setAgence(savedAgence);

        Vehicule saved = vehiculeRepository.save(v);
        assertNotNull(saved.getIdVehicule());
        System.out.println("Véhicule enregistré : id=" + saved.getIdVehicule()
                + ", " + saved.getMarque() + " " + saved.getModele());

        // 2. findAll()
        vehiculeRepository.findAll().forEach(x ->
                System.out.println("Véhicule en base : " + x.getIdVehicule() + " - "
                        + x.getMarque() + " " + x.getModele()));

        // 3. count()
        long total = vehiculeRepository.count();
        System.out.println("Nombre de véhicules : " + total);
        assertTrue(total >= 1);

        // 4. modification : save() sur un objet qui a déjà un id fait un UPDATE
        saved.setModele("308");
        Vehicule updated = vehiculeRepository.save(saved);
        assertEquals("308", updated.getModele());
        System.out.println("Véhicule modifié : " + updated.getModele());

        // 5. deleteById() puis vérification
        Long id = saved.getIdVehicule();
        vehiculeRepository.deleteById(id);
        assertFalse(vehiculeRepository.existsById(id));
        System.out.println("Véhicule supprimé : " + id);

        // nettoyage : le véhicule est supprimé avant l'agence (clé étrangère)
        agenceRepository.deleteById(savedAgence.getIdAgence());
    }
}