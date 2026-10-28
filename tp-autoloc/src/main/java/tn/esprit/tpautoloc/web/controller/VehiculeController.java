
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.service.IVehiculeService;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(
        name = "Vehicules",
        description = "Gestion des véhicules de location"
)
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @GetMapping
    @Operation(summary = "Lister tous les véhicules")
    @ApiResponse(
            responseCode = "200",
            description = "Liste des véhicules récupérée"
    )
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un véhicule par ID")
    @ApiResponse(
            responseCode = "200",
            description = "Véhicule trouvé"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Véhicule introuvable"
    )
    public Vehicule findById(@PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter un véhicule")
    @ApiResponse(
            responseCode = "201",
            description = "Véhicule créé"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.create(vehicule);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un véhicule")
    @ApiResponse(
            responseCode = "200",
            description = "Véhicule modifié"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Véhicule introuvable"
    )
    public Vehicule update(
            @PathVariable Long id,
            @RequestBody Vehicule vehicule) {

        return vehiculeService.update(id, vehicule);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un véhicule")
    @ApiResponse(
            responseCode = "204",
            description = "Véhicule supprimé"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Véhicule introuvable"
    )
    public void deleteById(@PathVariable Long id) {
        vehiculeService.deleteById(id);
    }
}
