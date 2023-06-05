package net.xdclass.manage;

import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.model.DomainDO;

import java.util.List;

public interface DomainManage {
    /**

     *@描述 查找详情

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/30 0030


     */
    DomainDO findById(Long id,Long accountNo);
    /**

     *@描述 查找详情

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/30 0030


     */
    DomainDO findDomainTypeAndId(Long id, DomainTypeEnum domainTypeEnum);
    /**

     *@描述 新增

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/30 0030


     */
    int addDomainDO(DomainDO domainDO);

    /**

     *@描述 查找全部官方的域名

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/30 0030


     */
    List<DomainDO> ListOfficalDomain();
    /**

     *@描述 查找全部自定义的域名

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/30 0030


     */
    List<DomainDO> ListCustomDomain(Long accountNo);
}
