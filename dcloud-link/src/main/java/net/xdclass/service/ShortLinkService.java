package net.xdclass.service;

import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.controller.request.ShortLinkPageRequest;
import net.xdclass.model.EventMessage;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.ShortLinkVo;

import java.util.Map;

public interface ShortLinkService {
    /**

     *@描述 解析短链

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/28 0028


     */
    ShortLinkVo parseLinkCode(String linkCode);

    /**

     *@描述 创建短链

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/28 0028


     */
    JsonData createShortLink(ShortLinkAddRequest request);

    /**
     * @description 处理新增短链消息
     *
     * @return
     * @author
     * @date
     */

    boolean handerAddShortLink(EventMessage eventMessage);
    /**
     * @description TODO
     * 分页查找短链
     * @return
     * @author
     * @date
     *
     */
    Map<String,Object>  pageShortLinkByGroupId(ShortLinkPageRequest request);
}
