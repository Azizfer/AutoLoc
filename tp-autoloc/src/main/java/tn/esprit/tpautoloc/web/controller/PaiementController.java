
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.service.IPaiementService;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(
        name = "Paiements",
        description = "Consultation des paiements"
)
public class PaiementController {

    private final IPaiementService paiementService;

    @GetMapping
    @Operation(summary = "Lister tous les paiements")
    public ResponseEntity<List<PaiementDTO>> findAll() {

        return ResponseEntity.ok(
                paiementService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un paiement par ID")
    public ResponseEntity<PaiementDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                paiementService.findById(id));
    }
}
