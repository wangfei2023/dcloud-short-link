package net.xdclass.biz;

import net.xdclass.AccountApplication;
import net.xdclass.config.JedisConfig;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import org.apache.poi.ss.usermodel.Workbook;
import redis.clients.jedis.JedisPool;

import java.io.FileOutputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
public class RedisText {
   @Autowired
   private JedisPool jedisPool;
    @Test
    public void testSaveTraffic() throws IOException {
        Jedis jedis = new Jedis("localhost", 6379);
        String key = "mykey";
        String value = jedis.get(key);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(value);

        FileOutputStream fileOutputStream = new FileOutputStream("output.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }
    //使用jedisconfig连接redis=-
@Test
    public void jedisConnect()  {
//        Jedis jedis = jedisPool.getResource();
//    System.out.println(jedis.get("1"));
//    jedis.close();
    new RedisUtils().get("1");
//        System.out.println(".......");
//        jedis.get("link");
//        List<Jedis> jedisList = List.of(jedis);
//        System.out.println(jedisList);
      //  jedis.hset("myhash", "field1", "value1");

        // 关闭与 Redis 的连接

    }
    @Test
    public void jedisWrite()  {
      //  RedisUtils.get("1");

        // 关闭与 Redis 的连接
       // jedis.close();

    }
}
