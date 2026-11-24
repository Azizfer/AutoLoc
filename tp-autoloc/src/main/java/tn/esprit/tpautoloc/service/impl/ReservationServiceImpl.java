
package tn.esprit.tpautoloc.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;

import tn.esprit.tpautoloc.exception.ResourceNotFoundException;

import tn.esprit.tpautoloc.repository.IClientRepository;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;

import tn.esprit.tpautoloc.service.IReservationService;

import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.mapper.ReservationMapper;

// Adapter ces deux imports à leur package réel.
import tn.esprit.tpautoloc.observer.ReservationSubject;
import tn.esprit.tpautoloc.service.factory.TarificationStrategyFactory;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {
    private static final String RESERVATION = "Reservation";
    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;

    private final ReservationMapper reservationMapper;
    private final ReservationSubject reservationSubject;
    private final TarificationStrategyFactory tarificationStrategyFactory;

    @Override
    public ReservationResponseDTO create(
            ReservationRequestDTO dto) {

        if (dto == null
                || dto.getClientId() == null
                || dto.getVehiculeId() == null) {

            throw new IllegalArgumentException(
                    "Le client et le véhicule sont obligatoires");
        }

        verifierDates(dto.getDateDebut(), dto.getDateFin());

        Client client = clientRepository
                .findById(dto.getClientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client", dto.getClientId()));

        Vehicule vehicule = vehiculeRepository
                .findById(dto.getVehiculeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicule", dto.getVehiculeId()));

        Reservation reservation = new Reservation();

        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        Reservation saved = reservationRepository.save(reservation);

        return convertirEnDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {

        Reservation reservation = reservationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "RESERVATION", id));

        return convertirEnDTO(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {

        return reservationRepository.findAll()
                .stream()
                .map(this::convertirEnDTO)
                .toList();
    }

    @Override
    public ReservationResponseDTO update(
            Long id, ReservationRequestDTO dto) {

        Reservation existante = reservationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                RESERVATION, id));

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Les données de réservation sont obligatoires");
        }

        verifierDates(dto.getDateDebut(), dto.getDateFin());

        // Les associations sont conservées.
        // Le changement de client/véhicule
        // n'est pas réalisé par cette méthode.

        if (dto.getClientId() != null
                && !Objects.equals(
                dto.getClientId(),
                existante.getClient().getIdClient())) {
            throw new IllegalArgumentException(
                    "Le changement de client n'est pas autorisé");
        }

        if (dto.getVehiculeId() != null
                && !Objects.equals(
                dto.getVehiculeId(),
                existante.getVehicule().getIdVehicule())) {
            throw new IllegalArgumentException(
                    "Le changement de véhicule n'est pas autorisé");
        }

        existante.setDateDebut(dto.getDateDebut());
        existante.setDateFin(dto.getDateFin());

        Reservation saved = reservationRepository.save(existante);

        return convertirEnDTO(saved);
    }

    @Override
    public void deleteById(Long id) {

        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    RESERVATION, id);
        }

        reservationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return reservationRepository.count();
    }

    // Annulation REST par identifiant

    @Override
    public ReservationResponseDTO annulerReservation(Long id) {

        Reservation reservation = reservationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                RESERVATION, id));

        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            throw new IllegalArgumentException(
                    "La réservation est déjà annulée");
        }

        annulerReservation(reservation);

        Reservation saved = reservationRepository.save(reservation);

        return convertirEnDTO(saved);
    }

    // Fonctionnalité Observer de l'Atelier 6


    @Override
    public Reservation creerReservation(Client client) {

        if (client == null) {
            throw new IllegalArgumentException(
                    "Le client est obligatoire");
        }

        Reservation reservation = new Reservation();

        reservation.setClient(client);
        reservation.setDateDebut(LocalDate.now(ZoneId.of("Africa/Tunis")));
        reservation.setStatut(StatutReservation.CONFIRMEE);

        reservationSubject.notifierConfirmation(reservation);

        return reservation;
    }


    @Override
    public Reservation annulerReservation(Reservation reservation) {

        if (reservation == null) {
            throw new IllegalArgumentException(
                    "La réservation est obligatoire");
        }

        reservation.setStatut(StatutReservation.ANNULEE);
        reservationSubject.notifierAnnulation(reservation);

        return reservation;
    }

    // Conversion enrichie avec le tarif Strategy

    private ReservationResponseDTO convertirEnDTO(
            Reservation reservation) {

        ReservationResponseDTO dto =
                reservationMapper.toDto(reservation);

        if (reservation.getVehicule() != null
                && reservation.getDateDebut() != null
                && reservation.getDateFin() != null) {

            var strategie =
                    tarificationStrategyFactory.resoudre(reservation);
            dto.setTarifEstime(
                    strategie.calculerTarif(
                            reservation.getVehicule(),
                            reservation));
        }

        return dto;
    }

    private void verifierDates(
            LocalDate dateDebut,
            LocalDate dateFin) {

        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException(
                    "Les dates de réservation sont obligatoires");
        }

        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException(
                    "La date de fin ne peut pas précéder la date de début");
        }
    }
}
