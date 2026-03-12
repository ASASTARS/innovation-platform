package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.PersonLibrary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonLibraryMapper {
    List<PersonLibrary> selectPage(@Param("offset") int offset, @Param("limit") int limit,
                                   @Param("personTypeId") Long personTypeId,
                                   @Param("keyword") String keyword);
    int count(@Param("personTypeId") Long personTypeId, @Param("keyword") String keyword);
    PersonLibrary selectById(@Param("id") Long id);
    int insert(PersonLibrary person);
    int update(PersonLibrary person);
    int deleteById(@Param("id") Long id);
}
