package net.xdclass.service;

import net.xdclass.utils.JsonData;

import javax.servlet.http.HttpServletRequest;

public interface LogService {
    /**
     * @description TODO 
     * 记录日志;
     * @return 
     * @author 
     * @date  
     */
    void recodeShortLinkLog(HttpServletRequest request, String shortLinkCode,Long accountNo);
}
