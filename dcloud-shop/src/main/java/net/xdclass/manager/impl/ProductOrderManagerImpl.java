/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/3 0003 18:26:11
 * @version 1.0
 */

package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.mapper.ProductMapper;
import net.xdclass.mapper.ProductOrderMapper;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.vo.ProductOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class ProductOrderManagerImpl implements ProductOrderManager {
    @Autowired
    private ProductOrderMapper productOrderMapper;
    @Override
    public int add(ProductOrderDO productOrderDO) {
        int rows = productOrderMapper.insert(productOrderDO);
        return rows;
    }

    @Override
    public ProductOrderDO findOutTradeNoAndAccount(String outTradeNo, Long accountNo) {
        ProductOrderDO productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>()
                .eq("out_trade_no", outTradeNo)
                .eq("account_no", accountNo)
        );
        return productOrderDO;
    }

    @Override
    public int updateOrderPayState(String outTradeNo, Long accountNo, String newStatus, String oldStatus) {
        int rows = productOrderMapper.update(null, new UpdateWrapper<ProductOrderDO>()
                .eq("out_trade_no", outTradeNo)
                .eq("account_no", accountNo)
                .eq("state", oldStatus)
                .set("state", newStatus)
        );
        return rows;
    }

    @Override
    public Map<String, Object> selectPage(int page, int size, Long accountNo, String status) {
        Page<ProductOrderDO> productOrderDOPage = new Page<>(page, size);
        IPage<ProductOrderDO>orderDoInPage;
        if (StringUtils.isEmpty(status)){
            orderDoInPage=productOrderMapper.selectPage(productOrderDOPage,new QueryWrapper<ProductOrderDO>()
                    .eq("account_no", accountNo));
        }else {
            orderDoInPage=productOrderMapper.selectPage(productOrderDOPage,new QueryWrapper<ProductOrderDO>()
                    .eq("account_no", accountNo)
                    .eq("state", status)
            );
        }
        //将Do转Vo;
       // HashMap<String, Object> map = new HashMap<>(3);
       // map.put("current_data",productOrderDOPage.getRecords());
        List<ProductOrderVO> productOrderVOList = productOrderDOPage.getRecords().stream().map(obj -> {
            ProductOrderVO productOrderVO = new ProductOrderVO();
            BeanUtils.copyProperties(obj, productOrderVO);
            return productOrderVO;
        }).collect(Collectors.toList());
         HashMap<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("totle_record",orderDoInPage.getTotal());
        hashMap.put("totle_page",orderDoInPage.getPages());
        hashMap.put("current_data",productOrderVOList);
        return hashMap;
    }

    @Override
    public int del(long productOrderId, long accountNo) {
        int rows = productOrderMapper.update(null, new UpdateWrapper<ProductOrderDO>()
                .eq("id", productOrderId)
                .eq("account_no", accountNo)
                .set("del", 1)
        );
        return rows;
    }
}


