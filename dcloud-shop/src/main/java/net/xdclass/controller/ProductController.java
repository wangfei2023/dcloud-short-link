/**
 * @project dcloud-short-link
 * @description商品列表和详情;
 * @author Administrator
 * @date 2023/6/27 0027 19:50:52
 * @version 1.0
 */

package net.xdclass.controller;

import net.xdclass.service.ProductService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/v1")
public class ProductController {
    @Autowired
    private ProductService productService;
    /**
     * @description TODO
     * 查看列表详情
     * @return
     * @author
     * @date
     */
    @GetMapping
    public JsonData list(){
       List<ProductVo>productVoList = productService.list();
       return JsonData.buildSuccess(productVoList);
    }

    @GetMapping("detail/{product_id}")
    public JsonData detail(@PathVariable(value = "product_id") long productId){
        ProductVo productVo=productService.findDetailById(productId);
        return JsonData.buildSuccess(productVo);
    }

}


