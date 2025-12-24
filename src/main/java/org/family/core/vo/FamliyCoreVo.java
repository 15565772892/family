package org.family.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FamliyCoreVo {

    @Schema(description = "成员ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 50, message = "姓名长度1-50")
    @Schema(
            description = "中文姓名",
            example = "李四",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    private String chineseName;

    @Min(value = 1, message = "世代必须大于0")
    @Max(value = 100, message = "世代不能超过100")
    @Schema(description = "世代", example = "5", minimum = "1", maximum = "100")
    private Integer generation;

    @NotNull(message = "性别不能为空")
    @Schema(
            description = "性别",
            example = "1",
            allowableValues = {"1", "2"},
            defaultValue = "1"
    )
    private Integer gender; // 1-男, 2-女

    @PastOrPresent(message = "出生日期不能是未来")
    @Schema(description = "出生日期", example = "1990-01-15")
    private LocalDate birthDate;

    @PastOrPresent(message = "逝世日期不能是未来")
    @Schema(description = "逝世日期", example = "2020-05-20")
    private LocalDate deathDate;

    @Size(max = 2000, message = "简介不能超过2000字")
    @Schema(description = "生平简介", example = "著名家族成员，对家族有重大贡献")
    private String biography;


}
