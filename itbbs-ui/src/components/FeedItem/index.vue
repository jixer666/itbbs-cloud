<template>
  <el-card
    shadow="never"
    class="feed-item"
    body-style="padding: 5px 16px;"
  >
    <div class="feed-row">
      <div class="feed-left">
        <div class="feed-meta">
          <el-avatar :size="18" :src="item.authorAvatar" />
          <span class="author">{{ item.author }}</span>
          <span class="dot">·</span>
          <span class="time">{{ item.time }}</span>
        </div>

        <div class="feed-title">{{ item.title }}</div>
        <div class="feed-desc">{{ item.desc }}</div>

        <div class="feed-actions">
          <span class="action"><i class="el-icon-view" /> 阅读 {{ item.views }}</span>
          <span class="action"><i class="el-icon-thumb" /> 赞 {{ item.likes }}</span>
          <span class="action"><i class="el-icon-star-off" /> 收藏 {{ item.favs }}</span>
        </div>
      </div>

      <div class="feed-right">
        <div class="thumb" :style="{ backgroundImage: `url(${item.thumb})` }" />
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
