import request from '@/utils/request'

export function getDictPage(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/dict/page',
    method: 'get',
    params: data
  })
}

export function updateDict(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/dict',
    method: 'put',
    data: data
  })
}

export function addDict(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/dict',
    method: 'post',
    data: data
  })
}

export function deleteDict(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/dict',
    method: 'delete',
    data: data
  })
}
