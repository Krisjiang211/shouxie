package com.jiang.singlelearningdemo.BFS;

import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiang.singlelearningdemo.BFS.testEntity.A;
import com.jiang.singlelearningdemo.BFS.testEntity.FenceAlarmProcessVO;
import lombok.*;
//import org.jeecg.common.system.vo.DictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSONObject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author krisJiang
 * 目前只支持对原生Obj中, 第一层是List的切带有Dict注解的字段进行解析
 * 注意: 第一层的List的泛型不要是Object, 否则会报错
 */
@Component
public class ObjDictParse {



    @SneakyThrows
    public static void main(String[] args) {
        A a = A.getInstance();
        ObjDictParse dictParse = new ObjDictParse();
        for (Level level : dictParse.parseToLevels(a, a.getClass())) {
            System.out.println(level);
        }
        /**
         * ObjDictParse.Level(pathList=[a2], dictValue=a2, dictCode=null, dictText=解析好的值, parsedValueStr=a2dictParse)
         * ObjDictParse.Level(pathList=[b, b2], dictValue=b2, dictCode=null, dictText=解析好的值, parsedValueStr=b2dictParse)
         * ObjDictParse.Level(pathList=[c, c2], dictValue=c2, dictCode=null, dictText=解析好的值, parsedValueStr=c2dictParse)
         * ObjDictParse.Level(pathList=[b, c, c2], dictValue=c2, dictCode=null, dictText=解析好的值, parsedValueStr=c2dictParse)
         */
        long start = System.currentTimeMillis();
        JSONObject resJson = dictParse.parse(a,A.class);
        long end = System.currentTimeMillis();
        System.out.println(resJson+"\n耗时: " + (end - start));
        /**
         * {"a2":"a2","b":{"b1":"b1","b2":"b2","c":{"c1":"c1","c2":"c2","c2dictParse":"解析好的值"},"b2dictParse":"解析好的值"},"c":{"c1":"c1","c2":"c2","c2dictParse":"解析好的值"},"a2dictParse":"解析好的值"}
         */
        boolean b = resJson.get("a2") instanceof JSONArray;
        System.out.println("b = " + b);

        //性能测试
        String jsonStr = "{\"records\":[{\"id\":\"1979028090273681410\",\"createTime\":\"2025-10-20 11:34:00\",\"fenceAlarmHistoryId\":\"2\",\"misinformationReasonCode\":\"1\",\"misinformationReasonContext\":null,\"processUser\":\"kris\",\"processStatus\":\"2\",\"statusDescription\":null,\"processStatus_dictText\":\"处理中\"},{\"id\":\"1979070198800003074\",\"createTime\":\"2025-10-20 14:21:19\",\"fenceAlarmHistoryId\":\"2\",\"misinformationReasonCode\":null,\"misinformationReasonContext\":null,\"processUser\":\"kris\",\"processStatus\":\"2\",\"statusDescription\":null,\"processStatus_dictText\":\"处理中\"},{\"id\":\"1981264361502371842\",\"createTime\":\"2025-10-23 15:40:09\",\"fenceAlarmHistoryId\":\"5\",\"misinformationReasonCode\":\"0\",\"misinformationReasonContext\":\"test1111\",\"processUser\":\"admin\",\"processStatus\":\"5\",\"statusDescription\":null,\"processStatus_dictText\":\"误报\"},{\"id\":\"p1\",\"createTime\":\"2025-10-17 02:59:20\",\"fenceAlarmHistoryId\":\"1\",\"misinformationReasonCode\":null,\"misinformationReasonContext\":null,\"processUser\":\"admin\",\"processStatus\":\"0\",\"statusDescription\":\"未处理\",\"processStatus_dictText\":\"未处理\"},{\"id\":\"p2\",\"createTime\":\"2025-10-17 03:04:20\",\"fenceAlarmHistoryId\":\"1\",\"misinformationReasonCode\":null,\"misinformationReasonContext\":null,\"processUser\":\"admin\",\"processStatus\":\"1\",\"statusDescription\":\"已确认告警，开始处理\",\"processStatus_dictText\":\"已确认\"},{\"id\":\"p3\",\"createTime\":\"2025-10-17 03:09:20\",\"fenceAlarmHistoryId\":\"1\",\"misinformationReasonCode\":null,\"misinformationReasonContext\":null,\"processUser\":\"admin\",\"processStatus\":\"2\",\"statusDescription\":\"处理中，现场核查中\",\"processStatus_dictText\":\"处理中\"},{\"id\":\"p4\",\"createTime\":\"2025-10-17 03:29:20\",\"fenceAlarmHistoryId\":\"1\",\"misinformationReasonCode\":null,\"misinformationReasonContext\":null,\"processUser\":\"王强\",\"processStatus\":\"3\",\"statusDescription\":\"告警处理完成\",\"processStatus_dictText\":\"已完成\"},{\"id\":\"p5\",\"createTime\":\"2025-10-17 03:39:20\",\"fenceAlarmHistoryId\":\"1\",\"misinformationReasonCode\":null,\"misinformationReasonContext\":null,\"processUser\":\"王强\",\"processStatus\":\"4\",\"statusDescription\":\"工单号：GD20251017-001\",\"processStatus_dictText\":\"转工单\"},{\"id\":\"p6\",\"createTime\":\"2025-10-17 03:49:20\",\"fenceAlarmHistoryId\":\"1\",\"misinformationReasonCode\":\"OTHER\",\"misinformationReasonContext\":\"误触发告警\",\"processUser\":\"李华\",\"processStatus\":\"5\",\"statusDescription\":\"误报处理完成\",\"processStatus_dictText\":\"误报\"}]}";
        ObjectMapper mapper = new ObjectMapper();
        FenceAlarmProcessVO fenceAlarmHistoryVO = mapper.readValue(jsonStr, FenceAlarmProcessVO.class);
        int i = 0;
        while (i<20){
            long s = System.currentTimeMillis();
            System.out.println(dictParse.parse(fenceAlarmHistoryVO, FenceAlarmProcessVO.class));
            long e = System.currentTimeMillis();
            System.out.println("耗时: " + (e - s));
            i++;
        }
    }


