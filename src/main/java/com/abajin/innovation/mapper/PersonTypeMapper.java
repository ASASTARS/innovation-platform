package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.PersonType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonTypeMapper {
    List<PersonType> selectAll();
    PersonType selectById(Long id);
}
