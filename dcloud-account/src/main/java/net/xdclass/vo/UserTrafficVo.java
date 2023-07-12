/**
 * @project dcloud-short-link
 * @description 流量包更新
 * @author Administrator
 * @date 2023/7/12 0012 18:28:03
 * @version 1.0
 */

package net.xdclass.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.xdclass.model.TrafficDO;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTrafficVo {
    /**
     * @description TODO 
     * 天剩余可用次数 =总次数 - 已用次数       
     * @return 
     * @author 
     * @date  
     */
    private  Integer dayTotalLeftTimes;
    
    
    /**
     * @description TODO 
     * 当前使用的流量包       
     * @return
     * @author 
     * @date  
     */
    private TrafficDO  currentTrafficDo;

    /**
     * @description TODO
     * 记录未过期，但今天未更新;
     * @return
     * @author
     * @date
     */

    private List<Long> unUpdateTrafficIds;
}


