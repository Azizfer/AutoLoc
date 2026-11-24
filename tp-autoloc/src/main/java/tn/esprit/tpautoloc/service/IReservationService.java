
package tn.esprit.tpautoloc.service;

import java.util.List;

import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;

public interface IReservationService {

    ReservationResponseDTO create(ReservationRequestDTO dto);

    ReservationResponseDTO findById(Long id);

    List<ReservationResponseDTO> findAll();

    ReservationResponseDTO update(
            Long id, ReservationRequestDTO dto);

    void deleteById(Long id);

    long count();

    ReservationResponseDTO annulerReservation(Long id);

    // Méthodes de l'Atelier 6 conservées
    Reservation creerReservation(Client client);

    Reservation annulerReservation(Reservation reservation);
}
