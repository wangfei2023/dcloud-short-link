/**
 * @project dcloud-short-link
 * @description 使用流量包
 * @author Administrator
 * @date 2023/7/11 0011 10:11:19
 * @version 1.0
 */

package net.xdclass.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UseTrafficRequest {
    /**
     * @description TODO 
     * 账号
     * @return 
     * @author 
     * @date  
     */
    private Long accountNo;


    /**
     * @description TODO
     * 业务的id,短链码;
     * @return
     * @author
     * @date
     */
    private String bizId;

    
}


