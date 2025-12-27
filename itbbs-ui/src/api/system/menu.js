import request from '@/utils/request'

export function getMenuPage(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/menu/page',
    method: 'get',
    params: data
  })
}

export function updateMenu(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/menu',
    method: 'put',
    data: data
  })
}

export function addMenu(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/menu',
    method: 'post',
    data: data
  })
}

export function deleteMenu(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/menu',
    method: 'delete',
    data: data
  })
}
