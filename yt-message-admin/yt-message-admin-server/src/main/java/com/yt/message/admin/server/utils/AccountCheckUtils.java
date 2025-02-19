package com.yt.message.admin.server.utils;

import cn.hutool.core.lang.Validator;
import com.yt.message.admin.server.constant.AccountType;

import java.util.Collection;

/**
 * @ClassName AccountCheckUtils
 * @Author Ts
 * @Version 1.0
 */

public class AccountCheckUtils {

    public static boolean checkAccounts(AccountType accountType, Collection<String> accounts) {
        switch (accountType) {
            case EMAIL:
                return accounts.stream().allMatch(Validator::isEmail);
            case PHONE:
                return accounts.stream().allMatch(Validator::isMobile);
        }
        return true;

    }

}
