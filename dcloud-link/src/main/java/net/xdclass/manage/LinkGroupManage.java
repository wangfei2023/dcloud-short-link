package net.xdclass.manage;

import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.model.LinkGroupDO;

public interface LinkGroupManage {
    int add(LinkGroupDO linkGroupDO);
    int del(Long groupId, long accountNo);

}
