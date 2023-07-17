/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/10 0010 19:00:12
 * @version 1.0
 */

package net.xdclass.manager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.PluginTypeEnum;
import net.xdclass.manager.TrafficManage;
import net.xdclass.mapper.TrafficMapper;
import net.xdclass.model.TrafficDO;
import net.xdclass.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TrafficManageaImpl implements TrafficManage {
    @Autowired
    private TrafficMapper trafficMapper;
    @Override
    public int add(TrafficDO trafficDO) {
        int rows = trafficMapper.insert(trafficDO);
        return rows;
    }

    @Override
    public IPage<TrafficDO> pageAvaliable(int page, int size, long accountNo) {
        Page<TrafficDO> pageInfo = new Page<>(page, size);
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");
        Page<TrafficDO> trafficDOPage = trafficMapper.selectPage(pageInfo, new QueryWrapper<TrafficDO>()
                .eq("account_no", accountNo)
                //ge:大于
                .ge("expired_date", today).orderByDesc("gmt_create")
        );
        return trafficDOPage;
    }

    @Override
    public TrafficDO findByIdAndAccountNo(long trafficId, long accountNo) {
        TrafficDO trafficDO = trafficMapper.selectOne(new QueryWrapper<TrafficDO>()
                .eq("account_no", accountNo)
                .eq("id", trafficId)
        );
        return trafficDO;
    }



    @Override
    public boolean deleteExpireTraffic() {
       int rows= trafficMapper.delete(new QueryWrapper<TrafficDO>().le("expired_date",new Date()));
       log.info("删除流量包rows",rows);
        return true;
    }
/**
 * @description TODO 
 * 查找未过期的流量包列表,不一定可用，可能超过次数;
 * 免费的和付费不过期的流量包,免费的流量包没有过期时间
 * select *from traffic where account_no="1111" and expire date>=someday or out_trade_no="free_init"
 * @return 
 * @author 
 * @date  
 */
    @Override
    public List<TrafficDO> selectAvailableTraffics(Long accountNo) {
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");
        QueryWrapper<TrafficDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_no",accountNo);
        //利用函数式编程
        queryWrapper.and(wrapper->wrapper.ge("expired_date",today).or().eq("out_trade_no","free_init"));

        return trafficMapper.selectList(queryWrapper);
    }
/**
 * @description TODO 
 * 增加流量包使用次数;
 * @return 
 * @author 
 * @date  
 */
    @Override
    public int addDayUsedTimes(Long accountNo, Long trafficId, Integer usedTimes) {
        return  trafficMapper.addDayUsedTimes(accountNo,trafficId, usedTimes);
    }
   /**
    * @description TODO 
    * 恢复流量包的使用次数,回滚流量包;
    * @return 
    * @author 
    * @date  
    */
    @Override
    public int releaseUsedTimes(Long accountNo, Long trafficId, Integer usedTimes,String useDateStr) {
        return  trafficMapper.releaseUsedTimes( accountNo,  trafficId,  usedTimes,useDateStr);
    }

    @Override
    public int batchUpdateUsedTimes(Long accountNo, List<Long> unUpdatedTrafficIds) {
        int rows = trafficMapper.update(null, new UpdateWrapper<TrafficDO>()
                .eq("account_no", accountNo)
                .in("id", unUpdatedTrafficIds)
                //将已经用过的改为0;
                .set("day_used", 0)
        );
        return rows;
    }

    public static void main(String[] args) {
        String date = TimeUtil.format(new Date(), "yyyy-MM-dd");
        System.out.println(date);
    }
}


