package com.jiang.singlelearningdemo.objPool.test;

import com.jiang.singlelearningdemo.common.pojo.PlainUser;
import com.jiang.singlelearningdemo.objPool.HashTableDataStructure;
import com.jiang.singlelearningdemo.objPool.QueueDataStructure;
import com.jiang.singlelearningdemo.objPool.defaultImpl.DefaultObjPoolUtil;
import com.jiang.singlelearningdemo.objPool.ObjPool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@RestController
@RequestMapping("pool/obj/test")
public class ObjPoolTest {

    {
        constructorMap.put(String.class,"中国");
    }

    private final ExecutorService executors = Executors.newFixedThreadPool(5);

    private static final HashMap<Class<?>, Object> constructorMap = new HashMap<>();

//    private final QueueDataStructure<PlainUser> queueDataStructure =QueueDataStructure.newDefault(2,new PlainUserGivebackStrategy());

    private final HashTableDataStructure<PlainUser> dataStructure = HashTableDataStructure.newDefault(2,new PlainUserGivebackStrategy());
    private final ObjPool<PlainUser> objPool = ObjPool.newDefaultObjPool("User类对象池",
            PlainUser.class,
            constructorMap,
            dataStructure);


    @GetMapping("status")
    public String getStatus(){
        return objPool.getStatus();
    }

    @GetMapping("apply/one/10s")
    public String applyOne10s() throws InterruptedException {
        ObjPool.Obj<PlainUser> apply = objPool.apply(1000);
        if (apply==null){
            return "取对象失败, 对象池没有对象了";
        }
        PlainUser plainUser = apply.getObj();
        plainUser.setName(getOneRandomName());
        plainUser.setAge(random.nextInt(80)+1);
        Thread.sleep(10000);
        objPool.giveBack(apply);
        return "有借有还, 再接不难";
    }



    private final ArrayList<String> firstNameList = new ArrayList<>(Arrays.asList("姜","陈","王","李","黄"));
    private final ArrayList<String> lastNameList = new ArrayList<>(Arrays.asList("明","芳","胜","凯","旋","信","沙","仨","发","擦","大","新","昕"));
    private final Random random = new Random();
    private String getOneRandomName(){
        return firstNameList.get(random.nextInt(firstNameList.size())) +
                lastNameList.get(random.nextInt(lastNameList.size())) +
                lastNameList.get(random.nextInt(lastNameList.size()));
    }
}
