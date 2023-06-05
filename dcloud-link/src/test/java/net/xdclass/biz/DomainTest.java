package net.xdclass.biz;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.ShortLinkApplication;
import net.xdclass.manage.DomainManage;
import net.xdclass.model.DomainDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShortLinkApplication.class)
@Slf4j
public class DomainTest {
    @Autowired
    private DomainManage domainManage;
    @Test
    public void testListDomain() {
        List<DomainDO> officalDomainList = domainManage.ListOfficalDomain();
        log.info("打印officalDomainList={}",officalDomainList);
    }
}
