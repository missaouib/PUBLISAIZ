package publisaiz.controller.api.dto;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import publisaiz.datasources.database.entities.Controller;
import publisaiz.datasources.database.entities.Permission;
import publisaiz.services.PermissionService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionDTO {
    private Long id;
    private String createdBy;
    private String validFromDate;
    private String validUpToDate;
    private String permissionFor;
    private List<ControllerDTO> controllers;
    private Boolean active;

    public PermissionDTO() {
    }

    public PermissionDTO(@NotNull Permission permission) {
        id = permission.getId();
        if (permission.getPermissionFor() != null)
            permissionFor = permission.getPermissionFor().getLogin();
        createdBy = permission.getCreatedBy().getLogin();
        if (permission.getToDate() != null)
            validUpToDate = permission.getToDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (permission.getFromDate() != null)
            validFromDate = permission.getFromDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Set<Controller> controllersDB = permission.getControllers();
        if (controllersDB != null) {
            controllers = controllersDB.stream().map(ControllerDTO::new).collect(Collectors.toList());
        }
        active = permission.isActive();
    }

    public static Page<PermissionDTO> convert(Page<Permission> all) {
        return all.map(PermissionDTO::new);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(String validFromDate) {
        this.validFromDate = validFromDate;
    }

    public String getValidUpToDate() {
        return validUpToDate;
    }

    public void setValidUpToDate(String validUpToDate) {
        this.validUpToDate = validUpToDate;
    }

    public String getPermissionFor() {
        return permissionFor;
    }

    public void setPermissionFor(String permissionFor) {
        this.permissionFor = permissionFor;
    }

    public List<ControllerDTO> getControllers() {
        return controllers;
    }

    public void setControllers(List<ControllerDTO> controllers) {
        this.controllers = controllers;
    }

    @Transactional
    public Permission toEntity(PermissionService permissionService) {
        Optional<Permission> permission = Optional.empty();
        Permission p = null;
        if (id != null)
            permission = permissionService.findById(id);
        if (permission.isPresent()) {
            p = permission.get();
            bind(permissionService, p);
        } else {
            p = new Permission();
            bind(permissionService, p);
        }
        return p;
    }

    private void bind(@NotNull PermissionService permissionService, @NotNull Permission p) {
        p.setActive(true);
        if (validFromDate != null)
            p.setFromDate(parseZoneDateTime(validFromDate));
        if (validUpToDate != null)
            p.setToDate(parseZoneDateTime(validUpToDate));
        p.setPermissionFor(permissionService.findUser(permissionFor));
        p.setCreatedBy(permissionService.getLoggedUser());
        p.setActive(active);
    }

    @NotNull
    private ZonedDateTime parseZoneDateTime(String datetime) {
        return LocalDateTime.parse(datetime, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault());
    }
}
