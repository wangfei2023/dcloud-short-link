package net.xdclass.manage;

import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.model.LinkGroupDO;
import net.xdclass.vo.LinkGroupVo;

import java.util.List;

public interface LinkGroupManage {
    int add(LinkGroupDO linkGroupDO);

    int del(Long groupId, long accountNo);

    LinkGroupDO detail(Long groupId, long accountNo);

    List<LinkGroupDO> findAllGroup( long accountNo);
}
