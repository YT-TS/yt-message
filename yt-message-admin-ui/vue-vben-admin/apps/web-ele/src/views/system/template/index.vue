<template>
  <div>
    <Grid>
      <template #action="{ row }">
        <div class=" flex  justify-evenly justify-around">
          <el-link type="info" @click="send(row)">便捷发送</el-link>
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
    <EditModal title="编辑">
      <EditForm />
    </EditModal>
    <AddModal title="新增">
      <AddForm />
    </AddModal>
    <SendModal title="发送消息">
      <SendForm />
    </SendModal>
  </div>
</template>
<script setup lang="ts">
import { dic, type Dic, type OneLayerTreeDic, treeDic } from '#/api/dic'
import { computed, ref, reactive, h } from 'vue';
import { useVbenVxeGrid, type VxeGridProps } from '#/adapter/vxe-table';
import type { templateApi } from '#/api/system/template';
import { page as templatePage, update, create, remove, sendMessage } from '#/api/system/template';
import { useVbenForm, type VbenFormProps } from '#/adapter/form';
import Codemirror from "codemirror-editor-vue3";
import {
  ElLink,
  ElButton,
  ElMessage,
  ElPopconfirm,
} from 'element-plus';
import { useVbenModal } from '@vben/common-ui';
import AccountGroupSelect from '#/components/account-group-select.vue';
//TODO 分页展示更多信息   

//获取消息类型字典
const messageUsageDic = ref<Dic[]>([]);
dic('messageUsage').then(res => messageUsageDic.value = res);
//获取消息类型-平台字典
let plateformDic = reactive<OneLayerTreeDic<number, string>[]>([]);
treeDic('platform').then(res => plateformDic = res);
const yesValue = ref();
const noValue = ref();
dic('yesOrNo').then(res => {
  yesValue.value = res.find(item => item.label === 'YES')?.value;
  noValue.value = res.find(item => item.label === 'NO')?.value;
})
//获取平台组字典
const platformDic = ref<Dic[]>([]);
dic('platform').then(res => platformDic.value = res);
//获取账户组字典
const accountGroupDic = ref<Dic[]>([]);
dic('accountGroup').then(res => accountGroupDic.value = res);
//获取限流类型字典
const rateLimitStrategy = ref<Dic[]>([]);
dic('rateLimitStrategy').then(res => rateLimitStrategy.value = res);


