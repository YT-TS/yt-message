<template>
  <div>
    <Grid>
      <template #action="{ row }">
        <div class=" flex  justify-evenly justify-around">
          <el-popconfirm title="确定要发送吗?" @confirm="send(row)">
            <template #reference>
              <el-link type="info">发送消息</el-link>
            </template>
          </el-popconfirm>
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
    <AddModal title="新增">
      <AddForm />
    </AddModal>
    <EditModal title="编辑">
      <EditForm />
    </EditModal>
  </div>
</template>
<script setup lang="ts">
import type { VxeGridProps } from '#/adapter/vxe-table';
import type { VbenFormProps } from '#/adapter/form';
import { useVbenVxeGrid } from '#/adapter/vxe-table';
import { page as templatePage, update, add as create, remove, preparedTemplateApi,send as sendMessage} from '#/api/system/prepared-template';
import { dic, treeDic, type Dic, type OneLayerTreeDic } from '#/api/dic';
import { computed, ref, h, reactive } from 'vue';
import { useVbenModal } from '@vben/common-ui';
import { useVbenForm } from '#/adapter/form'
import {
  ElLink,
  ElButton,
  ElMessage,
  ElPopconfirm
} from 'element-plus';
import AccountGroupSelect from '#/components/account-group-select.vue';
//获取平台-模板字典
let platformTemplateDic = ref<OneLayerTreeDic[]>([]);
treeDic('PlatformAndTemplate').then(res => platformTemplateDic.value = res);
//获取账户组字典
const accountGroupDic = ref<Dic[]>([]);
dic('accountGroup').then(res => accountGroupDic.value = res);
//按钮字典
const yesValue = ref();
const noValue = ref();
dic('yesOrNo').then(res => {
  yesValue.value = res.find(item => item.label === 'YES')?.value;
  noValue.value = res.find(item => item.label === 'NO')?.value;
})
//获取消息类型-平台字典
let plateformDic = reactive<OneLayerTreeDic<number, string>[]>([]);
treeDic('platform').then(res => plateformDic = res);

