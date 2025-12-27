import request from '@/utils/request'

export function getDictDataPage(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/dictData/page',
    method: 'get',
    params: data
  })
}

export function updateDictData(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/dictData',
    method: 'put',
    data: data
  })
}

export function addDictData(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/dictData',
    method: 'post',
    data: data
  })
}

export function deleteDictData(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/dictData',
    method: 'delete',
    data: data
  })
}

export function getDictDataByDictKey(dictKey) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/dictData/key/' + dictKey,
    method: 'get'
  })
}
