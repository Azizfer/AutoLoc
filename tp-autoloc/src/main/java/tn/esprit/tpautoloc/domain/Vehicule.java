package tn.esprit.tpautoloc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    private String immatriculation;
    private String marque;
    private String modele;
    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    private CategorieVehicule categorie;

    @Enumerated(EnumType.STRING)
    private StatutVehicule statut;

    // 2.1 — déjà fait
    @ManyToOne
    @JoinColumn(name = "agence_id")
    private Agence agence;

    // 2.2 — Vehicule → Reservation (OneToMany)
    @OneToMany(mappedBy = "vehicule")
    private List<Reservation> reservations;

    // 2.3 — Vehicule → Maintenance (OneToMany)
    @OneToMany(mappedBy = "vehicule")
    private List<Maintenance> maintenances;

    // 2.4 — Vehicule ↔ Equipement (ManyToMany)
    @ManyToMany
    @JoinTable(
            name = "vehicule_equipements",
            joinColumns = @JoinColumn(name = "vehicule_id"),
            inverseJoinColumns = @JoinColumn(name = "equipement_id")
    )
    private Set<Equipement> equipements = new HashSet<>();
}