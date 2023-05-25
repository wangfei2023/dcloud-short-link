package net.xdclass.controller;


import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.service.LinkGroupService;
import net.xdclass.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MrWang
 * @since 2023-05-23
 */
@RestController
@RequestMapping("/api/group/v1")
public class LinkGroupController {
    @Autowired
    private LinkGroupService linkGroupService;
    /**

     *@描述 添加短链分组

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/25 0025

     */
   @PostMapping("/add")
    public JsonData add(@RequestBody LinkGroupAddRequest linkGroupAddRequest){
       int rows = linkGroupService.add(linkGroupAddRequest);
       return rows==1?JsonData.buildResult(BizCodeEnum.GROUP_ADD_SUCCESS):JsonData.buildResult(BizCodeEnum.GROUP_ADD_FAIL);
   }
  /**

  *@描述  删除短链分组

  *@参数

  *@返回值
  *
  *@创建时间  2023/5/25 0025

  */
  @DeleteMapping("/del/{group_id}")
  public JsonData del(@PathVariable("group_id") Long groupId ){
      int rows = linkGroupService.del(groupId);
      return rows==1?JsonData.buildResult(BizCodeEnum.GROUP_ADD_SUCCESS):JsonData.buildResult(BizCodeEnum.GROUP_NOT_EXIST);
  }
}

