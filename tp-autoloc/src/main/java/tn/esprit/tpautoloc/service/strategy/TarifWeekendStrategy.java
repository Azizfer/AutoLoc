
package tn.esprit.tpautoloc.service.strategy;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.Reservation;

@Component
public class TarifWeekendStrategy
        implements ITarificationStrategy {

    private static final BigDecimal REMISE_WEEKEND =
            new BigDecimal("0.90");

    @Override
    public BigDecimal calculerTarif(
            Vehicule vehicule,
            Reservation reservation) {

        if (vehicule == null || reservation == null
                || vehicule.getTarifJournalier() == null
                || reservation.getDateDebut() == null
                || reservation.getDateFin() == null) {

            throw new IllegalArgumentException(
                    "Données de tarification incomplètes");
        }

        long nombreJours = ChronoUnit.DAYS.between(
                reservation.getDateDebut(),
                reservation.getDateFin()
        );

        if (nombreJours <= 0) {
            throw new IllegalArgumentException(
                    "La date de fin doit être après la date de début");
        }

        BigDecimal tarifJournalier =
                new BigDecimal(
                        String.valueOf(vehicule.getTarifJournalier())
                );

        return tarifJournalier
                .multiply(BigDecimal.valueOf(nombreJours))
                .multiply(REMISE_WEEKEND);
    }

    @Override
    public String getCode() {
        return "WEEKEND";
    }
}
