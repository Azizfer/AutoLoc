
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(
        name = "Véhicules",
        description = "Gestion des véhicules et des affectations"
)
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @PostMapping
    @Operation(summary = "Créer un véhicule")
    public ResponseEntity<VehiculeDTO> create(
            @Valid @RequestBody VehiculeDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehiculeService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Lister tous les véhicules")
    public ResponseEntity<List<VehiculeDTO>> findAll() {

        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @GetMapping("/count")
    @Operation(summary = "Compter les véhicules")
    public ResponseEntity<Long> count() {

        return ResponseEntity.ok(vehiculeService.count());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un véhicule par ID")
    public ResponseEntity<VehiculeDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un véhicule")
    public ResponseEntity<VehiculeDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody VehiculeDTO dto) {

        return ResponseEntity.ok(vehiculeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un véhicule")
    public void deleteById(@PathVariable Long id) {

        vehiculeService.deleteById(id);
    }

    @PutMapping("/{vehiculeId}/agence/{agenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Affecter ou transférer un véhicule")
    @ApiResponse(responseCode = "204",
            description = "Affectation effectuée")
    @ApiResponse(responseCode = "404",
            description = "Véhicule ou agence introuvable")
    public void affecterVehiculeAAgence(
            @PathVariable Long vehiculeId,
            @PathVariable Long agenceId) {

        vehiculeService.affecterVehiculeAAgence(
                vehiculeId, agenceId);
    }

    @PutMapping("/{vehiculeId}/desaffecter")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Désaffecter un véhicule de son agence")
    @ApiResponse(responseCode = "204",
            description = "Désaffectation effectuée")
    @ApiResponse(responseCode = "400",
            description = "Véhicule déjà sans agence")
    @ApiResponse(responseCode = "404",
            description = "Véhicule introuvable")
    public void desaffecterVehiculeDeAgence(
            @PathVariable Long vehiculeId) {

        vehiculeService.desaffecterVehiculeDeAgence(vehiculeId);
    }
}
