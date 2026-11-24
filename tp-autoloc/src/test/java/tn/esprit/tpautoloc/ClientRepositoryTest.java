package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.repository.IClientRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientRepositoryTests {

    @Autowired
    private IClientRepository clientRepository;

    @Test
    void testClientRepository() {
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

        Client saved1 = clientRepository.save(c1);
        Client saved2 = clientRepository.save(c2);
        assertNotNull(saved1.getIdClient());
        assertNotNull(saved2.getIdClient());
        System.out.println("Clients enregistrés : " + saved1.getIdClient()
                + " et " + saved2.getIdClient());

        clientRepository.findAll().forEach(c ->
                System.out.println("Client en base : " + c.getIdClient() + " - "
                        + c.getNom() + " " + c.getPrenom()));

        Long id1 = saved1.getIdClient();
        Optional<Client> found = clientRepository.findById(id1);
        assertTrue(found.isPresent());
        System.out.println("findById : " + found.get().getNom());

        assertTrue(clientRepository.existsById(id1));
        assertFalse(clientRepository.existsById(9999L));

        long total = clientRepository.count();
        System.out.println("Nombre de clients : " + total);
        assertTrue(total >= 2);

        // nettoyage pour pouvoir relancer le test
        clientRepository.deleteById(id1);
        clientRepository.deleteById(saved2.getIdClient());
        assertFalse(clientRepository.existsById(id1));
    }
}