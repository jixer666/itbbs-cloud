import request from '@/utils/request'

export function getFilePage(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/file/page',
    method: 'get',
    params: data
  })
}

export function updateFile(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/file',
    method: 'put',
    data: data
  })
}

export function addFile(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/file',
    method: 'post',
    data: data
  })
}

export function deleteFile(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/file',
    method: 'delete',
    data: data
  })
}
