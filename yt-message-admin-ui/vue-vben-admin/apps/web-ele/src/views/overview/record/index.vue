<template>
  <div>
    <div>
      <el-alert title="最多显示15天内的消息记录" type="warning" :closable="false" />
    </div>
    <Grid>
      <template #action="{ row }">
        <el-link type="primary" @click="track(row)">消息轨迹</el-link>
      </template>
      <template #result="{ row }">
        <el-tag :type="row.result ? 'success' : 'danger'">{{ row.result ? '成功' : '失败' }}</el-tag>
      </template>
    </Grid>
    <TrackModal title="消息轨迹">
      <!-- <el-steps  :active="activeTrack()" align-center :process-status="activeStatus()" > -->
      <el-steps align-center>
        <el-step title="平台下发" :description="trackTime(0)" :status="trackStatusActive(0)" />
        <el-step title="消息发送" :description="trackTime(1)" :status="trackStatusActive(1)" />
      </el-steps>
    </TrackModal>
  </div>
</template>
<script setup lang="ts">

import type { VxeGridProps } from '#/adapter/vxe-table';
import moment from 'moment';
import type { VbenFormProps } from '#/adapter/form';
import { useVbenVxeGrid } from '#/adapter/vxe-table';
import { recordApi, page as recordPage, track as trackApi } from '#/api/overview/record'
import { useVbenModal } from '@vben/common-ui';
import {
  ElLink,
  ElAlert,
  ElSteps,
  ElStep,
  ElTag,
} from 'element-plus';
import { ref } from 'vue';

//表格选项
const gridOptions: VxeGridProps<recordApi.Record> = {
  pagerConfig: {
   
    layouts: [ 'PrevPage', 'Number', 'NextPage', 'FullJump'],
    background: true,
    align: 'left',
    pagerCount: 3,
    pageSize:10
  },
  columns: [
    { title: '序号', type: 'seq' },
    { field: 'receive_account', title: '接收账户' },
    { field: 'biz_id', title: '消息id' },
    {
      field: 'hand_timestamp', title: '发送时间',
      formatter({ cellValue }) {
        return moment(cellValue).format('YYYY-MM-DD HH:mm:ss');
      }
    },
    {
      field: 'step',
      slots: { default: 'result' },
      title: '发送结果',
    },
    {
      field: 'action',
      slots: { default: 'action' },
      fixed: 'right',

      title: '操作',
    },
  ],
  proxyConfig: {
    ajax: {
      query: async ({ page }, formValues) => {

        let params: any = {}
        if (formValues.startTime) {
          params['startTimeStamp'] = formValues.startTime.getTime()

        }
        if (formValues.endTime) {
          params['endTimeStamp'] = formValues.endTime.getTime()
        }
        if (formValues.receiveAccount) {
          params['receiveAccount'] = formValues.receiveAccount
        }
        let res = await recordPage({
          pageNum: page.currentPage,
          pageSize: page.pageSize,
          // ...formValues
          ...params
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
      
        for (const element of data) {
          if (element.step == -1) {
            element['result'] = false
          } else {
            let track = await trackApi(element.biz_id);
            let resObject = JSON.parse(track)
            element['result'] = resObject.datarows[1][2] > 0 ? true : false
          }
        }
        
     
        return {
          pageNum: 1,
          pageSize: 10,
          // pageCount: 10000,
          totalCount: Number.MAX_VALUE,
          records: data
        };
      },
    },
  },
};
//表格上搜索表单选项
const formOptions: VbenFormProps = {
  schema: [
    {
      component: 'DatePicker',
      defaultValue: undefined,
      fieldName: 'startTime',
      label: '开始时间: ',
      componentProps: {
        type: "datetime",
        placeholder: "开始日期",
        'disabled-date': (date: any): boolean => {
          const currentTime = new Date();
          const fifteenDaysAgo = moment(currentTime).subtract(15, 'days').toDate();
          return !(date <= currentTime && date >= fifteenDaysAgo);
        },

      },
    },
    {
      component: 'DatePicker',
      defaultValue: undefined,
      fieldName: 'endTime',
      label: '结束时间: ',
      componentProps: {
        type: "datetime",
        placeholder: "开始日期",
        'disabled-date': (date: any): boolean => {
          const currentTime = new Date();
          const fifteenDaysAgo = moment(currentTime).subtract(15, 'days').toDate();
          return !(date <= currentTime && date >= fifteenDaysAgo);
        },

      },
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '接收账户',
      },
      defaultValue: undefined,
      label: '接收账户：',
      fieldName: 'receiveAccount',

    },

  ],
  // 控制表单是否显示折叠按钮
  showCollapseButton: false,
  submitButtonOptions: {
    content: '查询',
  },
};
//生成表格组件
const [Grid] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});
//消息轨迹模态框
const [TrackModal, trackModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
  class: 'w-1/2',
  onClosed: () => {
    trackStatus.value = {}
  }
});
const trackStatus = ref<recordApi.TrackStatus>({})
const track = (row: any) => {

  trackApi(row.biz_id).then(res => {

    let resObject = JSON.parse(res)
    if (resObject.datarows.length == 1) {
      trackStatus.value.issue = resObject.datarows[0][2] > 0 ? true : false;
      trackStatus.value.issueTimestamp = resObject.datarows[0][1];
    } else if (resObject.datarows.length == 2) {
      resObject.datarows.forEach((element: any) => {
        if (element[2] == 1) {
          trackStatus.value.issue = true;
          trackStatus.value.issueTimestamp = element[1];
        } else if (element[2] == -1) {
          trackStatus.value.issue = false;
          trackStatus.value.issueTimestamp = element[1];
        } else if (element[2] == 2) {
          trackStatus.value.send = true;
          trackStatus.value.sendTimestamp = element[1];
        } else if (element[2] == -2) {
          trackStatus.value.send = false;
          trackStatus.value.sendTimestamp = element[1];
        }
      });
    }

    trackModalApi.open()
  })
}

const trackStatusActive = (active: number) => {
  if (active == 0) {

    return trackStatus.value.issue == undefined ? 'wait' : trackStatus.value.issue ? 'success' : 'error'
  } else {
    return trackStatus.value.send == undefined ? 'wait' : trackStatus.value.send ? 'success' : 'error'
  }

}
const trackTime = (active: number) => {

  if (active == 0) {

    return trackStatus.value.issueTimestamp ? moment(trackStatus.value.issueTimestamp).format('YYYY-MM-DD HH:mm:ss') : ''
  } else {
    return trackStatus.value.sendTimestamp ? moment(trackStatus.value.sendTimestamp).format('YYYY-MM-DD HH:mm:ss') : ''
  }

}
// const activeTrack = (): number => {

//   if (trackStatus.value.send == undefined) {

//     return 0;
//   } else {
//     return 1
//   }
// }
// const activeStatus = (): 'success' | 'error' | 'wait' | 'process' => {
//   if (activeTrack() == 0) {
//     return trackStatus.value.issue ? 'success' : 'error'
//   } else {

//     if (trackStatus.value.send == undefined) {
//       return 'wait'
//     } else {

//       return trackStatus.value.send ? 'success' : 'error'

//     }
//   }
// }
</script>
