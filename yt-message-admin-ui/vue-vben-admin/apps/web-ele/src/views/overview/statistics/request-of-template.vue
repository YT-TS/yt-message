<script setup lang="ts">
import {
  EchartsUI,
  type EchartsUIType,
  useEcharts,
} from '@vben/plugins/echarts';
import { ref, watch } from 'vue';
import { defineProps } from 'vue';

const props = defineProps<{
  data: any
}>();


const chartRef = ref<EchartsUIType>();
const { renderEcharts } = useEcharts(chartRef);
watch(() => props.data, (newValue) => {
  const data: any[] = []
  for (const key in newValue) {
    data.push({
      name: key,
      value: newValue[key]
    })
  }
  renderEcharts({
    animation: false,
    title: {
      text: '消息模板请求量',
      subtext: '本日',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: '5%',
      type: 'scroll',
      left: 'center'
    },
    series: [
      {
        name: '请求量',
        type: 'pie',
        radius: '50%',
        data: data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }

  );
})  
</script>

<template>

  <EchartsUI ref="chartRef" />

</template>
