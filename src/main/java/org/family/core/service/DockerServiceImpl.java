package org.family.core.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.family.core.config.DockerApiConfig;
import org.family.core.dao.FamilyCoreDao;
import org.family.core.dto.ContainerDto;
import org.family.core.dto.FamilyCoreDto;
import org.family.core.util.DateUtils;
import org.family.core.vo.ContainerBo;
import org.family.core.vo.FamliyCoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DockerServiceImpl implements DockerService {

    private static final Logger logger = LogManager.getLogger(DockerServiceImpl.class);

    private DockerClient dockerClient = DockerApiConfig.createDockerClientWithTLS();;

    @Override
    public ContainerDto getContainerList(ContainerBo bo) {

        ContainerDto containerDto = new ContainerDto();

        List<Container> list = dockerClient.listContainersCmd()
                .withShowAll(bo.getIsShowAll())  // 显示所有容器（包括停止的）
                .withShowSize(true) // 显示容器大小
                .exec();

        for (Container container : list) {
            logger.info("ID: " + container.getId());
            logger.info("名称: " + Arrays.toString(container.getNames()));
            logger.info("镜像: " + container.getImage());
            logger.info("状态: " + container.getStatus());
            logger.info("创建时间: " + container.getCreated());
            logger.info("端口: " + container.getPorts());
            logger.info("----------------------");
        }
        containerDto.setList(list);
        return containerDto;
    }

}
