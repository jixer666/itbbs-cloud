<template>
  <div class="resource-list-container">
    <el-row :gutter="20">
      <el-col 
        :xs="24" 
        :sm="12" 
        :md="8" 
        :lg="6" 
        v-for="(resource, index) in resources" 
        :key="resource.id || index"
        class="resource-item-wrapper"
      >
        <ResourceItem 
          :resource="resource" 
          @click="handleResourceClick"
        />
      </el-col>
    </el-row>
    
    <!-- 加载更多提示，滚动到底部自动加载 -->
    <div class="load-more" v-if="hasMore || loading">
      <div v-loading="loading" class="infinite-loading-tips">
        <span v-if="!loading">正在为您加载更多资源...</span>
        <span v-else>&nbsp;</span>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-if="resources.length === 0" class="empty-container">
      <el-empty description="暂无资源数据"></el-empty>
    </div>
  </div>
</template>

<script>
import ResourceItem from '@/components/ResourceItem'

export default {
  name: 'ResourceList',
  components: {
    ResourceItem
  },
  
  props: {
    resources: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    },
    hasMore: {
      type: Boolean,
      default: true
    }
  },

  mounted() {
    // 监听滚动事件
    window.addEventListener('scroll', this.handleScroll, true);
  },
  
  beforeDestroy() {
    // 移除滚动事件监听
    window.removeEventListener('scroll', this.handleScroll, true);
  },
  
  methods: {
    /**
     * 处理滚动事件
     */
    handleScroll() {
      // 如果正在加载或没有更多数据，则不处理
      if (this.loading || !this.hasMore) return;
      
      // 计算页面滚动到底部的距离
      const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      const windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
      const scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
      
      // 当距离底部小于500px时触发加载
      if (scrollHeight - scrollTop - windowHeight < 500) {
        this.loadMore();
      }
    },
    
    /**
     * 处理资源点击事件
     */
    handleResourceClick(resource) {
      this.$emit('resource-click', resource)
    },
    
    /**
     * 加载更多
     */
    loadMore() {
      if (!this.loading && this.hasMore) {
        this.$emit('load-more');
      }
    }
  }
}
</script>

<style scoped>
.resource-item-wrapper {
  margin-bottom: 20px;
}

.load-more {
  text-align: center;
  margin: 30px 0;
}

.infinite-loading-tips {
  padding: 20px;
  text-align: center;
  color: #999;
  font-size: 14px;
}

.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60px 0;
}
</style>