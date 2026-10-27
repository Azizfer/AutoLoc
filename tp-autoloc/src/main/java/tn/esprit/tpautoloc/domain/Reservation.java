package tn.esprit.tpautoloc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    // 4.1 — Reservation → Client (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // 4.2 — Reservation → Vehicule (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    // 4.3 — Reservation ↔ Contrat (OneToOne — côté inverse)
    @OneToOne(mappedBy = "reservation")
    private Contrat contrat;
}