package org.family.core.controller;

import org.family.core.dto.FamilyCoreDto;
import org.family.core.vo.FamliyCoreVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
interface FamilyCoreController {

    @PostMapping(path = "core/getFamily",name = "获取详情" ,produces = "application/json")
    @ResponseBody
    public List<FamilyCoreDto> getFamily(@RequestBody FamliyCoreVo bo);

}