    @SneakyThrows
    public <T> JSONObject parse(T obj, Class<?> clazz) {

        //获取到需要解析的字段的路径
        List<Level> levels = parseToLevels(obj, clazz);
        //按照获取的层级, 放入到JSONObject
        JSONObject resJson = JSONObject.parseObject(JSONObject.toJSONString(obj));
        //初步结果
        JSONObject formerJSON = parseByLevels(levels, resJson);
        //可以解析第一层List类型的Dict注解
        List<Field> listDict = getListDict(clazz);
        if (!listDict.isEmpty()){
            for (Field field : listDict) {
                JSONArray oneFieldJsonArray = new JSONArray();
                List<?> listField =(List<?>) field.get(obj);
                for (Object o : listField) {
                    JSONObject parse = parse(o, o.getClass());
                    oneFieldJsonArray.add(parse);
                }
                formerJSON.put(field.getName(),oneFieldJsonArray);
            }
        }
        return formerJSON;
    }

    //看能否获取到主Obj内部字段有没有也需要解析的List类型的Dict注解
    private List<Field> getListDict(Class<?> clazz){
        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType()==List.class){
                //拿到List中的元素类型
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType){
                    Class<?> actualType = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                    for (Field elemField : actualType.getDeclaredFields()) {
                        elemField.setAccessible(true);
                        if (elemField.isAnnotationPresent(Dict.class)) {
                            fields.add(field);
                        }
                    }
                }
            }
        }
        return fields;
    }



    //根据Levels, 解析出JSONObject
    private JSONObject parseByLevels(List<Level> levels,JSONObject objJSON){
        for (Level level : levels) {
            JSONObject currentJson = objJSON;
            if (level.getPathList().size()==1){
                currentJson.put(level.getParsedValueStr(),level.getDictText());
            }else {
                for (int i = 0; i < level.getPathList().size() - 1; i++) {
                    //更新currentJson为下一个层级的JSONObject
                    currentJson = (JSONObject) currentJson.get(level.getPathList().get(i));
                }
                currentJson.put(level.getParsedValueStr(),level.getDictText());
            }
        }
        return objJSON;
    }


    @SneakyThrows
    public List<Level> parseToLevels(Object obj, Class<?> clazz) {
        //先不考虑List这种情况
        List<Level> levels = new ArrayList<>();
        Queue<ClassInfo> queue = new LinkedList<>();
        List<String> currentPath = new ArrayList<>();
        Object fatherObj = obj;
        //广度优先遍历
        queue.offer(new ClassInfo(clazz,"",currentPath,obj));
        while (!queue.isEmpty()){
            //一直要保持一个对象字段的实例, 因为我们需要从他来拿值
            ClassInfo current = queue.poll();
            fatherObj = current.getObj();
            currentPath = current.getPathList();

            for (Field field : current.getClazz().getDeclaredFields()) {
                field.setAccessible(true);
                Object currentObj = field.get(fatherObj);//当前字段的对象, 用于拿值
                if (currentObj == null){
                    continue;
                }
                String name = field.getName();
                //如果存在Dict注解标注, 那么不入队
                if (field.isAnnotationPresent(Dict.class)) {
                    //拿到注解的值
                    Dict annotation = field.getAnnotation(Dict.class);
                    ArrayList<String> pathList = new ArrayList<>(currentPath);
                    pathList.add(name);
                    Level level = Level.builder()
                            .pathList(pathList)
                            .dictValue(currentObj)
                            .dictText(parseByDictCode(annotation.dicCode(),currentObj))
                            .dictCode(annotation.dicCode())
                            .parsedValueStr(name+"dictParse").build();//TODO 后来要修改这个解析的值的名字哈
                    levels.add(level);
                    continue;
                }
                //如果为java基础类, 那么不入队
                Class<?> fieldType = field.getType();
                if (fieldType.isPrimitive()
                        || fieldType == String.class
                        || Number.class.isAssignableFrom(fieldType)
                        || Boolean.class.isAssignableFrom(fieldType)
                        || Character.class.isAssignableFrom(fieldType)
                        || fieldType.isEnum()
                        || fieldType.isArray()) {
                    continue;
                }
                //如果为其他类, 那么入队
                List<String> pathList = new ArrayList<>(current.getPathList());
                pathList.add(name);//路径add
                queue.offer(new ClassInfo(fieldType,name, pathList,currentObj));
            }
        }


        return levels;
    }


    //根据字典编码和字典值解析出字典文本
    public String parseByDictCode(String dictCode,Object dictValue){
        return "解析好的值";
        //TODO 实际应用取消下方注释
//        return commonAPI.queryEnableDictItemsByCode(dictCode).stream()
//                .filter(item -> item.getValue().equals(dictValue))
//                .findFirst()
//                .map(DictModel::getText)
//                .orElse("未知字典: "+dictCode+"->"+dictValue);
    }

    @Data
    @AllArgsConstructor
    public static class ClassInfo{
        private Class<?> clazz;
        private String fieldName;
        private List<String> pathList;
        private Object obj;
    }



    @Data
    @Builder
    @ToString
    public static class Level {
        private List<String> pathList;
        private Object dictValue;
        private String dictCode;
        private String dictText;
        private String parsedValueStr;
    }
}
