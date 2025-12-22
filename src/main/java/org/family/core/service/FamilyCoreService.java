package org.family.core.service;

import org.family.core.dto.FamilyCoreDto;
import org.family.core.vo.FamliyCoreVo;

import java.util.List;

public interface FamilyCoreService {

    public List<FamilyCoreDto> getFamily(FamliyCoreVo bo);

}
