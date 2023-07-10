package net.xdclass.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author MrWang
 * @since 2023-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("traffic")
@Builder
public class TrafficDO implements Serializable {

    private static final long serialVersionUID = 1L;
   //使用mybatis,注释掉，使用shardingjdbc的方式;
    //  @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 每天限制多少条，短链
     */
    private Integer dayLimit;

    /**
     * 当天用了多少条，短链
     */
    private Integer dayUsed;

    /**
     * 总次数，活码才用
     */
    private Integer totalLimit;

    /**
     * 账号
     */
    private Long accountNo;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 产品层级：FIRST青铜、SECOND黄金、THIRD钻石
     */
    private String level;

    /**
     * 过期日期
     */
    private Date expiredDate;

    /**
     * 插件类型
     */
    private String pluginType;

    /**
     * 商品主键
     */
    private Long productId;

    private Date gmtCreate;

    private Date gmtModified;


}
