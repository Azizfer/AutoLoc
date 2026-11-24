
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion des agences")
public class AgenceController {

    private final IAgenceService agenceService;

    @PostMapping
    @Operation(summary = "Créer une agence")
    public ResponseEntity<AgenceDTO> create(
            @Valid @RequestBody AgenceDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(agenceService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Lister toutes les agences")
    public ResponseEntity<List<AgenceDTO>> findAll() {

        return ResponseEntity.ok(agenceService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une agence par ID")
    public ResponseEntity<AgenceDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(agenceService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une agence")
    public ResponseEntity<AgenceDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AgenceDTO dto) {

        return ResponseEntity.ok(agenceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une agence")
    public void deleteById(@PathVariable Long id) {

        agenceService.deleteById(id);
    }

    @GetMapping("/count")
    @Operation(summary = "Compter les agences")
    public long count() {

        return agenceService.count();
    }
}
