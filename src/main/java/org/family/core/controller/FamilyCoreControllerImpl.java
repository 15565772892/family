package org.family.core.controller;

import org.family.core.dto.FamilyCoreDto;
import org.family.core.service.FamilyCoreService;
import org.family.core.vo.FamliyCoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FamilyCoreControllerImpl implements FamilyCoreController {

    @Autowired
    private FamilyCoreService familyCoreService;

    @Override
    public List<FamilyCoreDto> getFamily(FamliyCoreVo vo) {

        return familyCoreService.getFamily(vo);

    }
}
