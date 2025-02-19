<script setup lang="ts">
import {
  AnalysisChartCard,

} from '@vben/common-ui';
import { ElStatistic } from 'element-plus';
import {total} from '#/api/overview/statistics';
import { onMounted, onUnmounted, ref } from 'vue';
const data = ref<any>({});
const flush = ()=>{
  total().then((res) => {
  data.value = res;
});
}
let intervalId: any;
onMounted(() => {
  flush()
  intervalId = setInterval(() => {
    flush()
  }, 5000);
});

onUnmounted(() => {
clearInterval(intervalId);
})


</script>

<template>
  <div class="grid grid-flow-col auto-cols-fr gap-3" >
    <AnalysisChartCard title="历史请求量">
      <div   class="grid grid-flow-col auto-cols-fr gap-3">
        <div>
          <ElStatistic :value="data.historyTotal">
            <template #title>
              <span>总请求量</span>
            </template>
          </ElStatistic>
        </div>
        <div>
          <ElStatistic :value="data.historySuccessTotal">
            <template #title>
              <span>成功请求量</span>
            </template>
          </ElStatistic>
        </div>
        <div>
          <ElStatistic :value="data.historyFailTotal">
            <template #title>
              <span>失败请求量</span>
            </template>
          </ElStatistic>
        </div>
      </div>
    </AnalysisChartCard>
    <AnalysisChartCard title="本月总请求量">
      <div   class="grid grid-flow-col auto-cols-fr gap-3">
        <div>
          <ElStatistic :value="data.monthTotal">
            <template #title>
              <span>总请求量</span>
            </template>
          </ElStatistic>
        </div>
        <div>
          <ElStatistic :value="data.monthSuccessTotal">
            <template #title>
              <span>成功请求量</span>
            </template>
          </ElStatistic>
        </div>
        <div>
          <ElStatistic :value="data.monthFailTotal">
            <template #title>
              <span>失败请求量</span>
            </template>
          </ElStatistic>
        </div>
      </div>

    </AnalysisChartCard>
    <AnalysisChartCard title="本日总请求量">
      <div   class="grid grid-flow-col auto-cols-fr gap-3">
        <div>
          <ElStatistic :value="data.todayTotal">
            <template #title>
              <span>总请求量</span>
            </template>
          </ElStatistic>
        </div>
        <div>
          <ElStatistic :value="data.todaySuccessTotal">
            <template #title>
              <span>成功请求量</span>
            </template>
          </ElStatistic>
        </div>
        <div>
          <ElStatistic :value="data.todayFailTotal">
            <template #title>
              <span>失败请求量</span>
            </template>
          </ElStatistic>
        </div>
      </div>

    </AnalysisChartCard>
    
  </div>
</template>