//表格选项
const gridOptions: VxeGridProps<preparedTemplateApi.PreparedTemplateRsp> = {
  columns: [
    { title: '序号', type: 'seq' },
    { field: 'templateName', title: '预发送模板名' },
    {
      field: 'templateId', title: '消息模板',
      formatter: (params) => {
        return findTemplateName(platformTemplateDic.value,params.cellValue)
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
          templateId: formValues.templateId ? formValues.templateId[1] : undefined,
        });
      },
    },
  },
};
//表格上搜索表单选项
const formOptions: VbenFormProps = {
  schema: [
    {
      component: 'ApiCascader',
      // 对应组件的参数
      componentProps: {
        clearable: true,
        filterable: true,
        options: computed(() => platformTemplateDic.value),
      },
      // 字段名
      fieldName: 'templateId',
      label: '消息模板',

    },
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
//添加模态框
const [AddModal, addModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
});
//添加表单
const [AddForm, addFormApi] = useVbenForm({
  // 提交函数
  handleSubmit: handleAdd,
  commonConfig: {
    labelWidth: 200,
    colon: true,
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
      component: 'ApiCascader',
      // 对应组件的参数
      componentProps: {
        // api: getPlateformDic,
        filterable: true,
        options: computed(() => platformTemplateDic.value),
        onChange: (value: any) => {
          addFormApi.setFieldValue('messageType', findType(plateformDic, value[0]))
        },
      },

      // 字段名
      fieldName: 'templateId',
      // 界面显示的label
      label: '消息模板',
      rules: 'required',
    },
    {
      component: 'InputTag',
      // 字段名
      fieldName: 'subjectParams',
      // 界面显示的label
      label: '模板标题参数',
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
      label: '模板内容参数值',
      help: '回车输入下一个'
    },
    {
      component: 'Input',
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
});
//打开添加框
function add() {
  addModalApi.open()
}
//提交添加
async function handleAdd(row: any) {
  if (row.messageType != 3) {
    if (!row.receiveAccounts && !row.accountGroupId) {
      ElMessage.error('接收账户为空');
      return;
    }
  }
  // const raw = row.receiveAccounts
  // if (row.receiveAccounts) {
  //   row.receiveAccounts = row.receiveAccounts.split(',').filter((item: string) => item !== "")
  // }

  if (Array.isArray(row.templateId)) {
    row.templateId = row.templateId[1]
  }
  create(row).then(() => {
    ElMessage.success('添加成功');
    // row.receiveAccounts = raw
    addModalApi.close()
    gridApi.query()
  }).catch(() => {
    // row.receiveAccounts = raw
  })



}
//编辑模态框
const [EditModal, editModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
  onBeforeClose: () => {
    gridApi.query()
  },
});
//编辑表单
const [EditForm, editFormApi] = useVbenForm({
  // 提交函数
  handleSubmit: handleEdit,
  commonConfig: {
    labelWidth: 200,
    colon: true,
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
      component: 'ApiCascader',
      // 对应组件的参数
      componentProps: {
        // api: getPlateformDic,
        filterable: true,
        options: computed(() => platformTemplateDic.value),
        placeholder: '消息模板',
        onChange: (value: any) => {
          addFormApi.setFieldValue('messageType', findType(plateformDic, value[0]))
        },
      },
      disabled: true,
      // 字段名
      fieldName: 'templateId',
      // 界面显示的label
      label: '消息模板',
      rules: 'required',
    },
    {
      component: 'InputTag',
      // 字段名
      fieldName: 'subjectParams',
      // 界面显示的label
      label: '模板标题参数',
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
      label: '模板内容参数值',
      help: '回车输入下一个'
    },
    {
      component: 'Input',
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
});
//打开编辑框
async function edit(row: preparedTemplateApi.PreparedTemplateRsp) {
  const platformId = findPlatform(platformTemplateDic.value, row.templateId)
  if (platformId) {
    row.messageType = findType(plateformDic, platformId)
  }

  if (row.contentParams) {
    row.contentParams = row.contentParams.split(',').filter((item: string) => item !== "")
  } else {
    row.contentParams = []
  }
  if (row.subjectParams) {
    row.subjectParams = row.subjectParams.split(',').filter((item: string) => item !== "")
  } else {
    row.subjectParams = []
  }


  editFormApi.setValues(row, false)
  editModalApi.open()
}
//提交编辑
async function handleEdit(row: any) {
  if (row.messageType != 3) {
    if (!row.receiveAccounts && !row.accountGroupId) {
      ElMessage.error('接收账户为空');
      return;
    }
  }
  // const raw = row.receiveAccounts
  // if (row.receiveAccounts) {
  //   row.receiveAccounts = row.receiveAccounts.split(',').filter((item: string) => item !== "")
  // } else {
  //   row.receiveAccounts = []
  // }

  if (Array.isArray(row.templateId)) {
    row.templateId = row.templateId[1]
  }
  update(row).then(() => {
    ElMessage.success('修改成功');
    // row.receiveAccounts = raw
    editModalApi.close()
  }).catch(() => {
    // row.receiveAccounts = raw
  })



}
//发送消息
function send(row: preparedTemplateApi.PreparedTemplateRsp) {
  sendMessage(row.preparedTemplateId).then(() => {
    ElMessage.success('发送成功');
  }).catch(() => { })


}
//删除
async function handleDelete(row: any) {
  remove(row.preparedTemplateId).then(() => {
    ElMessage.success('删除成功');
    gridApi.query()
  }).catch(() => { })

}
function findPlatform(platformTemplateDic: OneLayerTreeDic<string, string>[], templateId: string) {
  for (const item of platformTemplateDic) {
    if (item.children) {
      for (const e of item.children) {
        if (e.value === templateId) {
          return item.value
        }
      }
    }
  }
}
function findTemplateName(platformTemplateDic: OneLayerTreeDic<string, string>[], templateId: string):string {
  for (const item of platformTemplateDic) {
    if (item.children) {
      for (const e of item.children) {
        if (e.value === templateId) {
          return e.label
        }
      }
    }
  }
  return ''
}
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
</script>
