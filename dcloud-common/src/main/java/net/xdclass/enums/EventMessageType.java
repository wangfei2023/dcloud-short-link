package net.xdclass.enums;
//消息类型;
public enum EventMessageType {
   /**
    * @description 短链创建
    *
    * @return
    * @author
    * @date
    */
    SHORT_LINK_ADD,


    /**
     * @description 短链创建C端
     *
     * @return
     * @author
     * @date
     */
    SHORT_LINK_ADD_LINK,

    /**
     * @description 短链创建B端
     *
     * @return
     * @author
     * @date
     */
    SHORT_LINK_ADD_MAPPING,

 /**
  * @description 短链删除
  *
  * @return
  * @author
  * @date
  */
 SHORT_LINK_DEL,


 /**
  * @description 短链删除C端
  *
  * @return
  * @author
  * @date
  */
 SHORT_LINK_DEL_LINK,

 /**
  * @description 短链删除B端
  *
  * @return
  * @author
  * @date
  */
 SHORT_LINK_DEL_MAPPING,

 /**
  * @description 短链更新
  *
  * @return
  * @author
  * @date
  */
 SHORT_LINK_UPDATE,


 /**
  * @description 短链更新C端
  *
  * @return
  * @author
  * @date
  */
 SHORT_LINK_UPDATE_LINK,

 /**
  * @description 短链更新B端
  *
  * @return
  * @author
  * @date
  */
 SHORT_LINK_UPDATE_MAPPING,


/**
 * @description 新建商品订单
 *
 * @return
 * @author
 * @date
 */
 PRODUCT_ORDER_NEW;
}
