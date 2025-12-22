package org.family.core.model;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data // 指定表名
public class FamilyMember {

    private Long id;

    private String chineseName;
    
    private Integer generationNumber;
    
    private Integer sex;  // 1-男, 2-女

    private LocalDate birthDate;

    private LocalDate deathDate;
    
    private String biography;
    
    // 父亲ID（外键）
    private Long fatherId;
    
    // 逻辑删除（0-未删除，1-已删除）
    private Integer isDel;
    
    // 自动填充
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}