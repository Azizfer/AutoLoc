
package tn.esprit.tpautoloc.web.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.service.IEmployeService;

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
    public List<Employe> findAll() {
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
    public Employe findById(@PathVariable Long id) {
        return employeService.findById(id);
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
    public Employe create(@RequestBody Employe employe) {
        return employeService.create(employe);
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
    public Employe update(
            @PathVariable Long id,
            @RequestBody Employe employe) {

        return employeService.update(id, employe);
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
