package publisaiz.functionalities.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import publisaiz.api.dto.ControllerDTO;
import publisaiz.entities.Controller;

class ControllerDto2Controller {
    private static final Logger logger = LoggerFactory.getLogger(ControllerDto2Controller.class);

    private ControllerDto2Controller() {
    }

    static Controller controllerDTOtoEntity(ControllerDTO dto, ControllerService controllerService) {
        final Controller controller = controllerService.findById(dto.getId());
        if (controller == null)
            throw new IllegalArgumentException("no such controller");
        logger.debug("addPermission");
        controllerService.addPermission(dto, controller);
        logger.debug("delPermission");
        controllerService.delPermission(dto, controller);
        logger.debug("addRole");
        controllerService.addRole(dto, controller);
        logger.debug("deleteRole");
        controllerService.deleteRole(dto, controller);
        return controller;
    }
}