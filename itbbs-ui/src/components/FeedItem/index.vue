<template>
  <el-card
    shadow="never"
    class="feed-item"
    body-style="padding: 5px 16px;"
  >
    <div class="feed-row">
      <div class="feed-left">
        <div class="feed-meta">
          <el-avatar :size="25" :src="item.userInfo.avatar" />
          <span class="author">{{ item.userInfo.nickname }}</span>
          <span class="dot">·</span>
          <span class="time">{{ formatTime(item.createTime) }}</span>
        </div>

        <div class="feed-title" @click="toArticlePage(item.htmlFilePath)">{{ item.title }}</div>
        <div class="feed-desc">{{ item.summary }}</div>

        <div class="feed-actions">
          <span class="action"><i class="el-icon-view" /> 阅读 {{ item.viewsCount }}</span>
          <span class="action"><i class="el-icon-thumb" /> 赞 {{ item.likeCount }}</span>
          <span class="action"><i class="el-icon-star-off" /> 收藏 {{ item.collectCount }}</span>
        </div>
      </div>

      <div class="feed-right">
        <div class="thumb" :style="{ backgroundImage: `url(${item.cover})` }" />
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'FeedItem',
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  methods: {
    toArticlePage(path) {
      location.href = path
    },
    formatTime(time) {
      const date = new Date(time)
      const now = new Date()
      const diffMs = now - date // 时间差（毫秒）

      // 计算秒、分钟、小时、天数
      const diffSeconds = Math.floor(diffMs / 1000)
      const diffMinutes = Math.floor(diffSeconds / 60)
      const diffHours = Math.floor(diffMinutes / 60)
      const diffDays = Math.floor(diffHours / 24)

      // 一天之内显示"xxx之前"
      if (diffHours < 24) {
        if (diffMinutes < 1) {
          return '刚刚'
        } else if (diffMinutes < 60) {
          return `${diffMinutes}分钟前`
        } else {
          return `${diffHours}小时前`
        }
      }
      // 一周之内显示"xxx天之前"
      else if (diffDays < 7) {
        return `${diffDays}天前`
      }
      // 否则显示日期
      else {
        // 格式化日期为 YYYY-MM-DD
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        return `${year}-${month}-${day}`
      }
    }
  }
}
</script>

<style scoped>
.feed-item {
  border-radius: 12px;
  border: 1px solid #eef0f5;
}
.feed-row {
  display: flex;
  gap: 14px;
  align-items: flex-start;
  border-bottom: 1px solid #eef0f5;
  padding-bottom: 10px;
}
.feed-left {
  flex: 1;
}
.feed-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #6b7280;
  font-size: 12px;
  margin-bottom: 8px;
}
.feed-meta .author {
  color: #2c3e50;
  font-weight: 600;
}
.feed-meta .dot {
  opacity: 0.7;
}
.feed-title {
  font-size: 18px;
  font-weight: 800;
  margin-bottom: 8px;
  line-height: 1.3;
}
.feed-desc {
  color: #606266;
  line-height: 1.6;
  font-size: 13px;
  margin-bottom: 12px;
}
.feed-actions {
  display: flex;
  gap: 14px;
  color: #909399;
  font-size: 12px;
}
.feed-actions .action i {
  margin-right: 4px;
}
.feed-right {
  width: 220px;
}
.thumb {
  width: 220px;
  height: 124px;
  border-radius: 10px;
  background-size: cover;
  background-position: center;
  border: 1px solid #eef0f5;
}
</style>
