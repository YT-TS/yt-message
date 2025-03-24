<script setup lang="ts">
 import {
  ElUpload,
  ElIcon,
  type UploadFile,
  type UploadFiles,
} from 'element-plus';

import { UploadFilled } from '@element-plus/icons-vue'
import { ref } from 'vue'
interface Props {
  onChange?: (uploadFile: UploadFile, uploadFiles: UploadFiles) => void
  onRemove?:(uploadFile: UploadFile, uploadFiles: UploadFiles) => void
}
const props = withDefaults(defineProps<Props>(), {
  onChange: () => {},
  onRemove: () => {},
});

const value = ref([])
</script>

<template>
<el-upload
    style="width: 100%;"
    drag
    :auto-upload='false'
    :limit="1"
    v-model:file-list="value"
    :on-change="props.onChange"
    :on-remove="props.onRemove"
  >
    <el-icon  class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text" >
      拖动文件或者 <em>点击上传</em>
    </div>
    <!-- <el-button @click="console.log(file)">aaa</el-button> -->
  </el-upload>
</template>
