package net.xdclass.biz;

import net.xdclass.mapper.TrafficMapper;
import net.xdclass.model.TrafficDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
public class TrafficTest {
    @Autowired
    private TrafficMapper trafficMapper;

    @Test
    public void testSaveTraffic() {

        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            TrafficDO trafficDO = new TrafficDO();
            trafficDO.setAccountNo(Long.valueOf(random.nextInt(1000)));
            trafficMapper.insert(trafficDO);
        }
    }
}
