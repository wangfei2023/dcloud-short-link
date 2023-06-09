package net.xdclass.manage.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.mapper.ShortLinkMapper;
import net.xdclass.model.ShortLinkDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/28 0028 20:41]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/28 0028 20:41]
 * @updateRemark : [说明本次修改内容]
 */
@Component
public class ShortLinkManagerImpl implements ShortLinkManager {
    @Autowired
    private ShortLinkMapper shortLinkMapper;
    @Override
    public int addShortLink(ShortLinkDO shortLinkDO) {
        return shortLinkMapper.insert(shortLinkDO);
    }

    @Override
    public ShortLinkDO findByShortLinkCode(String shortLinkCode) {
        ShortLinkDO shortLinkDO = shortLinkMapper.selectOne(new QueryWrapper<ShortLinkDO>().eq("code", shortLinkCode));
        return shortLinkDO;
    }

    @Override
    public int del(ShortLinkDO shortLinkDO) {

        return shortLinkMapper.update(null,new UpdateWrapper<ShortLinkDO>()
                .eq("code",shortLinkDO.getCode())
                .eq("account_no",shortLinkDO.getAccountNo())
                .set("del",1)
        );
    }

    @Override
    public int update(ShortLinkDO shortLinkDO) {
       int rows= shortLinkMapper.update(null,new UpdateWrapper<ShortLinkDO>()
        .eq("code",shortLinkDO.getCode())
                //如果短链没有被删除,才进行更新;
        .eq("del",0)
        .eq("account_no",shortLinkDO.getAccountNo())
        //更新的内容;
        .set("title",shortLinkDO.getTitle())
        .set("domain",shortLinkDO.getDomain())
        );
        return rows;
    }
}
