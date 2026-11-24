
package tn.esprit.tpautoloc.service.strategy;

import java.math.BigDecimal;

import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.Reservation;

public interface ITarificationStrategy {

    BigDecimal calculerTarif(
            Vehicule vehicule,
            Reservation reservation
    );

    String getCode();
}
