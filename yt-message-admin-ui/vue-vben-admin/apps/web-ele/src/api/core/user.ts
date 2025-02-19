import type { UserInfo } from '@vben/types';

import { requestClient } from '#/api/request';

/**
 * 获取用户信息
 */
export async function getUserInfoApi() {
  // return requestClient.get<UserInfo>('/user/info');
  return {
    "code": 0,
    "data": {
      "id": 0,
      "realName": "Vben",
      "roles": [
        "super"
      ],
      "username": "vben"
    },
    "error": null,
    "message": "ok"
  }
}
