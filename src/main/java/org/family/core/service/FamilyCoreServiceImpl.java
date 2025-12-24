package org.family.core.service;

import org.family.core.dao.FamilyCoreDao;
import org.family.core.dto.FamilyCoreDto;
import org.family.core.util.DateUtils;
import org.family.core.vo.FamliyCoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyCoreServiceImpl implements FamilyCoreService {

    @Autowired
    private FamilyCoreDao familyCoreDao;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<FamilyCoreDto> getFamily(FamliyCoreVo vo) {

        List<FamilyCoreDto> famliyCoreList = familyCoreDao.getFamliyCoreList(vo);
        System.out.println("▀▀▀▀▀▀▀▀▀MYSQL查询结果：" + famliyCoreList.size());
        redisTemplate.opsForValue().set("family"+ DateUtils.getDateTime(), famliyCoreList.toString());
        System.out.println("▀▀▀▀▀▀▀▀▀REDIS执行成功");
        return famliyCoreList;

    }
}
