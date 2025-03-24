<template>
  <div>
    <Grid>
      <template #action="{ row }">
        <div class=" flex  justify-evenly justify-around">
          <el-link type="primary" @click="url(row)">获取链接</el-link>
          <!-- <el-link type="primary" @click="download(row)">下载</el-link> -->
          <el-popconfirm title="确定要删除吗?" @confirm="handleDelete(row)">
            <template #reference>
              <el-link type="danger">删除</el-link>
            </template>
          </el-popconfirm>
        </div>
      </template>
      <template #size>
        <div class="flex items-center justify-center">
          <span>附件大小 单位：</span>
          <el-select @change="updateSize" v-model="sizeUnit" placeholder="单位" size="small"
            style="width: 60px;background-color: inherit;">
            <el-option v-for="item in sizeUnits" :key="item" :label="item" :value="item" />
          </el-select>
        </div>

      </template>
      <template #toolbar-tools>
        <el-button type="primary" @click="upload">上传附件</el-button>
      </template>

    </Grid>
    <UrlModal title="获取链接">
      <UrlForm />
    </UrlModal>
    <UploadModal title="上传附件">
      <UploadForm />
    </UploadModal>
  </div>
</template>
<script setup lang="ts">
import type { VxeGridProps } from '#/adapter/vxe-table';
import type { VbenFormProps } from '#/adapter/form';
import { useVbenVxeGrid } from '#/adapter/vxe-table';
import { page as attachmentPage, attachmentApi, upload as fileUpload, url as attachmentUrl, remove } from '#/api/system/attachment';
import { useVbenForm, useVbenModal } from '@vben/common-ui';
import {
  ElLink,
  ElButton,
  ElMessage,
  ElPopconfirm,
  ElSelect,
  ElOption,
  ElMessageBox,
  type Action,
  ElInput,
  type MessageBoxState,
  type UploadFile,
  type UploadFiles,
} from 'element-plus';
import { useDicStore } from '#/store';
import { computed, h, ref } from 'vue';

import bytes from 'bytes';

const sizeUnits = ['B', 'KB', 'MB', 'GB'];
const sizeUnit = ref('B')
//获取消息类型字典
const dicStore = useDicStore();
const attachmentTypeDic = dicStore.getDic("attachmentType");
const updateSize = () => {
  gridApi.query()
}


const handleDelete = (row: any) => {
  remove(row.attachmentId).then(() => {
    ElMessage.success('删除成功');
    gridApi.query()
  }).catch(() => { })

}
const upload = () => {
  uploadModalApi.open()
}

const handleUpload = (data: any) => {

  fileUpload(data).then(() => {
    ElMessage.success('上传成功');
    uploadModalApi.close()
    gridApi.query()
  }).catch(() => { })
}
const url = (row: any) => {
  urlFormApi.setValues(row, false)
  urlModalApi.open()

}
const urlHandle = (data: any) => {
  attachmentUrl(data.attachmentId, data.expiry).then((res) => {
    urlModalApi.close()
    ElMessageBox({
      message: () =>
        h(ElInput, {
          value: res.url,
          type: "textarea",
          // disabled:true
          readonly: true,
          autosize: true,
        },
        ),
      title: '获取链接',
      confirmButtonText: '复制',
      confirmButtonClass: 'copy',
      closeOnClickModal: false,
      closeOnPressEscape: false,
      showClose: false,
      customClass: "messageStyle",

      callback: () => {
        ElMessage.success('复制成功');
      },
      beforeClose: (action: Action, _instance: MessageBoxState, done: () => void) => {
        if (action === 'confirm') {
          navigator.clipboard.writeText(res.url);
        }
        done();
      }
    })

  }).catch(() => { })
}
// const download = (row: any) => {
//   attachmentUrl(row.attachmentId, 1).then((res) => {
   
//   }).catch(() => { })
// }
//表格选项
const gridOptions: VxeGridProps<attachmentApi.AttachmentRsp> = {
  columns: [
    { title: '序号', type: 'seq' },
    {
      field: 'type',
      title: '附件类型',
      formatter({ cellValue }) {
        const item = attachmentTypeDic.value.find(item => item.value === cellValue)
        return item ? item.label : cellValue
      }
    },
    {
      title: '附件名称',
      field: 'originName',
    },
    {
      slots: {
        header: 'size'
      },
      title: '附件大小',
      field: 'size',
      formatter({ cellValue }) {
        return bytes(cellValue, { unit: sizeUnit.value, decimalPlaces: 5 });
      }
    },

    {
      title: '上传时间'
      ,
      field: 'createTime',
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
        return await attachmentPage({
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
        options: computed(() => attachmentTypeDic.value),
        placeholder: '请选择',
      },
      fieldName: 'type',
      label: '附件类型',
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

//上传模态框
const [UploadModal, uploadModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
})

//上传表单
const [UploadForm,uploadFormApi] = useVbenForm({
  // 提交函数
  handleSubmit: handleUpload,
  commonConfig: {
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
      component: 'Select',
      componentProps: {
        options: computed(() => attachmentTypeDic.value),
        placeholder: '附件类型',
      },
      fieldName: 'type',
      label: '附件类型',
      rules: 'required',
    },
    {
      component: 'FileUpload',
      componentProps: {
        onChange: (uploadFile: UploadFile) => {
           uploadFormApi.setFieldValue(
            'file',
            uploadFile.raw
           );
        },
        onRemove: () => {
          uploadFormApi.setFieldValue(
            'file',
            undefined
           );
        },
      },
      fieldName: 'file',
      label: '附件',
      rules: 'required',
    },

  ]
});
//获取链接模态框
const [UrlModal, urlModalApi] = useVbenModal({
  fullscreenButton: false,
  closable: false,
  footer: false,
})
//url表单
const [UrlForm, urlFormApi] = useVbenForm({
  // 提交函数
  handleSubmit: urlHandle,
  commonConfig: {
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
      component: 'InputNumber',
      componentProps: {
        placeholder: '最少1天, 最多7天',
        min: 1,
        max: 7,
        step: 1,
        'step-strictly': true,

        style: {
          width: '100%'
        }
      },
      fieldName: 'expiry',
      label: '链接有效期',
      rules: 'required',
    },
  ]
});
</script>
<style lang="scss">
.el-message-box__container {
  display: block !important;
}

.el-select__wrapper {
  background-color: inherit
}
</style>
