package org.family.core.controller;

import org.family.core.dto.FamilyCoreDto;
import org.family.core.service.FamilyCoreService;
import org.family.core.util.DateUtils;
import org.family.core.vo.FamliyCoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FamilyCoreControllerImpl implements FamilyCoreController {

    @Autowired
    private FamilyCoreService familyCoreService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<FamilyCoreDto> getFamily(FamliyCoreVo vo) {

        List<FamilyCoreDto> list = familyCoreService.getFamily(vo);
        redisTemplate.opsForValue().set("family"+ DateUtils.getDateTime(), list.toString());
        return familyCoreService.getFamily(vo);

    }
}
