<script setup lang="ts">
import {
  EchartsUI,
  type EchartsUIType,
  useEcharts,
} from '@vben/plugins/echarts';
import {  ref, watch } from 'vue';
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
      text: '消息类型请求量',
      subtext: '本日',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: '5%',
      left: 'center'
    },
    series: [
      {
        name: '请求量',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 40,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data
      }
    ]
  });
})

</script>

<template>

  <EchartsUI ref="chartRef" />

</template>
