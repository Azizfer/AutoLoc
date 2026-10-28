
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.domain.Maintenance;
import tn.esprit.tpautoloc.service.IMaintenanceService;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
@Tag(
        name = "Maintenances",
        description = "Gestion des maintenances des véhicules"
)
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @GetMapping
    @Operation(summary = "Lister toutes les maintenances")
    @ApiResponse(
            responseCode = "200",
            description = "Liste des maintenances récupérée"
    )
    public List<Maintenance> findAll() {
        return maintenanceService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une maintenance par ID")
    @ApiResponse(
            responseCode = "200",
            description = "Maintenance trouvée"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Maintenance introuvable"
    )
    public Maintenance findById(@PathVariable Long id) {
        return maintenanceService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une maintenance")
    @ApiResponse(
            responseCode = "201",
            description = "Maintenance créée"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    public Maintenance create(
            @RequestBody Maintenance maintenance) {

        return maintenanceService.create(maintenance);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une maintenance")
    @ApiResponse(
            responseCode = "200",
            description = "Maintenance modifiée"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Maintenance introuvable"
    )
    public Maintenance update(
            @PathVariable Long id,
            @RequestBody Maintenance maintenance) {

        return maintenanceService.update(id, maintenance);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une maintenance")
    @ApiResponse(
            responseCode = "204",
            description = "Maintenance supprimée"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Maintenance introuvable"
    )
    public void deleteById(@PathVariable Long id) {
        maintenanceService.deleteById(id);
    }
}
