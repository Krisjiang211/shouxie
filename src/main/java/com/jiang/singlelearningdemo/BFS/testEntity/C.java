package com.jiang.singlelearningdemo.BFS.testEntity;

import com.jiang.singlelearningdemo.BFS.Dict;
import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class C {
    private String c1;
    @Dict(dicCode = "sys_dict")
    private String c2;
}
