package net.xdclass.controller;


import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.service.LinkGroupService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.LinkGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
  /**

   *@描述 根据id找详情;

   *@参数

   *@返回值
   *
   *@创建时间  2023/5/26 0026
   */
  @GetMapping
  public JsonData detail(@PathVariable("group_id") Long groupId ){
      LinkGroupVo linkGroupVo=linkGroupService.detail(groupId);
      return JsonData.buildSuccess(linkGroupVo);
  }

    /**

     *@描述 列出用户全部分组

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/26 0026
     */
    @GetMapping("/list")
    public JsonData findUserAllGroup(){
        List<LinkGroupVo> linkGroupVoList = linkGroupService. findAllGroup();
        return JsonData.buildSuccess(linkGroupVoList);
    }
}

