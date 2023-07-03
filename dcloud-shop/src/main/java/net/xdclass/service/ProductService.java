/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/6/27 0027 20:54:54
 * @version 1.0
 */

package net.xdclass.service;

import net.xdclass.vo.ProductVo;

import java.util.List;

public interface ProductService {

    List<ProductVo> list();

    ProductVo findDetailById(long productId);
}


