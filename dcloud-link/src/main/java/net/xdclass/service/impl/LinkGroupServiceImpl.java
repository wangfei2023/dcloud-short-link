package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manage.LinkGroupManage;
import net.xdclass.model.LinkGroupDO;
import net.xdclass.service.LinkGroupService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.LinkGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/23 0023 23:37]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/23 0023 23:37]
 * @updateRemark : [说明本次修改内容]
 */
@Service
@Slf4j
public class LinkGroupServiceImpl implements LinkGroupService {
    @Autowired
    private LinkGroupManage linkGroupManage;
    @Override
    public int add(LinkGroupAddRequest linkGroupAddRequest) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        System.out.println(accountNo);
        log.info("accountNo={}",accountNo);
        LinkGroupDO linkGroupDO = LinkGroupDO.builder().accountNo(accountNo)
                .title(linkGroupAddRequest.getTitle()).build();
        int rows = linkGroupManage.add(linkGroupDO);
        return rows;
    }

    @Override
    public int del(Long groupId) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        int rows=linkGroupManage.del(groupId,accountNo);
        return rows;
    }

    @Override
    public LinkGroupVo detail(Long groupId) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        LinkGroupDO linkGroupDo=linkGroupManage.detail(groupId,accountNo);
        LinkGroupVo linkGroupVo = new LinkGroupVo();
        BeanUtils.copyProperties(linkGroupDo,linkGroupVo);
        return linkGroupVo;
    }

    @Override
    public List<LinkGroupVo> findAllGroup() {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        List<LinkGroupDO>linkListGroup =  linkGroupManage.findAllGroup(accountNo);
        List<LinkGroupVo> linkGroupVoList = linkListGroup.stream().map(group -> {
            LinkGroupVo linkGroupVo = new LinkGroupVo();
            BeanUtils.copyProperties(group, linkGroupVo);
            return linkGroupVo;
        }).collect(Collectors.toList());
        return linkGroupVoList;
    }
}
