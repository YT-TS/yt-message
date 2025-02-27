<script setup lang="ts">
import { list, update } from '#/api/system/token'
import { ref } from 'vue';
import { ElTable, ElTableColumn, ElLink,ElPopconfirm, ElMessage } from 'element-plus';
import { dic, type Dic } from '#/api/dic';
const tokenList = () => {
  list().then(res => {
    tableData.value = res
  })
}
const platformFormatter = (row: any) => {
  const dic = platformDic.value.find(item => item.value === row.platformId);
  return dic ? dic.label : '';
};
const expireTimestampFormatter = (row: any) => {
  const date = new Date(row.expireTimestamp);
  return date.toLocaleString();
};
const updateToken = (row: any) => {
  console.log(row)
  update(row.platformId).then(() => {
    tokenList()
    ElMessage.success('更新成功');
  }).catch(() => {
  })
}

const tableData = ref<any[]>([]);
//获取平台组字典
const platformDic = ref<Dic[]>([]);
dic('platform').then(res => platformDic.value = res);
tokenList()
</script>

<template>
  <div>
    <el-table :data="tableData">
      <el-table-column type="index" />
      <el-table-column prop="platformId" label="平台" :formatter="platformFormatter" />
      <el-table-column prop="expireTimestamp" label="到期时间" :formatter="expireTimestampFormatter" />
      <<el-table-column label="operations">
        <template #default="scope">
          <el-popconfirm title="确定要删除吗?" @confirm="updateToken(scope.row)">
            <template #reference>
              <ElLink  type="danger">
                更新token
              </ElLink>
            </template>
          </el-popconfirm>

        </template>
        </el-table-column>
    </el-table>
  </div>
</template>
