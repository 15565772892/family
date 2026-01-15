package org.family.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ContainerBo {

    @Schema(description = "是否展示所有容器", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isShowAll;

}
