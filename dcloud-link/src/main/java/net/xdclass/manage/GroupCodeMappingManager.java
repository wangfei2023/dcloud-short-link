package net.xdclass.manage;

import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.model.GroupCodeMappingDO;

import java.util.Map;

public interface GroupCodeMappingManager {
    /**

     *@描述 查找详情

     *@参数

     *@返回值根据分片建查找;
     *
     *@创建时间  2023/5/29 0029


     */
    GroupCodeMappingDO findByGroupIdAndMappingId(Long mappingId,Long accountNo,Long groupId);
    /**

     *@描述

     *@参数  新增

     *@返回值
     *
     *@创建时间  2023/5/29 0029


     */
    int add(GroupCodeMappingDO groupCodeMappingDO);

    /**

     *@描述 根据短链码删除;

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/29 0029


     */
    int del(String shortLinkCode ,Long accountNo,Long groupId);

    /**

     *@描述 分页查询;

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/29 0029


     */
    Map<String,Object> pageShortLinkByGroupId(Integer page,Integer size,Long accountNo,Long groupId);
    
/**
 * @description TODO 
 * 更新短链码状态       
 * @return
 * @author 
 * @date  
 */
    int updateGroupCodeMappingState(String shortLinkCode , Long accountNo, Long groupId, ShortLinkStateEnum shortLinkStateEnum);
    /**
     * @description TODO
     * 查找是否存在短链码
     * @return
     * @author
     * @date
     */
    void findByCodeAndGroupId(String shortLinkCode, Long groupId, Long accountNo);
}
