package net.xdclass.biz;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.ShortLinkApplication;
import net.xdclass.compant.ShortLinkComponent;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.model.ShortLinkDO;
import net.xdclass.utils.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : 短链生成组件ShortLinkComponent封装**
 * @createTime : [2023/5/23 0023 20:59]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/23 0023 20:59]
 * @updateRemark : 使用的Murmur哈希生成短链；生成十进制;
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShortLinkApplication.class)
@Slf4j
public class ShortLinkTest {
    @Autowired
    private ShortLinkComponent shortLinkComponent;
    @Autowired
    private ShortLinkManager shortLinkManager;
    @Test
    public void testMurmurHash() {
        String originalUrl = "http://www.baidu.com?" + CommonUtil.generateUUID() + "pwd=" + CommonUtil.getStringNumRandom(7);
        long murmur3_32 = Hashing.murmur3_32().hashUnencodedChars(originalUrl).padToLong();
        log.info("打印murmur3_32={}", murmur3_32);
    }

    /**
     * 测试生成62位短链平台
     */
    @Test
    public void testCreateShortLink() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int num1 = random.nextInt(10);
            int num2 = random.nextInt(1000000);
            int num3 = random.nextInt(1000000);
            String originalUrl = num1 + "xdclass" + num2 + ".net" + num3;
            String shortLinkCode = shortLinkComponent.createShortLinkCode(originalUrl);
            log.info("originalUrl:" + originalUrl + ", shortLinkCode=" + shortLinkCode);
        }
    }
   /**

    *@描述 保存短链

    *@参数

    *@返回值
    *
    *@创建时间  2023/5/28 0028


    */
    @Test
    public void testSaveShortLink() {
        Random random = new Random();
            int num1 = random.nextInt(10);
            int num2 = random.nextInt(1000000);
            int num3 = random.nextInt(1000000);
            String originalUrl = num1 + "xdclass" + num2 + ".net" + num3;
            String shortLinkCode = shortLinkComponent.createShortLinkCode(originalUrl);
            ShortLinkDO shortLinkDO = new ShortLinkDO();
            shortLinkDO.setDel(0);
            shortLinkDO.setAccountNo((Long.valueOf(num3)));
            shortLinkDO.setSign(CommonUtil.MD5(originalUrl));
            shortLinkDO.setCode(shortLinkCode);
            shortLinkManager.addShortLink(shortLinkDO);
    }
    /**

     *@描述 查找短链

     *@参数  []

     *@返回值  void
     *
     *@创建时间  2023/5/28 0028


     */
    @Test
    public void testFind() {
        ShortLinkDO linkCode = shortLinkManager.findByShortLinkCode("1P7Ttl0");
        log.info("根据短链code查询短链={}",linkCode);
    }
    /**

     *@描述 随机生成短链码

     *@参数  []

     *@返回值  void
     *
     *@创建时间  2023/5/28 0028


     */
    @Test
    public void testGenCode() {
        for (int i = 0; i < 5; i++) {
            String url="https://xdclass.net/download.html";
            String shortlinkCode=shortLinkComponent.createShortLinkCode(url);
            System.out.println(shortlinkCode);
        }
    }
 }

