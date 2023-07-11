/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/6/27 0027 20:56:31
 * @version 1.0
 */

package net.xdclass.service.impl;

import net.xdclass.manager.ProductManager;
import net.xdclass.model.ProductDO;
import net.xdclass.service.ProductService;
import net.xdclass.vo.ProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductManager productManager;
    @Override
    public List<ProductVo> list() {
        List<ProductDO>productDOList=productManager.list();
        List<ProductVo> productVoList = productDOList.stream().map(obj -> BeanProcess(obj)).collect(Collectors.toList());
        return productVoList;
    }

    @Override
    public ProductVo findDetailById(long productId) {
       ProductDO productDO = productManager.findDetailById(productId);
//        ProductVo productVo = new ProductVo();
//        BeanUtils.copyProperties(productDO,productVo);
        ProductVo productVo = BeanProcess(productDO);
        return productVo;
    }

    public ProductVo BeanProcess(ProductDO productDo){
        ProductVo productVo = new ProductVo();
        BeanUtils.copyProperties(productDo,productVo);
        return productVo;
    }

}


