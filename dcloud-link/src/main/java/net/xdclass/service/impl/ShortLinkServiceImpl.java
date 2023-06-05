package net.xdclass.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.model.ShortLinkDO;
import net.xdclass.service.ShortLinkService;
import net.xdclass.vo.ShortLinkVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/28 0028 22:45]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/28 0028 22:45]
 * @updateRemark : [说明本次修改内容]
 */
@Service
@Slf4j
public class ShortLinkServiceImpl implements ShortLinkService {
    @Autowired
    private ShortLinkManager shortLinkManager;
    @Override
    public ShortLinkVo parseLinkCode(String linkCode) {
        ShortLinkDO shortLinkDO = shortLinkManager.findByShortLinkCode(linkCode);
        if (StringUtils.isEmpty(JSON.toJSONString(shortLinkDO))){
            return null;
        }
        ShortLinkVo shortLinkVo = new ShortLinkVo();
        BeanUtils.copyProperties(shortLinkDO,shortLinkVo);
        return shortLinkVo;
    }
}
