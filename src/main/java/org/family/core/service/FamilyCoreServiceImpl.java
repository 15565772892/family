package org.family.core.service;

import org.family.core.dao.FamilyCoreDao;
import org.family.core.dto.FamilyCoreDto;
import org.family.core.vo.FamliyCoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyCoreServiceImpl implements FamilyCoreService {

    @Autowired
    private FamilyCoreDao familyCoreDao;

    @Override
    public List<FamilyCoreDto> getFamily(FamliyCoreVo vo) {

        List<FamilyCoreDto> famliyCoreList = familyCoreDao.getFamliyCoreList(vo);

        return famliyCoreList;

    }
}
