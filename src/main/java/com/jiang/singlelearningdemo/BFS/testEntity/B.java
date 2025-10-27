package com.jiang.singlelearningdemo.BFS.testEntity;

import com.jiang.singlelearningdemo.BFS.Dict;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class B {
    private String b1;
    @Dict(dicCode = "sys_dict")
    private String b2;
    private C c;
    private List<C> cs;
}
