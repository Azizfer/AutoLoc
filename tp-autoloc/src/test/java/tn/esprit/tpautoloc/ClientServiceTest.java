
package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.service.IClientService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceTest {

    @Autowired
    private IClientService clientService;

    @Test
    void testClientService() {

        long countAvant = clientService.count();

        // 1. Créer deux clients
        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Sami");
        c1.setEmail("sami.benali@mail.com");
        c1.setTelephone("20111222");
        c1.setNumPermis("P-100200");
        c1.setDateInscription(LocalDate.now());

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Lina");
        c2.setEmail("lina.trabelsi@mail.com");
        c2.setTelephone("22333444");
        c2.setNumPermis("P-300400");
        c2.setDateInscription(LocalDate.now());

        // 2. Enregistrer les clients
        Client saved1 = clientService.create(c1);
        Client saved2 = clientService.create(c2);

        assertNotNull(saved1.getIdClient());
        assertNotNull(saved2.getIdClient());

        System.out.println("=== Clients enregistrés ===");
        System.out.println(saved1.getIdClient()
                + " - " + saved1.getNom());
        System.out.println(saved2.getIdClient()
                + " - " + saved2.getNom());

        // 3. findAll()
        List<Client> clients = clientService.findAll();

        System.out.println("=== Liste des clients ===");
        clients.forEach(c ->
                System.out.println(c.getIdClient()
                        + " - " + c.getNom()
                        + " " + c.getPrenom()));

        assertFalse(clients.isEmpty());

        // 4. findById()
        Long id1 = saved1.getIdClient();

        Client found = clientService.findById(id1);

        assertNotNull(found);
        assertEquals("Ben Ali", found.getNom());

        System.out.println("=== Client trouvé ===");
        System.out.println(found.getNom()
                + " " + found.getPrenom());

        // 5. count()
        long countApres = clientService.count();

        assertEquals(countAvant + 2, countApres);

        System.out.println("=== Nombre de clients : "
                + countApres + " ===");

        // 6. update()
        saved1.setTelephone("99111222");

        Client updated = clientService.update(id1, saved1);

        assertEquals("99111222", updated.getTelephone());

        System.out.println("=== Client modifié ===");
        System.out.println("Nouveau téléphone : "
                + updated.getTelephone());

        // 7. deleteById()
        clientService.deleteById(id1);
        clientService.deleteById(saved2.getIdClient());

        assertThrows(ResourceNotFoundException.class,
                () -> clientService.findById(id1));

        assertEquals(countAvant, clientService.count());

        System.out.println("=== Clients supprimés ===");
    }

    @Test
    void testValidationClient() {

        // Vérifier qu'un nom vide est refusé
        Client client = new Client();
        client.setNom("");
        client.setEmail("test@mail.com");

        assertThrows(IllegalArgumentException.class,
                () -> clientService.create(client));

        System.out.println(
                "=== Validation : nom vide refusé ===");
    }
}
