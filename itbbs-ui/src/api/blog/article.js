import request from '@/utils/request'

export function getArticlePage(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/article/page',
    method: 'get',
    params: data
  })
}

export function updateArticle(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/article',
    method: 'put',
    data: data
  })
}

export function addArticleDraft(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/article/draft',
    method: 'post',
    data: data
  })
}

export function deleteArticle(data) {
  return request({
    url: process.env.VUE_APP_BLOG_BASE_API + '/blog/article',
    method: 'delete',
    data: data
  })
}
