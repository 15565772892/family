package org.family.core.controller;

import org.family.core.dto.ContainerDto;
import org.family.core.dto.FamilyCoreDto;
import org.family.core.service.DockerService;
import org.family.core.service.FamilyCoreService;
import org.family.core.vo.ContainerBo;
import org.family.core.vo.FamliyCoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DockerControllerImpl implements DockerController {

    @Autowired
    private DockerService dockerService;


    @Override
    public ContainerDto getContainerList(ContainerBo bo) {

        return dockerService.getContainerList(bo);

    }

}
