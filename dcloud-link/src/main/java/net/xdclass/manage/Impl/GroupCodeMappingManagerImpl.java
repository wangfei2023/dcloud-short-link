package net.xdclass.manage.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.manage.GroupCodeMappingManager;
import net.xdclass.mapper.GroupCodeMappingMapper;
import net.xdclass.model.GroupCodeMappingDO;
import net.xdclass.vo.GroupCodeMappingVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/29 0029 23:24]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/29 0029 23:24]
 * @updateRemark : [说明本次修改内容]
 */
@Component
@Slf4j
public class GroupCodeMappingManagerImpl implements GroupCodeMappingManager {
    @Autowired
    private GroupCodeMappingMapper groupCodeMappingMapper;
    @Override
    public GroupCodeMappingDO findByGroupIdAndMappingId(Long mappingId, Long accountNo, Long groupId) {
        GroupCodeMappingDO groupCodeMappingDO = groupCodeMappingMapper.selectOne(new QueryWrapper<GroupCodeMappingDO>()
                .eq("id", mappingId)
                .eq("account_no", accountNo)
                .eq("group_id", groupId)

        );
        return groupCodeMappingDO;
    }

    @Override
    public int add(GroupCodeMappingDO groupCodeMappingDO) {
        return  groupCodeMappingMapper.insert(groupCodeMappingDO);
    }

    @Override
    public int del(String shortLinkCode, Long accountNo, Long groupId) {
        int rows = groupCodeMappingMapper.update(null, new UpdateWrapper<GroupCodeMappingDO>()
                .eq("code", shortLinkCode)
                .eq("account_no", accountNo)
                .eq("group_id", groupId).set("del",1)
        );
        return rows;
    }

    @Override
    public Map<String, Object> pageShortLinkByGroupId(Integer page, Integer size, Long accountNo, Long groupId) {
        Page<GroupCodeMappingDO> groupPageCodeMappingDO = new Page<>(page, size);
        Page<GroupCodeMappingDO> groupPage = groupCodeMappingMapper.selectPage(groupPageCodeMappingDO, new QueryWrapper<GroupCodeMappingDO>()
                .eq("account_no", accountNo)
                .eq("group_id", groupId)
        );
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("total_page",groupPageCodeMappingDO.getPages());
        map.put("total_recode",groupPageCodeMappingDO.getTotal());
        map.put("current_data",groupPageCodeMappingDO.getRecords()
        .stream().map(obj->beanProcess(obj)).collect(Collectors.toList()));
        return map;
    }

    @Override
    public int updateGroupCodeMappingState(String shortLinkCode, Long accountNo, Long groupId, ShortLinkStateEnum shortLinkStateEnum) {
        int rows = groupCodeMappingMapper.update(null, new UpdateWrapper<GroupCodeMappingDO>()
                .eq("code", shortLinkCode)
                .eq("account_no", accountNo)
                .eq("group_id", groupId)
        );
        return rows;
    }

    @Override
    public GroupCodeMappingDO findByCodeAndGroupId(String shortLinkCode, Long groupId, Long accountNo) {
        GroupCodeMappingDO groupCodeMappingDO = groupCodeMappingMapper.selectOne(new QueryWrapper<GroupCodeMappingDO>()
                .eq("code", shortLinkCode)
                .eq("account_no", accountNo)
                .eq("group_id", groupId)

        );
        return groupCodeMappingDO;
    }

    private GroupCodeMappingVO beanProcess(GroupCodeMappingDO groupPageCodeMappingDO){
        GroupCodeMappingVO groupCodeMappingVO = new GroupCodeMappingVO();
        BeanUtils.copyProperties(groupPageCodeMappingDO,groupCodeMappingVO);
        return groupCodeMappingVO;
    }
}
