<template>
  <el-card shadow="never" class="side-card">
    <div slot="header" class="side-header">
      <span>作者推荐</span>
    </div>

    <div class="author-list">
      <div v-for="a in authors" :key="a.id" class="author-item">
        <div class="author-info">
          <el-avatar :size="36" :src="a.avatar" />
          <div class="author-text">
            <div class="author-name">{{ a.name }}</div>
            <div class="author-desc">{{ a.desc }}</div>
          </div>
        </div>
        <el-button size="mini" plain type="primary">关注</el-button>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'AuthorRecommend',
  props: {
    authors: {
      type: Array,
      default: () => []
    }
  }
}
</script>

<style scoped>
.side-card {
  border-radius: 12px;
  border: 1px solid #eef0f5;
  margin-bottom: 12px;
}
.side-header {
  font-size: 18px;
  color: #000;
  font-weight: 600;
}
.author-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.author-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}
.author-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
.author-text {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.author-name {
  font-weight: 700;
  font-size: 13px;
}
.author-desc {
  color: #8a8f99;
  font-size: 12px;
}
</style>
