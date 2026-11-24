
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.validator.ReservationRequestValidator;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(
        name = "Réservations",
        description = "Gestion des réservations"
)
public class ReservationController {

    private final IReservationService reservationService;

    private final ReservationRequestValidator reservationRequestValidator;

    @PostMapping
    @Operation(summary = "Créer une réservation")
    public ResponseEntity<?> create(
            @Valid @RequestBody ReservationRequestDTO dto,
            BindingResult result) {

        reservationRequestValidator.validate(dto, result);

        if (result.hasErrors()) {

            return ResponseEntity.badRequest().body(
                    result.getFieldErrors().stream()
                            .map(error -> error.getField()
                                    + " : "
                                    + error.getDefaultMessage())
                            .toList()
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Lister toutes les réservations")
    public ResponseEntity<List<ReservationResponseDTO>> findAll() {

        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une réservation")
    public ResponseEntity<ReservationResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(reservationService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une réservation")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequestDTO dto,
            BindingResult result) {

        reservationRequestValidator.validate(dto, result);

        if (result.hasErrors()) {

            return ResponseEntity.badRequest().body(
                    result.getFieldErrors().stream()
                            .map(error -> error.getField()
                                    + " : "
                                    + error.getDefaultMessage())
                            .toList()
            );
        }

        return ResponseEntity.ok(
                reservationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une réservation")
    public void deleteById(@PathVariable Long id) {

        reservationService.deleteById(id);
    }

    @GetMapping("/count")
    @Operation(summary = "Compter les réservations")
    public ResponseEntity<Long> count() {

        return ResponseEntity.ok(reservationService.count());
    }

    @PutMapping("/{id}/annuler")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Annuler une réservation")
    public void annulerReservation(@PathVariable Long id) {

        reservationService.annulerReservation(id);
    }
}
