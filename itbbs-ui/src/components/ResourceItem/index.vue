<template>
  <div class="resource-item-card">
    <div class="card-content">
      <div class="resource-header">
        <div class="resource-type" :class="getResourceTypeClass(resource.type)">
          {{ getResourceTypeName(resource.type) }}
        </div>
        <div class="resource-price" v-if="resource.price > 0">
          ¥{{ resource.price }}
        </div>
        <div class="resource-price free" v-else>
          免费
        </div>
      </div>

      <div class="resource-body">
        <div class="resource-cover">
          <img :src="resource.cover || getDefaultCover(resource.type)" alt="资源封面" />
        </div>
        
        <div class="resource-info">
          <h3 class="resource-title" @click="handleClick">{{ resource.title }}</h3>
          <p class="resource-desc">{{ resource.description }}</p>
          
          <div class="resource-meta">
            <div class="meta-item">
              <i class="el-icon-download"></i>
              {{ resource.downloadCount }} 下载
            </div>
            <div class="meta-item">
              <i class="el-icon-star-off"></i>
              {{ resource.starCount }} 收藏
            </div>
          </div>
          
          <div class="resource-author">
            <el-avatar size="small" :src="resource.author.avatar"></el-avatar>
            <span class="author-name">{{ resource.author.name }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ResourceItem',
  props: {
    resource: {
      type: Object,
      required: true
    }
  },

  methods: {
    /**
     * 获取资源类型名称
     */
    getResourceTypeName(type) {
      const names = {
        doc: '文档',
        code: '代码',
        video: '视频',
        book: '电子书',
        source: '源码'
      }
      return names[type] || type
    },

    /**
     * 获取资源类型样式类
     */
    getResourceTypeClass(type) {
      return `type-${type}`
    },

    /**
     * 获取默认封面
     */
    getDefaultCover(type) {
      const covers = {
        doc: 'https://dummyimage.com/300x200/4a90e2/fff.png&text=文',
        code: 'https://dummyimage.com/300x200/50e3c2/fff.png&text=代',
        video: 'https://dummyimage.com/300x200/e6745a/fff.png&text=视',
        book: 'https://dummyimage.com/300x200/9013fe/fff.png&text=电',
      }
      return covers[type] || covers.doc
    },

    /**
     * 处理点击事件
     */
    handleClick() {
      this.$emit('click', this.resource)
    }
  }
}
</script>

<style scoped>
.resource-item-card {
  transition: all 0.3s ease;
  border-radius: 10px;
  overflow: hidden;
  height: 320px;
  display: flex;
  flex-direction: column;
  background: #fff;
  border: 1px solid #ebeef5;
}

.resource-item-card:hover {
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}

.card-content {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.resource-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px 5px;
}

.resource-type {
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 12px;
  color: #fff;
}

.type-doc { background-color: #4a90e2; }
.type-code { background-color: #50e3c2; }
.type-video { background-color: #e6745a; }
.type-book { background-color: #9013fe; }
.type-source { background-color: #7ed321; }

.resource-price {
  font-size: 14px;
  font-weight: bold;
}

.resource-price.free {
  color: #67c23a;
}

.resource-body {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.resource-cover {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
  background-color: #ffffff;
}

.resource-cover img {
  max-width: 100%;
  max-height: 120px;
  border-radius: 4px;
}

.resource-info {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.resource-title {
  cursor: pointer;
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.resource-title:hover {
  color: #409eff;
}

.resource-desc {
  margin: 0 0 12px;
  font-size: 13px;
  color: #909399;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.resource-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 12px;
  color: #909399;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.resource-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-name {
  font-size: 13px;
  color: #606266;
}
</style>