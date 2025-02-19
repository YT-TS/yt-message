import type { RouteRecordRaw } from 'vue-router';

import { BasicLayout } from '#/layouts';
import { $t } from '#/locales';

const routes: RouteRecordRaw[] = [
  {
    component: BasicLayout,
    meta: {
      icon: 'lucide:layout-dashboard',
      order: -1,
      title: $t('page.dashboard.title'),
    },
    name: 'Overview',
    path: '/',
    children: [
      {
        name: 'Statistics',
        path: '/statistics',
        component: () => import('#/views/overview/statistics/index.vue'),
        meta: {
          affixTab: true,
          icon: 'lucide:area-chart',
          title: '业务'
        },
      },
      {
        name: 'Runtime',
        path: '/runtime',
        component: () => import('#/views/overview/runtime/index.vue'),
        meta: {
          affixTab: true,
          icon: 'lucide:area-chart',
          title: '系统'
        },
      },
      {
        name: 'Record',
        path: '/record',
        component: () => import('#/views/overview/record/index.vue'),
        meta: {
          affixTab: true,
          icon: 'lucide:area-chart',
          title: '消息记录'
        },
      },

    ],
  },
];

export default routes;
