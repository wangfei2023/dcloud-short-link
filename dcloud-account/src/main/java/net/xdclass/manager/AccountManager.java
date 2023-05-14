package net.xdclass.manager;

import net.xdclass.model.AccountDO;

import java.util.List;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/2 0002 0:11
 * @Version: 1.0
 * @Description:
 */

public interface AccountManager {
   int insert(AccountDO accountDO);

   List<AccountDO> findByPhone(String phone);
}