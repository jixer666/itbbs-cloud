<template>
  <el-card shadow="never" class="side-card">
    <div slot="header" class="side-header">
      <span>官方博客</span>
    </div>
    <div class="official">
      <div v-for="b in official" :key="b" class="official-item">{{ b }}</div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'OfficialBlog',
  props: {
    official: {
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
.official {
  display: flex;
  flex-direction: column;
  gap: 10px;
  color: #606266;
}
.official-item {
  padding: 10px 12px;
  border: 1px dashed #eef0f5;
  border-radius: 10px;
  background: #fafbff;
}
</style>
