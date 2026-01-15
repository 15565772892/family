package org.family.core.service;

import org.family.core.dto.ContainerDto;
import org.family.core.dto.FamilyCoreDto;
import org.family.core.vo.ContainerBo;
import org.family.core.vo.FamliyCoreVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DockerService {

    ContainerDto getContainerList(@RequestBody ContainerBo bo);

}
