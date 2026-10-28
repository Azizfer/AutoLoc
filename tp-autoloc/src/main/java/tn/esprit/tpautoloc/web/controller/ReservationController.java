
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.IReservationService;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(
        name = "Reservations",
        description = "Gestion des réservations de véhicules"
)
public class ReservationController {

    private final IReservationService reservationService;

    @GetMapping
    @Operation(summary = "Lister toutes les réservations")
    @ApiResponse(
            responseCode = "200",
            description = "Liste des réservations récupérée"
    )
    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une réservation par ID")
    @ApiResponse(
            responseCode = "200",
            description = "Réservation trouvée"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Réservation introuvable"
    )
    public Reservation findById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une réservation")
    @ApiResponse(
            responseCode = "201",
            description = "Réservation créée"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    public Reservation create(
            @RequestBody Reservation reservation) {

        return reservationService.create(reservation);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une réservation")
    @ApiResponse(
            responseCode = "200",
            description = "Réservation modifiée"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Réservation introuvable"
    )
    public Reservation update(
            @PathVariable Long id,
            @RequestBody Reservation reservation) {

        return reservationService.update(id, reservation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une réservation")
    @ApiResponse(
            responseCode = "204",
            description = "Réservation supprimée"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Réservation introuvable"
    )
    public void deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}
