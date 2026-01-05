import request from '@/utils/request'

export function getLikeRecordPage(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/likeRecord/page',
    method: 'get',
    params: data
  })
}

export function updateLikeRecord(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/likeRecord',
    method: 'put',
    data: data
  })
}

export function addLikeRecord(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/likeRecord',
    method: 'post',
    data: data
  })
}

export function deleteLikeRecord(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/likeRecord',
    method: 'delete',
    data: data
  })
}
