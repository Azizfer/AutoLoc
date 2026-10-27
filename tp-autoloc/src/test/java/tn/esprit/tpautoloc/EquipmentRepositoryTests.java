package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Equipement;
import tn.esprit.tpautoloc.repository.IEquipementRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EquipementRepositoryTests {

    @Autowired
    private IEquipementRepository equipementRepository;

    @Test
    void testEquipementRepository() {
        // 1. save()
        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS");

        Equipement saved = equipementRepository.save(equipement);
        assertNotNull(saved.getIdEquipement());
        System.out.println("Equipement enregistré : id=" + saved.getIdEquipement()
                + ", libelle=" + saved.getLibelle());

        // 2. findAll()
        equipementRepository.findAll().forEach(e ->
                System.out.println("Equipement en base : " + e.getIdEquipement()
                        + " - " + e.getLibelle()));

        // 3. findById()
        Long id = saved.getIdEquipement();
        Optional<Equipement> found = equipementRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("GPS", found.get().getLibelle());
        System.out.println("findById : " + found.get().getLibelle());

        // 4. existsById()
        assertTrue(equipementRepository.existsById(id));
        assertFalse(equipementRepository.existsById(9999L));

        // 5. deleteById()
        equipementRepository.deleteById(id);
        assertFalse(equipementRepository.existsById(id));
        System.out.println("Equipement supprimé : " + id);
    }
}