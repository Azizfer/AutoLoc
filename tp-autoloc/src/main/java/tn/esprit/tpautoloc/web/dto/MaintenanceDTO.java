
package tn.esprit.tpautoloc.web.dto;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceDTO {

    private Long id;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long vehiculeId;
}
