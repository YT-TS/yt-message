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
    <EditModal title="编辑">
      <EditForm />
    </EditModal>
    <AddModal title="新增">
      <AddForm />
    </AddModal>
  </div>
</template>
<script setup lang="ts">
import type { VxeGridProps } from '#/adapter/vxe-table';
import type { VbenFormProps } from '#/adapter/form';
import { useVbenVxeGrid } from '#/adapter/vxe-table';
import type { accountApi } from '#/api/system/account';
import { page as accountPage, update, add as create, remove } from '#/api/system/account';
import { dic, type Dic } from '#/api/dic';
import { computed,  ref } from 'vue';
import { useVbenModal } from '@vben/common-ui';
import { useVbenForm } from '#/adapter/form';
import {
  ElLink,
  ElButton,
  ElMessage,
  ElPopconfirm
} from 'element-plus';
//获取账户类型字典
const accountTypeDic = ref<Dic[]>([]);
dic('accountType').then(res => accountTypeDic.value = res);
//表格选项
const gridOptions: VxeGridProps<accountApi.AccountGroupRsp> = {
  columns: [
    { title: '序号', type: 'seq' },
    { field: 'groupName', title: '账户组名' },
    {
      field: 'type',
      title: '账户类型',
      formatter({ cellValue }) {
        const item = accountTypeDic.value.find(item => item.value === cellValue)
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
        return await accountPage({
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
        options: computed(() => accountTypeDic.value),
        placeholder: '请选择',
      },
      fieldName: 'type',
      label: '账户类型',
    }
  ],
  // 控制表单是否显示折叠按钮
  showCollapseButton: false,
  submitButtonOptions: {
    content: '查询',
  },
};
//生成表格组件
const [Grid,gridApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});
//添加模态框
const [AddModal, addModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
  class: 'w-1/2',
});
//添加表单
const [AddForm] = useVbenForm({
  // 提交函数
  handleSubmit: handleAdd,
  commonConfig: {
    labelWidth: 150,
    colon:true,
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
        options: computed(() => accountTypeDic.value),
        placeholder: '账户类型',
      },
      fieldName: 'type',
      label: '账户类型',
    },
    {
      component: 'Input',
      // 对应组件的参数
      componentProps: {
        placeholder: '账户组名',
      },
      // 字段名
      fieldName: 'groupName',
      // 界面显示的label
      label: '账户组名',
    },
    {
      component: 'Input',
      fieldName: 'members',
      componentProps: {
        placeholder: '多个账户间以英文逗号分隔',
        type: 'textarea',
      },
      // 界面显示的label
      label: '账户组成员',
      rules: 'required',
      help: '多个账户间以英文逗号分隔',
    },
  ]
});
//打开添加框
function add() {
  addModalApi.open()
}
//提交添加
async function handleAdd(row: any) {
  const raw = row.members
  row.members =row.members.split(',').filter((item : string) => item !== "")
  create(row).then(() => {
    ElMessage.success('添加成功');
    row.members = raw
    addModalApi.close()
    gridApi.query()
  }).catch(() => {
      row.members = raw
  })
}
//编辑模态框
const [EditModal, editModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
  class: 'w-1/2',
});
//编辑表单
const [EditForm,editFormApi] = useVbenForm({
  // 提交函数
  handleSubmit: handleEdit,
  commonConfig: {
    labelWidth: 150,
    colon:true,
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
        options: computed(() => accountTypeDic.value),
        placeholder: '账户类型',
      },
      fieldName: 'type',
      label: '账户类型',
      disabled: true,
    },
    {
      component: 'Input',
      // 对应组件的参数
      componentProps: {
        placeholder: '账户组名',
      },
      // 字段名
      fieldName: 'groupName',
      // 界面显示的label
      label: '账户组名',
    },
    {
      component: 'Input',
      fieldName: 'members',
      componentProps: {
        placeholder: '多个账户间以英文逗号分隔',
        type: 'textarea',
      },
      // 界面显示的label
      label: '账户组成员',
      rules: 'required',
      help: '多个账户间以英文逗号分隔',
    },
  ]
});
function edit(row: accountApi.AccountGroupRsp) {
  editFormApi.setValues(row, false)
  editModalApi.open()
}
//提交编辑
async function handleEdit(row: any) {
  const raw = row.members
  row.members = row.members.split(',').filter((item : string) => item !== "")
  update(row).then(() => {
    ElMessage.success('修改成功');
    editModalApi.close()
    row.members = raw
    gridApi.query()
  }).catch(() => {
    row.members = raw
   })
}
//删除
async function handleDelete(row: any) {
  remove(row.groupId).then(() => {
    ElMessage.success('删除成功');
    gridApi.query()
  }).catch(() => { })

}
</script>
