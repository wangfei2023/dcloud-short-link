package net.xdclass.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage  implements Serializable {
    /**
     * 消息队列id
     */
    private String messageId;

    /**
     * 事件类型
     */
    private String eventMessageType;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 账号
     */
    private Long accountNo;

    /**
     * 消息体
     */
    private String content;

    /**
     * 异常备注
     */
    private String remark;
}
