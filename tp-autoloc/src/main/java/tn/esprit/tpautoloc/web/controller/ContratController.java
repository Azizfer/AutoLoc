
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.service.IContratService;
import tn.esprit.tpautoloc.web.dto.ContratDTO;
import tn.esprit.tpautoloc.web.dto.ContratCreationDTO;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats")
public class ContratController {

    private final IContratService contratService;

    @PostMapping
    @Operation(summary = "Créer un contrat")
    public ResponseEntity<ContratDTO> create(
            @Valid @RequestBody ContratCreationDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Lister tous les contrats")
    public ResponseEntity<List<ContratDTO>> findAll() {

        return ResponseEntity.ok(contratService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un contrat par ID")
    public ResponseEntity<ContratDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(contratService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un contrat")
    public ResponseEntity<ContratDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ContratDTO dto) {

        return ResponseEntity.ok(contratService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un contrat")
    public void deleteById(@PathVariable Long id) {

        contratService.deleteById(id);
    }

    @GetMapping("/count")
    @Operation(summary = "Compter les contrats")
    public long count() {

        return contratService.count();
    }
}
