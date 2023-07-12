package net.xdclass.biz;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.AccountApplication;
import net.xdclass.manager.TrafficManage;
import net.xdclass.mapper.TrafficMapper;
import net.xdclass.model.TrafficDO;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
@Slf4j
public class TrafficTest {
    @Autowired
    private TrafficMapper trafficMapper;

    @Autowired
    private TrafficManage trafficManage;

    @Test
    public void testSaveTraffic() {

        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            TrafficDO trafficDO = TrafficDO.builder()
                    .accountNo(Long.valueOf(random.nextInt(1000))).build();
            trafficMapper.insert(trafficDO);
        }
    }

    @Test
    public void testSelectAvailableTraffics() {

        List<TrafficDO> trafficDOList = trafficManage.selectAvailableTraffics(883671476425719808L);
        if (trafficDOList.size()>0){
            trafficDOList.stream().forEach(obj->{
                log.info("打印未过期流量包",obj);
            });
        }
    }

    @Test
    public void testAddDayUsedTimes() {

        int rows = trafficManage.addDayUsedTimes(883671476425719808L, 1678430897485475841L, 1);
        log.info("打印rows:{}",rows);
    }

    @Test
    public void testReleaseUsedTimes() {

        int rows = trafficManage.addDayUsedTimes(883671476425719808L, 1678430897485475841L, 1);
        log.info("打印rows:{}",rows);
    }
}
