package net.xdclass.biz;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.utils.CommonUtil;
import org.junit.Test;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : 短链生成组件ShortLinkComponent封装**
 * @createTime : [2023/5/23 0023 20:59]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/23 0023 20:59]
 * @updateRemark : 使用的Murmur哈希生成短链；生成十进制;
 */
@Slf4j
public class ShortLinkTest {
    @Test
    public void  testMurmurHash(){
        String originalUrl="http://www.baidu.com?"+ CommonUtil.generateUUID()+"pwd="+CommonUtil.getStringNumRandom(7);
        long murmur3_32 = Hashing.murmur3_32().hashUnencodedChars(originalUrl).padToLong();
        log.info("打印murmur3_32={}",murmur3_32);
    }
}
