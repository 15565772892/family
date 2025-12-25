package org.family.core.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(FamilyCoreServiceImpl.class);

    @Autowired
    private FamilyCoreDao familyCoreDao;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<FamilyCoreDto> getFamily(FamliyCoreVo vo) {

        //db
        List<FamilyCoreDto> famliyCoreList = familyCoreDao.getFamliyCoreList(vo);
        logger.info("▀▀▀▀▀▀▀▀▀MYSQL查询结果：{}" , famliyCoreList.size());

        //redis
        redisTemplate.opsForValue().set("family"+ DateUtils.getDateTime(), famliyCoreList.toString());
        logger.info("▀▀▀▀▀▀▀▀▀REDIS执行成功");


        // 不同级别的日志
        logger.info("▀▀▀▀▀▀▀▀日志程序执行开始");
        logger.trace("This is trace log");
        logger.debug("This is debug log");
        logger.info("This is info log");
        logger.warn("This is warn log");
        logger.error("This is error log");
        logger.fatal("This is fatal log");

        // 异常日志
        try {
            // some code
            int a = 1 / 0;
        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
        logger.info("▀▀▀▀▀▀▀▀日志程序执行完毕");

        return famliyCoreList;

    }
}
