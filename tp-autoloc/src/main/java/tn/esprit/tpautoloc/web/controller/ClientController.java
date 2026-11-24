
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.service.IClientService;
import tn.esprit.tpautoloc.web.dto.ClientDTO;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion des clients")
public class ClientController {

    private final IClientService clientService;

    @PostMapping
    @Operation(summary = "Créer un client")
    public ResponseEntity<ClientDTO> create(
            @Valid @RequestBody ClientDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un client par ID")
    public ResponseEntity<ClientDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lister tous les clients")
    public ResponseEntity<List<ClientDTO>> findAll() {

        return ResponseEntity.ok(clientService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un client")
    public ResponseEntity<ClientDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClientDTO dto) {

        return ResponseEntity.ok(clientService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un client")
    public void deleteById(@PathVariable Long id) {

        clientService.deleteById(id);
    }

    @GetMapping("/count")
    @Operation(summary = "Compter les clients")
    public long count() {

        return clientService.count();
    }
}
