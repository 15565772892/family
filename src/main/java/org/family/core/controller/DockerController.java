package org.family.core.controller;

import org.family.core.dto.ContainerDto;
import org.family.core.dto.FamilyCoreDto;
import org.family.core.vo.ContainerBo;
import org.family.core.vo.FamliyCoreVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
interface DockerController {

    @PostMapping(path = "docker/getContainerList",name = "获取容器列表" ,produces = "application/json")
    @ResponseBody
    public ContainerDto getContainerList(@RequestBody ContainerBo bo);

}
