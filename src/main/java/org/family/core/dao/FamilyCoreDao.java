// 3. 最简单的DAO接口
package org.family.core.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.family.core.dto.FamilyCoreDto;
import org.family.core.vo.FamliyCoreVo;

import java.util.List;

@Mapper  // ⭐ 只有这个注解，没有其他
public interface FamilyCoreDao {

    List<FamilyCoreDto> getFamliyCoreList(FamliyCoreVo vo);

}