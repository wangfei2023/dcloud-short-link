/**
 * @project dcloud-short-link
 * @description流量包任务表相关业务;
 * @author Administrator
 * @date 2023/7/13 0013 22:09:08
 * @version 1.0
 */

package net.xdclass.manager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.xdclass.manager.TrafficTaskManage;
import net.xdclass.mapper.TrafficTaskMapper;
import net.xdclass.model.TrafficTaskDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrafficTaskManageImpl implements TrafficTaskManage {
    @Autowired
    private TrafficTaskMapper trafficTaskMapper;
    @Override
    public int add(TrafficTaskDO trafficTaskDO) {
        int rows = trafficTaskMapper.insert(trafficTaskDO);
        return rows;
    }

    @Override
    public TrafficTaskDO findByIdAndAccountNo(Long id, Long accountNo) {
        TrafficTaskDO trafficTaskDO = trafficTaskMapper.selectOne(new QueryWrapper<TrafficTaskDO>()
                .eq("id", id)
                .eq("account_no", accountNo)
        );
        return trafficTaskDO;
    }

    @Override
    public int deleteByIdAndAccount(Long id, Long accountNo) {
        int rows = trafficTaskMapper.delete(new QueryWrapper<TrafficTaskDO>()
                .eq("id", id)
                .eq("account_no", accountNo)
        );
        return rows;
    }
}


