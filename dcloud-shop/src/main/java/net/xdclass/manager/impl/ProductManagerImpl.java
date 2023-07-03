/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/6/27 0027 20:57:56
 * @version 1.0
 */

package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.ProductManager;
import net.xdclass.mapper.ProductMapper;
import net.xdclass.model.ProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductManagerImpl implements ProductManager {
  @Autowired 
  private ProductMapper productMapper;
    @Override
    public List<ProductDO> list() {
        //无条件查询时就是用null;
        List<ProductDO> productDOList = productMapper.selectList(null);
        return productDOList;
    }

    @Override
    public ProductDO findDetailById(long productId) {
        ProductDO productDO = productMapper.selectOne(new QueryWrapper<ProductDO>().eq("id",productId));
        return productDO;
    }
}


