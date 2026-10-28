
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.service.IPaiementService;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(
        name = "Paiements",
        description = "Consultation des paiements des contrats"
)
public class PaiementController {

    private final IPaiementService paiementService;

    @GetMapping
    @Operation(summary = "Lister tous les paiements")
    @ApiResponse(
            responseCode = "200",
            description = "Liste des paiements récupérée"
    )
    public List<Paiement> findAll() {
        return paiementService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un paiement par ID")
    @ApiResponse(
            responseCode = "200",
            description = "Paiement trouvé"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Paiement introuvable"
    )
    public Paiement findById(
            @Parameter(description = "Identifiant du paiement")
            @PathVariable Long id) {

        return paiementService.findById(id);
    }
}
