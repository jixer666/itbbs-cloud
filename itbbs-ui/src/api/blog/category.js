import request from '@/utils/request'

export function getCategoryPage(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/category/page',
    method: 'get',
    params: data
  })
}

export function updateCategory(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/category',
    method: 'put',
    data: data
  })
}

export function addCategory(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/category',
    method: 'post',
    data: data
  })
}

export function deleteCategory(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/category',
    method: 'delete',
    data: data
  })
}
