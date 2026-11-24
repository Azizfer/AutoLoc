
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.service.IEmployeService;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(
        name = "Employes",
        description = "Gestion des employés des agences"
)
public class EmployeController {

    private final IEmployeService employeService;

    @GetMapping
    @Operation(summary = "Lister tous les employés")
    @ApiResponse(
            responseCode = "200",
            description = "Liste des employés récupérée"
    )
    public List<EmployeDTO> findAll() {
        return employeService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un employé par ID")
    @ApiResponse(
            responseCode = "200",
            description = "Employé trouvé"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Employé introuvable"
    )
    public EmployeDTO findById(@PathVariable Long id) {
        return employeService.findById(id);
    }

    @PutMapping("/{employeId}/agence/{agenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Affecter un employé à une agence")
    @ApiResponse(
            responseCode = "204",
            description = "Employé affecté avec succès"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Employé ou agence introuvable"
    )
    public void affecterEmployeAAgence(
            @PathVariable Long employeId,
            @PathVariable Long agenceId) {

        employeService.affecterEmployeAAgence(
                employeId, agenceId);
    }

    @PutMapping("/{employeId}/desaffecter")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Désaffecter un employé de son agence")
    @ApiResponse(
            responseCode = "204",
            description = "Employé désaffecté avec succès"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Employé déjà sans agence"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Employé introuvable"
    )
    public void desaffecterEmployeDeAgence(
            @PathVariable Long employeId) {

        employeService.desaffecterEmployeDeAgence(employeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un employé")
    @ApiResponse(
            responseCode = "201",
            description = "Employé créé"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    public EmployeDTO create(
            @RequestBody EmployeDTO dto) {

        return employeService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un employé")
    @ApiResponse(
            responseCode = "200",
            description = "Employé modifié"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Données invalides"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Employé introuvable"
    )
    public EmployeDTO update(
            @PathVariable Long id,
            @RequestBody EmployeDTO dto) {

        return employeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un employé")
    @ApiResponse(
            responseCode = "204",
            description = "Employé supprimé"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Employé introuvable"
    )
    public void deleteById(@PathVariable Long id) {
        employeService.deleteById(id);
    }
}
