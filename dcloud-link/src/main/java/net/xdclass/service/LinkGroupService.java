package net.xdclass.service;

import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.LinkGroupVo;

import java.util.List;

public interface LinkGroupService {
   /**

    *@描述 新增分组

    *@参数

    *@返回值
    *
    *@创建时间  2023/5/25 0025


    */
    int add(LinkGroupAddRequest linkGroupAddRequest);

    /**

     *@描述 删除分组

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/25 0025

     */
    int del(Long groupId);

    /**

     *@描述 根据id找详情;

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/25 0025

     */
    LinkGroupVo  detail(Long groupId);

    /**

     *@描述  列出用户全部分组

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/26 0026

     */
    List<LinkGroupVo>  findAllGroup();
}
