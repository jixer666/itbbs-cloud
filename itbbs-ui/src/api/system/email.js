import request from '@/utils/request'

export function sendRegisterEmail(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/sendRegisterEmail',
    method: 'post',
    data
  })
}
