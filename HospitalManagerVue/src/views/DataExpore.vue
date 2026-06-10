<template>
  <div class="Echarts">
    <div id="orderPeople" class="chart chart-wide"></div>
    <div id="orderSection" class="chart chart-wide"></div>
    <div class="chart-row">
      <div id="orderGender" class="chart chart-half"></div>
      <div id="patientAge" class="chart chart-half"></div>
    </div>
  </div>
</template>
<script>
import request from "@/utils/request.js";
export default {
  name: "dataExpore",
  data() {
    return {
      sevenDate: [],
      sevenOrder: [],
    };
  },
  methods: {
    //统计患者年龄分布
    patientAge(){
      var myChart = this.$echarts.init(document.getElementById("patientAge"));
      request.get("patient/patientAge")
      .then(res => {
        const ageData = res.data.data || [];
        var option = {
           title: {
        text: '患者年龄段分布',
        left: 'center'
    },
    tooltip: {
        trigger: 'item'
    },
    legend: {
        top: '5%',
        left: 'center'
    },
    series: [
        {
            name: '年龄段',
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
                    fontSize: '40',
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: [
                {value: ageData[0] || 0, name: '0-9岁'},
                {value: ageData[1] || 0, name: '10-19岁'},
                {value: ageData[2] || 0, name: '20-29岁'},
                {value: ageData[3] || 0, name: '30-39岁'},
                {value: ageData[4] || 0, name: '40-49岁'},
                {value: ageData[5] || 0, name: '50-59岁'},
                {value: ageData[6] || 0, name: '60-69岁'},
                {value: ageData[7] || 0, name: '70-79岁'},
                {value: ageData[8] || 0, name: '80-89岁'},
                {value: ageData[9] || 0, name: '90-99岁'},
            ]
        }
    ]
};
       
            

// 使用刚指定的配置项和数据显示图表。
          myChart.setOption(option);


      })
      .catch(err => {
        console.error(err); 
      })
    },
    //统计挂号科室人数
    orderSection(){
      var myChart = this.$echarts.init(document.getElementById("orderSection"));
      request.get("order/orderSection")
      .then(res => {
        const sectionData = res.data.data || [];
        var option = {
             title: {
        text: '挂号科室人数统计',
        left: 'center'
    },
    xAxis: {
        type: 'category',
        data: sectionData.map((item) => item.doctor && item.doctor.dSection ? item.doctor.dSection : '未知科室'),
        axisLabel: {//解决各个不显示问题
           interval:0,
           rotate:10,
           }

    },
    yAxis: {
        type: 'value'
    },
    series: [{
        data: sectionData.map((item) => item.countSection || 0),
        type: 'bar',
        showBackground: true,
        backgroundStyle: {
            color: 'rgba(180, 180, 180, 0.2)'
        }
    }]
};
// 使用刚指定的配置项和数据显示图表。
          myChart.setOption(option);


        

      })
      .catch(err => {
        console.error(err); 
      })
    },
    //挂号男女比例
    orderGender(){
      var myChart = this.$echarts.init(document.getElementById("orderGender"));
      request.get("order/orderGender",)
      .then(res => {
      const genderData = res.data.data || [];
      var option = {
    title: {
        text: '患者性别比例',
        left: 'center'
    },
    tooltip: {
        trigger: 'item'
    },
    legend: {
        orient: 'vertical',
        left: 'left',
    },
    series: [
        {
            name: '人数',
            type: 'pie',
            radius: '50%',
            data: genderData.length
                ? genderData.map((item) => ({
                    value: item.countGender || 0,
                    name: item.patient && item.patient.pGender ? item.patient.pGender : '未知'
                }))
                : [{ value: 0, name: '暂无数据' }],
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};
          // 使用刚指定的配置项和数据显示图表。
          myChart.setOption(option);

      })
      .catch(err => {
        console.error(err); 
      });


    },
    //获取过去num天日期
    pastSeven(num) {
      var date = new Date();
      date.setDate(date.getDate() - num);
      var time = date.getMonth() + 1 + "-" + date.getDate();
      return time;
    },
    //挂号人数图表
    orderPeople() {
      var myChart = this.$echarts.init(document.getElementById("orderPeople"));
      request
        .get("order/orderSeven")
        .then((res) => {
          if (res.data.status !== 200)
            return this.$message.error("数据请求失败！");
          var option = {
            title: {
              text: "近20天挂号人数折线图",
              left: "5%",
            },
            xAxis: {
              type: "category",
              data: this.sevenDate,
            },
            yAxis: {
              type: "value",
            },
            series: [
              {
                data: res.data.data,
                type: "line",
              },
            ],
          };
          // 使用刚指定的配置项和数据显示图表。
          myChart.setOption(option);
        })
        .catch((err) => {
          console.error(err);
        });
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.orderPeople();
      this.orderGender();
      this.orderSection();
      this.patientAge();
    });
  },
  created() {
    //获取过去7天日期
    for (var i = 20; i > 0; i--) {
      this.sevenDate.push(this.pastSeven(i));
    }
  },
};
</script>

<style scoped>
.Echarts {
  width: 100%;
}
.chart {
  min-height: 380px;
}
.chart-wide {
  width: 100%;
  height: 400px;
}
.chart-row {
  display: flex;
  gap: 16px;
}
.chart-half {
  flex: 1;
  height: 500px;
  min-width: 0;
}
</style>
