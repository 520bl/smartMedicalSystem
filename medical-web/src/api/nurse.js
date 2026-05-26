import request from '@/utils/request'

export function getNurseDashboardStats() {
  return request({
    url: '/nurse/dashboard/stats',
    method: 'get'
  })
}
