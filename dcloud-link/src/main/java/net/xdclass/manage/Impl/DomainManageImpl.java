package net.xdclass.manage.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.manage.DomainManage;
import net.xdclass.mapper.DomainMapper;
import net.xdclass.model.DomainDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/30 0030 23:47]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/30 0030 23:47]
 * @updateRemark : [说明本次修改内容]
 */
@Component
@Slf4j
public class DomainManageImpl  implements DomainManage {
    @Autowired
    private DomainMapper domainMapper;
    @Override
    public DomainDO findById(Long id, Long accountNo) {

        return   domainMapper.selectOne(new QueryWrapper<DomainDO>()
                .eq("account_no",accountNo)
                .eq("id",id)
        );
    }

    @Override
    public DomainDO findDomainTypeAndId(Long id, DomainTypeEnum domainTypeEnum) {
        return   domainMapper.selectOne(new QueryWrapper<DomainDO>()
                .eq("domain_type",domainTypeEnum.name())
                .eq("id",id)
        );
    }

    @Override
    public int addDomainDO(DomainDO domainDO) {

        return domainMapper.insert(domainDO);
    }

    @Override
    public List<DomainDO> ListOfficalDomain() {
        return  domainMapper.selectList(new QueryWrapper<DomainDO>().eq("domain_type",DomainTypeEnum.OFFICIAL.name()));
    }

    @Override
    public List<DomainDO> ListCustomDomain(Long accountNo) {
        return  domainMapper.selectList(new QueryWrapper<DomainDO>().eq("domain_type",DomainTypeEnum.CUSTOM.name()).eq("account_no",accountNo));
    }
}
