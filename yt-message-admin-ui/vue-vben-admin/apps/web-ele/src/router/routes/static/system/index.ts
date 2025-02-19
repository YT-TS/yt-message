import type { RouteRecordRaw } from 'vue-router';

import { BasicLayout } from '#/layouts';

const routes: RouteRecordRaw[] = [
  {
    component: BasicLayout,
    meta: {
      icon: 'icon-park-outline:system',
      title: '系统管理',
    },
    name: 'System',
    path: '/system',
    redirect: '/system/platform',
    children: [
      {
        name: 'Platform',
        path: '/system/platform',
        component: () => import('#/views/system/platform/index.vue'),
        meta: {
          icon: 'game-icons:platform',
          title: '消息平台',
        },
      },
      {
        name: 'Template',
        path: '/system/template',
        component: () => import('#/views/system/template/index.vue'),
        meta: {
          icon: 'fluent:mail-template-16-regular',
          title: '消息模板',
        },
      },
      {
        name: 'Account',
        path: '/system/account',
        component: () => import('#/views/system/account/index.vue'),
        meta: {
          icon: 'material-symbols:person-book-outline-sharp',
          title: '账户组',
        },
      },
      {
        name: 'Prepared',
        path: '/system/prepared',
        component: () => import('#/views/system/prepared/index.vue'),
        meta: {
          icon: 'mingcute:send-plane-fill',
          title: '预发送模板',
        },
      },
    ],
  },
];

export default routes;
