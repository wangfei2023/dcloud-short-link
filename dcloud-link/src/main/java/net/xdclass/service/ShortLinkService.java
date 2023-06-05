package net.xdclass.service;

import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.ShortLinkVo;

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
}
