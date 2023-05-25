package net.xdclass.manage.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.manage.LinkGroupManage;
import net.xdclass.mapper.LinkGroupMapper;
import net.xdclass.model.LinkGroupDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/24 0024 22:33]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/24 0024 22:33]
 * @updateRemark : [说明本次修改内容]
 */
@Component
public class LinkGroupManageImpl implements LinkGroupManage {
    @Autowired
    private LinkGroupMapper linkGroupMapper;
    @Override
    public int add(LinkGroupDO linkGroupDO) {
        int rows = linkGroupMapper.insert(linkGroupDO);
        return rows;
    }

    @Override
    public int del(Long groupId,long accountNo) {
        int rows = linkGroupMapper.delete(new QueryWrapper<LinkGroupDO>().eq("id", groupId).eq("account_no", accountNo));
        return rows;
    }
}
