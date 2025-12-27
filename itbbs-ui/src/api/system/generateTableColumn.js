import request from '@/utils/request'

export function getGenerateTableColumnPage(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/generateTableColumn/page',
    method: 'get',
    params: data
  })
}

export function updateGenerateTableColumn(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/generateTableColumn',
    method: 'put',
    data: data
  })
}

export function addGenerateTableColumn(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/generateTableColumn',
    method: 'post',
    data: data
  })
}

export function deleteGenerateTableColumn(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/generateTableColumn',
    method: 'delete',
    data: data
  })
}

export function getGenerateTableColumnDbPage(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/generateTableColumn/db/page',
    method: 'get',
    params: data
  })
}
