package com.jiang.singlelearningdemo.common.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiang.singlelearningdemo.common.contant.DataSourceConstant;
import com.jiang.singlelearningdemo.common.pojo.Part;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@DS(DataSourceConstant.POSTGRESQL)
public interface PartMapper extends BaseMapper<Part> {
    @Select("SELECT * FROM part")
    List<Part> getAll();

}
