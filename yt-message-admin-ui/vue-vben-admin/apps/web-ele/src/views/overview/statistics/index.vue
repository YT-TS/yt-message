<template>
  <div class="p-3">
 
      <RequestOfTotal>
      </RequestOfTotal>

      <AnalysisChartCard title="小时请求量" class="mt-3">
        <RequestOfPerHour></RequestOfPerHour>
      </AnalysisChartCard>
      <div class="mt-3 grid grid-flow-col auto-cols-fr gap-3" >
        <AnalysisChartCard  title="">
          <RequestOfType :data="todayData.typesOfRequest"></RequestOfType>
        </AnalysisChartCard>
        <AnalysisChartCard  title="">
          <RequestOfTemplate :data="todayData.templatesOfRequest" ></RequestOfTemplate>
        </AnalysisChartCard>
        <AnalysisChartCard  title="">
          <RequestOfPlatform :data="todayData.platformsOfRequest"></RequestOfPlatform>
        </AnalysisChartCard>
      </div>
      
  </div>
</template>


<script lang="ts" setup>

import RequestOfPerHour from './request-of-per-hour.vue'
import RequestOfTotal from './request-of-total.vue'

import RequestOfTemplate from './request-of-template.vue'
import RequestOfType from './request-of-type.vue'
import RequestOfPlatform from './request-of-platform.vue'


import {
  AnalysisChartCard,
} from '@vben/common-ui';
import {today} from '#/api/overview/statistics'
import { onMounted, onUnmounted, ref } from 'vue'
const todayData = ref<any>({})
today().then(res => {
  todayData.value = res
})

const flush = ()=>{
  today().then(res => {
  todayData.value = res
})
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
