<template>
  <div>
    <Grid>
      <template #action="{ row }">
        <div class=" flex  justify-evenly justify-around">
          <el-link type="primary" @click="edit(row)">编辑</el-link>
          <el-popconfirm title="确定要删除吗?" @confirm="handleDelete(row)">
            <template #reference>
              <el-link type="danger">删除</el-link>
            </template>
          </el-popconfirm>
        </div>
      </template>
      <template #toolbar-tools>
        <el-button type="primary" @click="add">新增</el-button>
      </template>
    </Grid>
    <Modal title="编辑">
      <Form />
    </Modal>
    <AddModal title="新增">
      <AddForm />
    </AddModal>
  </div>
</template>
<script lang="ts" setup>

import type { VxeGridProps } from '#/adapter/vxe-table';
import type { VbenFormProps } from '#/adapter/form';
import { useVbenVxeGrid } from '#/adapter/vxe-table';
import { page as platformPage, update, add as create, remove } from '#/api/system/platform';
import { platformApi } from '#/api/system/platform'
import { dic, type Dic } from '#/api/dic'
import {
  ElLink,
  ElButton,
  ElMessage,
  ElPopconfirm
} from 'element-plus';
import { ref, computed } from 'vue';
import { useVbenModal } from '@vben/common-ui';
import { useVbenForm } from '#/adapter/form';

//获取消息类型字典
const messageTypeDic = ref<Dic[]>([]);
dic('messageType').then(res => messageTypeDic.value = res);

//编辑模态框
const [Modal, modalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
});
//添加模态框
const [AddModal, addModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
});

