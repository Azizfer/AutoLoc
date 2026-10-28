
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.service.IClientService;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(
        name = "Clients",
        description = "Gestion des clients AutoLoc"
)
public class ClientController {

    private final IClientService clientService;

    @GetMapping
    @Operation(summary = "Lister tous les clients")
    @ApiResponse(
            responseCode = "200",
            description = "Liste des clients récupérée"
    )
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un client par ID")
    @ApiResponse(
            responseCode = "200",
            description = "Client trouvé"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Client introuvable"
    )
    public Client findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un client")
    @ApiResponse(
            responseCode = "201",
            description = "Client créé"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    public Client create(@RequestBody Client client) {
        return clientService.create(client);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un client")
    @ApiResponse(
            responseCode = "200",
            description = "Client modifié"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Client introuvable"
    )
    public Client update(
            @PathVariable Long id,
            @RequestBody Client client) {

        return clientService.update(id, client);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un client")
    @ApiResponse(
            responseCode = "204",
            description = "Client supprimé"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Client introuvable"
    )
    public void deleteById(@PathVariable Long id) {
        clientService.deleteById(id);
    }
}
