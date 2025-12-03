package com.jiang.singlelearningdemo.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.util.Map;

@Data
@TableName(value = "part")
public class Part {
    private String id;

    @TableField(typeHandler =  JacksonTypeHandler.class)
    private Map<String ,Object> param;
}