//表格选项
const gridOptions: VxeGridProps<templateApi.TemplateRsp> = {
  columns: [
    { title: '序号', type: 'seq' },
    { field: 'templateName', title: '模板名' },
    {
      field: 'messageUsage',
      title: '消息用途',
      formatter({ cellValue }) {

        const item = messageUsageDic.value.find(item => item.value === cellValue)
        return item ? item.label : cellValue
      }
    }, 
    {
      field: 'platformId', title: '消息平台',
      formatter: (params) => {
        return findPlatformName(plateformDic,params.cellValue)
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
        return await templatePage({
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
        options: computed(() => messageUsageDic.value),
        placeholder: '请选择',
      },
      fieldName: 'messageUsage',
      label: '消息用途',
    },
    {
      component: 'Select',
      componentProps: {
        clearable: true,
        options: computed(() => platformDic.value),
        placeholder: '请选择',
      },
      fieldName: 'platformId',
      label: '消息平台',
    }
  ],
  // 控制表单是否显示折叠按钮
  showCollapseButton: false,
  submitButtonOptions: {
    content: '查询',
  },
};
//获取表格组件
const [Grid, gridApi] = useVbenVxeGrid({
  formOptions,
  gridOptions
});
//编辑模态框
const [EditModal, editModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
  // class: 'w-fit'
});
//添加模态框
const [AddModal, addModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
  // class: 'w-fit'
});
//发送消息模态框
const [SendModal, sendModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
  // class: 'w-fit'
});
//发送消息表单
const [SendForm, sendFormApi] = useVbenForm({
  // 提交函数
  handleSubmit: handleSend,
  commonConfig: {
    labelWidth: 200,
    colon: true,
    componentProps: {

      class: 'w-full',
    },
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
        placeholder: '模板名',
      },
      disabled: true,
      // 字段名
      fieldName: 'templateName',
      // 界面显示的label
      label: '模板名',
    },
    {
      component: 'InputTag',
      // 字段名
      fieldName: 'subjectParams',
      // 界面显示的label
      label: '标题参数',
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      help: '回车输入下一个'
    },

    {
      component: 'InputTag',
      // 字段名
      fieldName: 'contentParams',
      // 界面显示的label
      label: '内容参数',
      help: '回车输入下一个'
    },
    {
      component: 'Input',
      // 对应组件的参数
      // 字段名
      fieldName: 'receiveAccounts',
      componentProps: {
        placeholder: '多个账户间以英文逗号分隔',
        type: 'textarea',
      },
      dependencies: {
        if(values) {
          return values.messageType != 3;
        },
        triggerFields: ['messageType'],
      },
      // 界面显示的label
      label: '接收账户(输入)',
      help: '多个账户间以英文逗号分隔'
    },
    {
      component: h(AccountGroupSelect),
      componentProps: {
        clearable: true,
        filterable: true,
        options: computed(() => accountGroupDic.value),
        placeholder: '请选择',
      },
      dependencies: {
        if(values) {
          return values.messageType != 3;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'accountGroupId',
      label: '接收账户(从账户组选择)',
    },
    {
      component: 'Switch',
      fieldName: 'requireAsync',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      label: '是否异步发送消息',
      rules: 'required',
    },
  ]
})
//编辑表单
const [EditForm, editFormApi] = useVbenForm({
  // 提交函数
  handleSubmit: handleEdit,
  commonConfig: {
    labelWidth: 150,
    colon: true,
    componentProps: {
      class: 'w-full',
    },
  },
  // handleValuesChange: (values) => {
  //   console.log(values);
  // },
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
        placeholder: '模板名',
      },
      // 字段名
      fieldName: 'templateName',
      // 界面显示的label
      label: '模板名',
      rules: 'required',
    },
    {
      component: 'Select',
      componentProps: {
        options: computed(() => messageUsageDic.value),
        placeholder: '消息用途',
      },
      fieldName: 'messageUsage',
      label: '消息用途',
      rules: 'required',
    },
    {
      component: 'ApiCascader',
      // 对应组件的参数
      componentProps: {
        // api: getPlateformDic,
        options: computed(() => plateformDic),
        placeholder: '推送平台',
        onChange: (value: any) => {
          editFormApi.setFieldValue('messageType', value[0])
        },

      },
      disabled: true,
      // 字段名
      fieldName: 'platformId',
      // 界面显示的label
      label: '推送平台',
      rules: 'required',
    },
    {
      component: 'Input',
      // 对应组件的参数
      componentProps: {
        placeholder: '三方平台模板id',
      },
      dependencies: {
        if(values) {
          return values.messageType == 1;
        },
        triggerFields: ['messageType'],
      },
      // 字段名
      fieldName: 'platformTemplateId',
      // 界面显示的label
      label: '三方平台模板id',
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '发送账户',
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'sendAccount',
      label: '发送账户',
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '用户名',
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'username',
      label: '用户名',
      help: '发送邮件需要认证时必填'
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '密码',
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'password',
      label: '密码',
      help: '发送邮件需要认证时必填'
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '标题',
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'subject',
      label: '标题',
      rules: 'required',
      help: '可用 \'{}\' 表示占位符'
    },
    {
      component: h(Codemirror, { border: true }),
      // component: h(Codemirror, { border: true }),
      dependencies: {
        if(values) {
          return values.messageType == 2 || values.messageType == 3;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'content',
      label: '内容',
      rules: 'required',
      help: '可用 \'{}\' 表示占位符',
      disabledOnChangeListener: false,
    },
    {
      component: 'Switch',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'requireSsl',
      label: '是否需要ssl发送',
      rules: 'required',
    },
    {
      component: 'Switch',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'requireHtml',
      label: '内容是否是html格式',
      rules: 'required',
    },
    {
      component: 'Switch',
      fieldName: 'requirePersist',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      label: '是否持久化消息',
      rules: 'required',
    },

    {
      component: 'Switch',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      fieldName: 'requireRateLimit',
      label: '是否开启限流',
      rules: 'required',
      help: '开启时请确认平台限流参数是否已设置'
    },
    {
      component: 'Select',
      componentProps: {
        options: computed(() => rateLimitStrategy.value),
        placeholder: '限流策略',
      },
      dependencies: {
        if(values) {
          return values.requireRateLimit == 1;
        },
        triggerFields: ['requireRateLimit'],
      },
      fieldName: 'rateLimitStrategy',
      label: '限流策略',
      help: '开启限流时,必填',
      defaultValue: 1,
    },
    {
      component: 'Switch',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      fieldName: 'requireRepeatConsume',
      label: '是否验证重复消费',
      rules: 'required',
    },
  ]
});
//添加表单
const [AddForm, addFormApi] = useVbenForm({
  // 提交函数
  handleSubmit: handleAdd,
  commonConfig: {
    labelWidth: 150,
    colon: true,
    componentProps: {
      class: 'w-full',
    },
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
        placeholder: '模板名',
      },
      // 字段名
      fieldName: 'templateName',
      // 界面显示的label
      label: '模板名',
      rules: 'required',
    },
    {
      component: 'Select',
      componentProps: {
        options: computed(() => messageUsageDic.value),
        placeholder: '消息用途',
      },
      fieldName: 'messageUsage',
      label: '消息用途',
      rules: 'required',
    },
    {
      component: 'ApiCascader',
      // 对应组件的参数
      componentProps: {
        // api: getPlateformDic,
        options: computed(() => plateformDic),
        placeholder: '推送平台',
        onChange: (value: any) => {
          addFormApi.setFieldValue('messageType', value[0])
        },
      },

      // 字段名
      fieldName: 'platformId',
      // 界面显示的label
      label: '推送平台',
      rules: 'required',
    },
    {
      component: 'Input',
      // 对应组件的参数
      componentProps: {
        placeholder: '三方平台模板id',
      },
      dependencies: {
        if(values) {
          return values.messageType == 1;
        },
        triggerFields: ['messageType'],
      },
      // 字段名
      fieldName: 'platformTemplateId',
      // 界面显示的label
      label: '三方平台模板id',
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '发送账户',
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'sendAccount',
      label: '发送账户',
      rules: 'required',
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '用户名',
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'username',
      label: '用户名',
      help: '发送邮件需要认证时必填'
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '密码',
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'password',
      label: '密码',
      help: '发送邮件需要认证时必填'
    },
    {
      component: 'Input',
      componentProps: {
        placeholder: '标题',
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'subject',
      label: '标题',
      rules: 'required',
      help: '可用 \'{}\' 表示占位符'
    },
    {
      component: h(Codemirror, { border: true }),
      dependencies: {
        if(values) {
          return values.messageType == 2 || values.messageType == 3;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'content',
      label: '内容',
      rules: 'required',

      help: '可用 \'{}\' 表示占位符',
      disabledOnChangeListener: false,


    },
    {
      component: 'Switch',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'requireSsl',
      label: '是否需要ssl发送',
      rules: 'required',
    },
    {
      component: 'Switch',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      dependencies: {
        if(values) {
          return values.messageType == 2;
        },
        triggerFields: ['messageType'],
      },
      fieldName: 'requireHtml',
      label: '内容是否是html格式',
      rules: 'required',
    },
    {
      component: 'Switch',
      fieldName: 'requirePersist',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      label: '是否持久化消息',
      rules: 'required',
    },
    {
      component: 'Switch',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      fieldName: 'requireRateLimit',
      label: '是否开启限流',
      rules: 'required',
      help: '开启时请确认平台限流参数是否已设置'
    },
    {
      component: 'Select',
      componentProps: {
        options: computed(() => rateLimitStrategy.value),
        placeholder: '限流策略',
      },
      dependencies: {
        if(values) {
          return values.requireRateLimit == 1;
        },
        triggerFields: ['requireRateLimit'],
      },
      fieldName: 'rateLimitStrategy',
      label: '限流策略',
      help: '开启限流时,必填',
      defaultValue: 1,
    },
    {
      component: 'Switch',
      componentProps: {
        'active-value': yesValue,
        'inactive-value': noValue,
      },
      fieldName: 'requireRepeatConsume',
      label: '是否验证重复消费',
      rules: 'required',
    },
  ]
});
function findType(plateformDic: OneLayerTreeDic<number, string>[], platformId: string) {
  for (const item of plateformDic) {
    if (item.children) {
      for (const e of item.children) {
        if (e.value === platformId) {
          return item.value
        }
      }
    }
  }
}
//打开编辑框
async function edit(row: templateApi.TemplateRsp) {
  row.messageType = findType(plateformDic, row.platformId)
  editFormApi.setValues(row, false)
  editFormApi.updateSchema([{
    fieldName: 'content',
    componentProps: {
      value: row.content
    },
  }])
  editModalApi.open()
}
//打开添加框
async function add() {
  // row.messageType = findType(plateformDic, row.platformId)
  addModalApi.open()
}
//提交编辑
async function handleEdit(row: any) {
  if(row.requireRateLimit == 1){
    if(!row.rateLimitStrategy){
      ElMessage.error('限流参数为空');
      return;
    }
  }else{
    row.rateLimitStrategy = null
  }
  // console.log(row)
  if (Array.isArray(row.platformId)) {
    row.platformId = row.platformId[1]
  }
  update(row).then(() => {
    ElMessage.success('修改成功');
    editModalApi.close()
    gridApi.query()
  }).catch(() => {
  })

}
//提交添加
async function handleAdd(row: any) {
  if(row.requireRateLimit == 1){
    if(!row.rateLimitStrategy){
      ElMessage.error('限流参数为空');
      return;
    }
  }else{
    row.rateLimitStrategy = null
  }
  if (Array.isArray(row.platformId)) {
    row.platformId = row.platformId[1]
  }
  
  create(row).then(() => {
    ElMessage.success('添加成功');
    addModalApi.close()
    gridApi.query()
  }).catch(() => { })
}
//删除
async function handleDelete(row: any) {
  remove(row.templateId).then(() => {
    ElMessage.success('删除成功');
    gridApi.query()
  }).catch(() => { })

}
//发送消息
function send(row: templateApi.TemplateRsp) {

  sendFormApi.setValues({
    templateId: row.templateId,
    templateName: row.templateName,
    messageType: findType(plateformDic, row.platformId),
  }, false)
  sendModalApi.open()
}
function handleSend(row: any) {
  if (row.messageType != 3) {
    if (!row.receiveAccounts && !row.accountGroupId) {
      ElMessage.error('接收账户为空');
      return;
    }
  }

  sendMessage(row).then(() => {
    ElMessage.success('发送成功');
    sendModalApi.close()
  }).catch(() => { })
}
function findPlatformName(plateformDic: OneLayerTreeDic<number, string>[], platformId: string): string {
  for (const item of plateformDic) {
    if (item.children) {
      for (const e of item.children) {
        if (e.value === platformId) {
          return e.label
        }
      }
    }
  }
  return ''
}
</script>
<style lang="css">
input[aria-hidden="true"] {

  display: none !important;

}
</style>
