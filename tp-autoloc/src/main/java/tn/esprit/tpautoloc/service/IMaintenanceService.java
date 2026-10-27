package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Maintenance;

import java.util.List;

public interface IMaintenanceService {

    Maintenance create(Maintenance maintenance);

    Maintenance update(Long id, Maintenance maintenance);

    Maintenance findById(Long id);

    List<Maintenance> findAll();

    void deleteById(Long id);
}