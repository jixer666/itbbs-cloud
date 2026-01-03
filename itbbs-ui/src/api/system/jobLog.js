import request from '@/utils/request'

export function getJobLogPage(data) {
    return request({
        url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/jobLog/page',
        method: 'get',
        params: data
    })
}

export function updateJobLog(data) {
    return request({
        url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/jobLog',
        method: 'put',
        data: data
    })
}

export function addJobLog(data) {
    return request({
        url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/jobLog',
        method: 'post',
        data: data
    })
}


export function deleteJobLog(data) {
    return request({
        url: process.env.VUE_APP_SYSTEM_BASE_API + '/system/jobLog',
        method: 'delete',
        data: data
    })
}
