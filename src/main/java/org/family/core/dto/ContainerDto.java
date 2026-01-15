package org.family.core.dto;

import com.github.dockerjava.api.model.Container;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContainerDto {

    @Schema(description = "容器集合")
    List<Container> list;


}
