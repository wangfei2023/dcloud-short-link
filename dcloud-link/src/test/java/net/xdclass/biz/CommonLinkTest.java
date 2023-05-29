package net.xdclass.biz;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.ShortLinkApplication;
import net.xdclass.startegy.ShardingDBConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/28 0028 16:28]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/28 0028 16:28]
 * @updateRemark : [说明本次修改内容]
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShortLinkApplication.class)
@Slf4j
public class CommonLinkTest {
    @Test
    public void testRandomDB(){
        for (int i = 0; i < 20; i++) {
           log.info(ShardingDBConfig.getRandomDBPrefix());
        }

    }
}
