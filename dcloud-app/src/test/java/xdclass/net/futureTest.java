/**
 * @project dcloud-short-link
 * @description 线程池测试
 * @author Administrator
 * @date 2023/7/21 0021 15:32:02
 * @version 1.0
 */

package xdclass.net;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.ShortLinkWideDO;
import org.junit.Test;

import java.util.concurrent.*;

@Slf4j
public class futureTest {
    @Test
    public void testFuture() throws Exception {
        ShortLinkWideDO shortLinkWideDO = new ShortLinkWideDO();
        shortLinkWideDO.setCity("上海");
        String city = shortLinkWideDO.getCity();
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            //普通的需要循环;
        for (int i = 0; i < 3; i++) {
            Future<String> future = executorService.submit(() ->
            {
                    Thread.sleep(2000);
                    return city;
            });
            System.out.println(future.get());
        }
        executorService.shutdown();

    }
 @Test
    public void testComable() throws Exception {
     CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
         return "学习自定义线程";
     });
     System.out.println(future.get());
 }

    @Test
    public void testFuture3() throws ExecutionException, InterruptedException, TimeoutException {

        //有返回值,默认使用ForkJoinPool.commonPool() 作为它的线程池执行异步代码
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() ->{
            System.out.println("执行任务一");
            return "冰冰一,";
        });

        //有返回值,当前任务正常完成以后执行，当前任务的执行的结果会作为下一任务的输入参数
        CompletableFuture<String> future2 = future1.thenApply((element) -> {
            System.out.println("入参："+element);
            System.out.println("执行任务二");
            return "冰冰二";
        });

        System.out.println("future2返回值:" + future2.get(1, TimeUnit.SECONDS));

    }
}