//表格选项
const gridOptions: VxeGridProps<platformApi.PlatformRsp> = {
  columns: [
    { title: '序号', type: 'seq' },
    { field: 'platformName', title: '平台名' },
    {
      field: 'messageType',
      title: '消息类型',
      formatter({ cellValue }) {

        const item = messageTypeDic.value.find(item => item.value === cellValue)
        return item ? item.label : cellValue
      }
    },
    { field: 'createTime', title: '创建时间' },
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
        return await platformPage({
          pageNum: page.currentPage,
          pageSize: page.pageSize,
          ...formValues
        });
      },
    },
  },
};
//表格上搜索表单选项
const formOptions: VbenFormProps = {
  schema: [
    {
      component: 'Select',
      componentProps: {
        clearable: true,
        options: computed(() => messageTypeDic.value),
        placeholder: '请选择',
      },
      fieldName: 'messageType',
      label: '消息类型',
    }
  ],
  // 控制表单是否显示折叠按钮
  showCollapseButton: false,
  submitButtonOptions: {
    content: '查询',
  },
};
//生成表格组件
const [Grid, gridApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});
//编辑表单
const [Form, formApi] = useVbenForm({
  // 提交函数
  handleSubmit: handleEdit,
  commonConfig: {
    colon: true,
    labelWidth: 200,
  },
  actionWrapperClass: 'text-center',
  actionButtonsReverse: true,
  resetButtonOptions: {
    show: false
  },
  layout: 'horizontal',
  schema: [
    {
      component: 'Input',
      // 对应组件的参数
      componentProps: {
        placeholder: '平台名',
      },
      // 字段名
      fieldName: 'platformName',
      // 界面显示的label
      label: '平台名',
    },
    {
      component: 'Select',
      componentProps: {
        options: computed(() => messageTypeDic.value),
        placeholder: '消息类型',
      },
      fieldName: 'messageType',
      disabled: true,
      label: '消息类型',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '服务器地址或webhook 地址',
      },
      fieldName: 'host',
      label: '服务器地址或webhook地址',
      dependencies: {
        if(values) {
          return values.messageType == 1 || values.messageType == 2 || values.messageType == 3;
        },
        triggerFields: ['messageType'],
      },
    },
    {
      component: 'InputNumber',
      componentProps: {
        placeholder: '请求端口',
        min: 0,
        step: 1,
        'step-strictly': true,
      },
      fieldName: 'port',
      label: '请求端口',
      dependencies: {
        if(values) {
          return values.messageType == 1 || values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: 'appId',
      },
      fieldName: 'appId',
      label: '应用id',
      dependencies: {
        if(values) {
          return values.messageType == 1 || values.messageType == 5;
        },
        triggerFields: ['messageType'],
      },
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: 'secretId',
      },
      fieldName: 'secretId',
      label: '访问密钥id',
      dependencies: {
        if(values) {
          return values.messageType == 1;
        },
        triggerFields: ['messageType'],
      },
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: 'secretKey',
      },
      fieldName: 'secretKey',
      label: '访问密钥值或签名密钥',
      dependencies: {
        if(values) {
          return values.messageType == 1 || values.messageType == 3 || values.messageType == 5;
        },
        triggerFields: ['messageType'],
      },
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '签名原文',
      },
      fieldName: 'signName',
      label: '签名原文',
      dependencies: {
        if(values) {
          return values.messageType == 1;
        },
        triggerFields: ['messageType'],
      },
    },

    {
      component: 'Input',
      componentProps: {
        placeholder: '处理器(handler)名',
      },
      fieldName: 'handlerName',
      label: '处理器(handler)名',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '限流key',
      },
      fieldName: 'rateLimitKey',
      label: '限流key',
    },
    {
      component: 'InputNumber',
      componentProps: {

        placeholder: '限流大小',
        min: 0,
        step: 1,
        'step-strictly': true,
      },
      fieldName: 'rateLimitScale',
      help: '当限流key不为空时,必填',
      label: '限流大小',

    },
    {
      component: 'InputNumber',
      componentProps: {
        placeholder: '限流间隔',
        min: 0,
        step: 1,
        'step-strictly': true,
      },
      fieldName: 'rateLimitInterval',
      label: '限流间隔',
      help: '当限流key不为空时,必填',
      suffix: () => '单位：秒',
    },

  ]
});
//添加表单
const [AddForm] = useVbenForm({
  // 提交函数
  handleSubmit: handleAdd,
  commonConfig: {
    colon: true,
    labelWidth: 200,
  },
  actionWrapperClass: 'text-center',
  actionButtonsReverse: true,
  resetButtonOptions: {
    show: false
  },
  layout: 'horizontal',
  schema: [
    {
      component: 'Select',
      componentProps: {
        options: computed(() => messageTypeDic.value),
        placeholder: '消息类型',
      },
      fieldName: 'messageType',
      label: '消息类型',
      rules: 'required',
    },
    {
      component: 'Input',
      // 对应组件的参数
      componentProps: {
        placeholder: '平台名',
      },
      // 字段名
      fieldName: 'platformName',
      // 界面显示的label
      label: '平台名',
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '服务器地址或webhook 地址',
      },
      fieldName: 'host',
      label: '服务器地址或webhook 地址',
      rules: 'required',
      dependencies: {
        if(values) {
          return values.messageType == 1 || values.messageType == 2 || values.messageType == 3;
        },
        triggerFields: ['messageType'],
      },
    },
    {
      component: 'InputNumber',
      componentProps: {
        placeholder: '请求端口',
        min: 0,
        step: 1,
        'step-strictly': true,
      },
      fieldName: 'port',
      label: '请求端口',
      dependencies: {
        if(values) {
          return values.messageType == 1 || values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: 'appId',
      },
      fieldName: 'appId',
      label: '应用id',
      dependencies: {
        if(values) {
          return values.messageType == 1 || values.messageType == 5;
        },
        triggerFields: ['messageType'],
      },
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: 'secretId',
      },
      fieldName: 'secretId',
      label: '访问密钥id',
      dependencies: {
        if(values) {
          return values.messageType == 1;
        },
        triggerFields: ['messageType'],
      },
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: 'secretKey',
      },
      fieldName: 'secretKey',
      label: '访问密钥值或签名密钥',
      dependencies: {
        if(values) {
          return values.messageType == 1 || values.messageType == 3 || values.messageType == 5;
        },
        triggerFields: ['messageType'],
      },
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '签名原文',
      },
      fieldName: 'signName',
      label: '签名原文',
      dependencies: {
        if(values) {
          return values.messageType == 1;
        },
        triggerFields: ['messageType'],
      },
    },

    {
      component: 'Input',
      componentProps: {
        placeholder: '处理器(handler)名',
      },
      fieldName: 'handlerName',
      label: '处理器(handler)名',
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '限流key',
      },
      fieldName: 'rateLimitKey',
      label: '限流key',
    },
    {
      component: 'InputNumber',
      componentProps: {

        placeholder: '限流大小',
        min: 0,
        step: 1,
        'step-strictly': true,
      },
      fieldName: 'rateLimitScale',
      label: '限流大小',
      help: '当限流key不为空时,必填',

    },
    {
      component: 'InputNumber',
      componentProps: {
        placeholder: '限流间隔',
        min: 0,
        step: 1,
        'step-strictly': true,
      },
      fieldName: 'rateLimitInterval',
      label: '限流间隔',
      suffix: () => '单位：秒',
      help: '当限流key不为空时,必填',
    },

  ]
});
//打开编辑框
function edit(row: platformApi.PlatformRsp) {
  // modalApi.setData({
  //   content: row
  // });
  formApi.setValues(row, false)
  modalApi.open()
}
//提交编辑
async function handleEdit(row: any) {
  update(row).then(() => {
    ElMessage.success('修改成功');
    modalApi.close()
    gridApi.query()
  }).catch(() => { })
}
//打开添加框
function add() {
  addModalApi.open()
}
//提交添加
async function handleAdd(row: any) {
  create(row).then(() => {
    ElMessage.success('添加成功');
    addModalApi.close()
    gridApi.query()
  }).catch(() => { })
}
//删除
async function handleDelete(row: any) {
  remove(row.platformId).then(() => {
    ElMessage.success('删除成功');
    gridApi.query()
  }).catch(() => { })

}





</script>
