package net.xdclass.manager.Impl;

import net.xdclass.manager.AccountManager;
import net.xdclass.mapper.AccountMapper;
import net.xdclass.model.AccountDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/2 0002 0:12
 * @Version: 1.0
 * @Description:
 */
@Service
public class AccountManagerImpl implements AccountManager {
   @Autowired
    private AccountMapper accountMapper;
    @Override
    public int insert(AccountDO accountDO) {
        int rows = accountMapper.insert(accountDO);
        return rows;
    }
}