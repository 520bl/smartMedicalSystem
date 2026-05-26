<template>
  <div class="dashboard-page">
    <div class="dashboard-welcome">
      <el-avatar :size="48" class="avatar-doctor">
        {{ (userInfo?.name || userInfo?.username || '医')[0] }}
      </el-avatar>
      <div>
        <div class="welcome-title">欢迎你，{{ userInfo?.name || userInfo?.username }}！</div>
        <div class="welcome-role">医生工作台</div>
      </div>
    </div>

    <el-row :gutter="20" class="dashboard-row" v-loading="loading">
      <el-col :span="8">
        <el-card class="stat-card stat-blue" shadow="hover">
          <i class="fa-solid fa-hourglass-half stat-icon"></i>
          <div class="stat-num">{{ stats.todayWaiting ?? 0 }}</div>
          <div class="stat-desc">今日待诊</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card stat-green" shadow="hover">
          <i class="fa-solid fa-user-check stat-icon"></i>
          <div class="stat-num">{{ stats.todayCompleted ?? 0 }}</div>
          <div class="stat-desc">已接诊</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card stat-orange" shadow="hover">
          <i class="fa-solid fa-file-medical stat-icon"></i>
          <div class="stat-num">{{ stats.pendingRecords ?? 0 }}</div>
          <div class="stat-desc">待写病历</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="18" class="dashboard-row quick-row">
      <el-col :span="6" v-for="item in quickEntries" :key="item.title">
        <el-card class="quick-card" @click="goto(item.path)">
          <i :class="item.icon + ' quick-icon'"></i>
          <span class="quick-title">{{ item.title }}</span>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getDoctorDashboardStats } from '@/api/doctor'

const router = useRouter()
const loading = ref(false)
const stats = ref({})

const userInfo = computed(() => {
  try {
    return JSON.parse(sessionStorage.getItem('userInfo') || '{}')
  } catch {
    return {}
  }
})

const quickEntries = [
  { title: '待诊队列', icon: 'fa-solid fa-list', path: '/doctor/queue' },
  { title: '病历书写', icon: 'fa-solid fa-pen', path: '/doctor/medical-record' },
  { title: '处方开立', icon: 'fa-solid fa-prescription', path: '/doctor/prescription' },
  { title: '我的排班', icon: 'fa-solid fa-calendar', path: '/doctor/schedule' }
]

const loadStats = async () => {
  loading.value = true
  try {
    const res = await getDoctorDashboardStats()
    stats.value = res || {}
  } finally {
    loading.value = false
  }
}

const goto = (path) => router.push(path)

onMounted(loadStats)
</script>

<style scoped>
.avatar-doctor {
  background: linear-gradient(135deg, #5c7c4a, #3d5a32);
  color: #fff;
  font-weight: 600;
}
.stat-blue .stat-icon { color: #3b82f6; }
.stat-green .stat-icon { color: #22c55e; }
.stat-orange .stat-icon { color: #f59e0b; }
.stat-card {
  text-align: center;
  padding: 8px 0;
  border: none;
}
.stat-icon {
  font-size: 28px;
  margin-bottom: 8px;
}
.stat-num {
  font-size: 26px;
  font-weight: 700;
  color: #1e293b;
}
.stat-desc {
  font-size: 13px;
  color: #64748b;
  margin-top: 4px;
}
.quick-card {
  cursor: pointer;
  text-align: center;
  padding: 20px 12px;
  transition: transform 0.2s;
}
.quick-card:hover {
  transform: translateY(-2px);
}
.quick-icon {
  font-size: 32px;
  color: #5c7c4a;
  display: block;
  margin-bottom: 10px;
}
.quick-title {
  font-size: 15px;
  font-weight: 500;
  color: #334155;
}
</style>
