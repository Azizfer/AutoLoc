
package tn.esprit.tpautoloc.service.factory;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;

@Component
public class TarificationStrategyFactory {

    private final Map<String, ITarificationStrategy> strategies;

    public TarificationStrategyFactory(
            List<ITarificationStrategy> strategyList) {

        strategies = new HashMap<>();

        for (ITarificationStrategy strategy : strategyList) {
            strategies.put(strategy.getCode(), strategy);
        }
    }

    public ITarificationStrategy resoudre(
            Reservation reservation) {

        if (reservation == null
                || reservation.getDateDebut() == null
                || reservation.getDateFin() == null) {
            throw new IllegalArgumentException(
                    "Les dates de réservation sont obligatoires");
        }

        long jours = ChronoUnit.DAYS.between(
                reservation.getDateDebut(),
                reservation.getDateFin());

        if (jours <= 0) {
            throw new IllegalArgumentException(
                    "La durée de réservation doit être positive");
        }

        String code;

        if (jours >= 7) {
            code = "LONGUE_DUREE";
        } else if (jours <= 3 && estWeekend(reservation)) {
            code = "WEEKEND";
        } else {
            code = "STANDARD";
        }

        ITarificationStrategy strategie = strategies.get(code);

        if (strategie == null) {
            throw new IllegalStateException(
                    "Stratégie de tarification introuvable : " + code);
        }

        return strategie;
    }

    private boolean estWeekend(Reservation reservation) {

        DayOfWeek jour =
                reservation.getDateDebut().getDayOfWeek();

        return DayOfWeek.SATURDAY.equals(jour)
                || DayOfWeek.SUNDAY.equals(jour);
    }
}
