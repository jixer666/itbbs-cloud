import request from '@/utils/request'

export function login(data) {
  return request({
    url: process.env.VUE_APP_AUTH_BASE_API + '/auth/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/user/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: process.env.VUE_APP_AUTH_BASE_API + '/auth/logout',
    method: 'post'
  })
}

export function getUserPage(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/user/page',
    method: 'get',
    params: data
  })
}

export function getUserRole(userId) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/user/getUserRole/' + userId,
    method: 'get'
  })
}

export function saveUserRole(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/user/saveUserRole',
    method: 'post',
    data
  })
}

export function resetPassword(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/user/resetPassword',
    method: 'put',
    data
  })
}

export function addUser(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/user',
    method: 'put',
    data
  })
}

