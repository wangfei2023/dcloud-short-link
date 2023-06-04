package net.xdclass.controller;

import net.xdclass.service.DomainService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.DomainVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/29 0029 23:02]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/29 0029 23:02]
 * @updateRemark : [说明本次修改内容]
 */
@RestController
@RequestMapping("/api/domain/v1")
public class DomainController {
    @Autowired
    private DomainService domainService;
    public JsonData list(){
        List<DomainVo>list= domainService.listAll();
        return JsonData.buildSuccess();
    }
}
