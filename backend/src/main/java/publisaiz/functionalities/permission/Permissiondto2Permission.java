package publisaiz.functionalities.permission;

import publisaiz.api.dto.PermissionDTO;
import publisaiz.entities.Permission;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

class Permissiondto2Permission {

    static public Permission permissionDTOtoEntity(PermissionDTO permissionDTO, PermissionService permissionService) {
        Optional<Permission> permission = Optional.empty();
        Permission p;
        if (permissionDTO.getId() != null)
            permission = permissionService.findById(permissionDTO.getId());
        if (permission.isPresent()) {
            p = permission.get();
            bindPermission(permissionDTO, permissionService, p);
        } else {
            p = new Permission();
            bindPermission(permissionDTO, permissionService, p);
        }
        return p;
    }

    private static void bindPermission(PermissionDTO permissionDTO, PermissionService permissionService, Permission p) {
        p.setActive(true);
        if (permissionDTO.getValidFromDate() != null)
            p.setFromDate(parseZoneDateTime(permissionDTO.getValidFromDate()));
        if ((permissionDTO.getValidUpToDate() != null))
            p.setToDate(parseZoneDateTime(permissionDTO.getValidUpToDate()));
        p.setPermissionFor(permissionService.findUser(permissionDTO.getPermissionFor()));
        p.setCreatedBy(permissionService.getLoggedUser());
        p.setActive((permissionDTO.getActive()));
    }

    static private ZonedDateTime parseZoneDateTime(String datetime) {
        return LocalDateTime.parse(datetime, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault());
    }
}