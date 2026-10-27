package tn.esprit.tpautoloc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contrat")
@Getter
@Setter
@NoArgsConstructor
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;

    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private Boolean valide;

    // 5.1 — Contrat → Reservation (OneToOne propriétaire, porte la FK)
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    // Contrat -> Paiement
    @OneToMany(
            mappedBy = "contrat",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Paiement> paiements = new ArrayList<>();
}