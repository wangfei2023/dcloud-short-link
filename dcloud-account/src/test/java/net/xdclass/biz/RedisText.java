package net.xdclass.biz;

import net.xdclass.AccountApplication;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
    //使用jedisconfig连接redis
    @Test
    public void jedisConnect()  {
        Jedis jedis = jedisPool.getResource();
        jedis.get("link");
     new ArrayList<>();
        List.of("1","2","3");
    }
}
