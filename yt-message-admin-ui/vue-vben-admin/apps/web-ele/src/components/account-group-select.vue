<template>
  <el-select v-bind="$attrs">
    <el-option v-for="item in props.options" :key="item.value" :label="item.label" :value="item.value">
      <span style="float: left">{{ item.label }}</span>
      <el-popover placement="right" trigger="click" :popper-style="{minWidth:'360px',width:'auto'}">
        <template #reference>
          <ElLink style="float: right;" type="primary" @click.stop="detail(item.value)">详情</ElLink>
        </template>
        <el-row :gutter="10" v-for="(row, rowIndex) in rowsData" :key="rowIndex">
          <el-col v-for="(item, colIndex) in row" :key="colIndex" :span="8">
            <div style="width: 100%;" class="truncate hover:text-clip">{{ item }}</div>
          </el-col>
        </el-row>
      </el-popover>
    </el-option>
  </el-select>
</template>
<script setup lang="ts">
import {
  ElLink,
  ElSelect,
  ElOption,
  ElPopover,
  ElRow,
  ElCol
} from 'element-plus';
import { members } from '#/api/system/account';
import { computed, ref } from 'vue';
const props = defineProps<{
  options: any[]
}>()
const accounts = ref<string[]>([])
const rowsData = computed(() => {
  const rows = [];
  for (let i = 0; i < accounts.value.length; i += 3) {
    rows.push(accounts.value.slice(i, i + 3));
  }
  if (rows.length > 0) {
    const last = rows[rows.length - 1] || [];
    if (last.length < 3) {
      last.push(...new Array(3 - last.length).fill(''));
    }
  }
  return rows;
})
const detail = (id: any) => {
  members(id).then(res => {
    accounts.value = res.split(',')
  })
}


</script>
