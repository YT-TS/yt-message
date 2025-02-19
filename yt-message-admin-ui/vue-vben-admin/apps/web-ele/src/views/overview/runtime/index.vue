<script setup lang="ts">
import { ref } from 'vue'
import {page as errorPage} from '#/api/overview/runtime'
import {
  ElCard,
  ElDatePicker
} from 'element-plus';
import type { VxeGridProps } from '#/adapter/vxe-table';
import moment from 'moment';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
const date = ref(new Date())

const disabledDate = (date: any): boolean => {
  const currentTime = new Date();
  const sevenDaysAgo = moment(currentTime).subtract(7, 'days').toDate();
  return !(date <= currentTime && date >= sevenDaysAgo);
}
//表格选项
const gridOptions: VxeGridProps<any> = {
  pagerConfig: {
    
    layouts: ['PrevPage', 'Number', 'NextPage', 'FullJump'],
    background: true,
    align: 'left',
    pagerCount: 3,
    pageSize:5
  },
  columns: [
    {  type: 'expand',width:50 ,slots:{
      content:'full'
    } },
    { field: 'timestamp', title: '时间',
      formatter: (row: any) => {
        return moment(row.cellValue).format('YYYY-MM-DD HH:mm:ss')
      }
     },
    { field: 'message', title: 'message' },
  ],
  proxyConfig: {
    ajax: {
      query: async ({ page }) => {
        let res = await errorPage({
          pageNum: page.currentPage,
          pageSize: page.pageSize,
          date:moment(date.value).format('YYYY-MM-DD')
        })
        let resObject = JSON.parse(res);
        let dataArrays = resObject.datarows
        let dataInfo = resObject.schema
        const data: any[] = []
        dataArrays.forEach((element: any) => {
          let item: any = {}
          for (let index = 0; index < element.length; index++) {
            item[dataInfo[index].field] = element[index]
          }
          data.push(item)
        });
        return {
          pageNum: page.currentPage,
          pageSize: page.pageSize,
          totalCount: Number.MAX_VALUE,
          records: data
        };
      },
    },
  },
};

//生成表格组件
const [Grid,gridApi] = useVbenVxeGrid({
  gridOptions,
});
const search = () => {
  gridApi.reload()
}
</script>

<template>
  <div class="p-3">
    <el-card >
      <template #header>
        <span class="font-bold">Error日志</span>
      </template>
      <div class="p-2">
        <el-date-picker :disabled-date="disabledDate"  @change="search" v-model="date" type="date" placeholder="Pick a day" :clearable="false" style="width: 100%" />
      </div>
      
      <Grid>
        <template #full="{row}">
           <pre class="font-medium text-base text-wrap break-words">{{row.full_message}}</pre>
        </template>
      </Grid>
    </el-card>

  </div>
</template>
