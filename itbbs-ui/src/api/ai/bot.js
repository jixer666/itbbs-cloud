import request from '@/utils/requestAi'

export function chatAi(data) {
  return request({
    url: process.env.VUE_APP_AI_BASE_API + '/ai/bot/chat',
    method: 'post',
    data: data
  })
}

export function chatAiStream(data) {
  return request({
    url: process.env.VUE_APP_AI_BASE_API + '/ai/bot/chat/stream',
    method: 'post',
    data: data
  })
}
