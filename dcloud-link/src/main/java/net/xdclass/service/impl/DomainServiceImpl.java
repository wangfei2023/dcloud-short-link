package net.xdclass.service.impl;

import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manage.DomainManage;
import net.xdclass.model.DomainDO;
import net.xdclass.service.DomainService;
import net.xdclass.vo.DomainVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class DomainServiceImpl implements DomainService {
    @Autowired
    private DomainManage domainManage;
    @Override
    public List<DomainVo> listAll() {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        List<DomainDO> customDomainList = domainManage.ListCustomDomain(accountNo);
        List<DomainDO> officalDomainList = domainManage.ListOfficalDomain();
        customDomainList.addAll(officalDomainList);
        List<DomainVo> domainVoList = customDomainList.stream().map(obj -> BeanProCess(obj)).collect(Collectors.toList());
        return domainVoList;
    }
    public DomainVo  BeanProCess(DomainDO domainDO){
        DomainVo domainVo = new DomainVo();
        BeanUtils.copyProperties(domainDO,domainVo);
        return domainVo;
    }
}
