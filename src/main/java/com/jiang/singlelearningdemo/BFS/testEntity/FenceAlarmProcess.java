package com.jiang.singlelearningdemo.BFS.testEntity;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jiang.singlelearningdemo.BFS.Dict;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 该表是个子表, 服务于主表fence_alarm_history, 用于记录围栏告警事件的处理时间轴
 * @Author: jeecg-boot
 * @Date:   2025-10-16
 * @Version: V1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FenceAlarmProcess implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    private java.lang.String id;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    /**围栏告警记录id*/
    private java.lang.String fenceAlarmHistoryId;
    /**误报原因code(是个字典, 只有当这个字典值为"其他"的时候才会读取misinformation_reason_context)*/
    private java.lang.String misinformationReasonCode;
    /**"其他"误报原因选项描述*/
    private java.lang.String misinformationReasonContext;
    /**处理人*/
    @Dict(dicCode = "fence_alarm_status")
    private java.lang.String processUser;
    /**处理状态(和fence_alarm_history一个意思, 只不过主表保证更新到最新字段, 这里每一个新状态都要insert一个新纪录)*/
    private java.lang.String processStatus;
    /**处理状态的描述, 比如状态为4(转工单),那么此处显示的是工单号*/
    private java.lang.String statusDescription;

}
