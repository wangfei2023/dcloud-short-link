package net.xdclass.manage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.xdclass.model.ShortLinkDO;

public interface ShortLinkManager{
/**
 * 新增域名
 *
 * @param shortLinkDO
 * @return
 */
int addShortLink(ShortLinkDO shortLinkDO);

/**
 * 根据短链码找内容
 *
 * @param shortLinkCode
 * @return
 */
ShortLinkDO findByShortLinkCode(String shortLinkCode);


/**
 * 根据短链码和accountNo删除
 *
 * @param shortLinkCode
 * @param accountNo
 * @return
 */
int del(String shortLinkCode, Long accountNo);
    /**
     * 更新
     *
     * @param shortLinkDO
     * @param
     * @return
     */
    int update(ShortLinkDO shortLinkDO);
}
