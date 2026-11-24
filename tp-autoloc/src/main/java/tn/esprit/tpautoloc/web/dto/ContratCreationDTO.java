
package tn.esprit.tpautoloc.web.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ContratCreationDTO extends ContratDTO {

    private List<PaiementDTO> paiements;
}
