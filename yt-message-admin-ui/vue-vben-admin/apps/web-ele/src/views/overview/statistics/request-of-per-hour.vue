<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue';

import {
  EchartsUI,
  type EchartsUIType,
  useEcharts,
} from '@vben/plugins/echarts';

import { hour } from '#/api/overview/statistics';
const chartRef = ref<EchartsUIType>();
const { renderEcharts } = useEcharts(chartRef);
const hours: string[] = [];
for (var i = 0; i < 24; i++) {
  var startHour = i < 10 ? '0' + i : i; // 补零
  var endHour = (i + 1) < 10 ? '0' + (i + 1) : (i + 1); // 补零
  hours.push(startHour + ':00-' + endHour + ':00');
}
let intervalId: any;
const flush = ()=>{
  hour().then(res => {
    const data: any[] = [];

    data.push({
      data: res.pie,
      type: 'bar',
      name: '小时总请求量',
    })
    for (const key in res.bar) {
      data.push({
        name: key,
        type: 'line',
        yAxisIndex: 1, // 使用第二个 y 轴
        data: res.bar[key]
      });
    }
    renderEcharts({
      animation: false,
      series: data,
      legend: {
        data: Object.keys(res.bar)
      },
      grid: {
        bottom: 0,
        containLabel: true,
        left: '1%',
        right: '1%',
        top: '10%',
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        data: hours,
        axisLabel: {
          rotate: 45, // 如果标签太长，可以旋转
          interval: 0 // 显示所有标签
        }
      },
      yAxis: [
        {
          type: 'value',
          name: '小时总请求量',
        },
        {
          type: 'value',
          name: '小时各类型请求量',
        }
      ],
    });
  })
}
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
  <EchartsUI ref="chartRef" />
</template>
