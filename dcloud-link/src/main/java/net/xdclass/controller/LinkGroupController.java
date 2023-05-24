package net.xdclass.controller;


import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.service.LinkGroupService;
import net.xdclass.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
   @PostMapping("/add")
    public JsonData add(@RequestBody LinkGroupAddRequest linkGroupAddRequest){
       int rows = linkGroupService.add(linkGroupAddRequest);
       return rows==1?JsonData.buildResult(BizCodeEnum.GROUP_ADD_SUCCESS):JsonData.buildResult(BizCodeEnum.GROUP_ADD_FAIL);
   }
}

