import request from '@/utils/request'

export function getCollectRecordPage(data) {
    return request({
        url: process.env.VUE_APP_BLOG_BASE_API + '/blog/collectRecord/page',
        method: 'get',
        params: data
    })
}

export function updateCollectRecord(data) {
    return request({
        url: process.env.VUE_APP_BLOG_BASE_API + '/blog/collectRecord',
        method: 'put',
        data: data
    })
}

export function addCollectRecord(data) {
    return request({
        url: process.env.VUE_APP_BLOG_BASE_API + '/blog/collectRecord',
        method: 'post',
        data: data
    })
}


export function deleteCollectRecord(data) {
    return request({
        url: process.env.VUE_APP_BLOG_BASE_API + '/blog/collectRecord',
        method: 'delete',
        data: data
    })
}
