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
    
    <!-- 加载更多按钮 -->
    <div class="load-more" v-if="hasMore">
      <el-button 
        type="primary" 
        plain 
        @click="loadMore"
        :loading="loading"
      >
        {{ loading ? '加载中...' : '加载更多' }}
      </el-button>
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

  methods: {
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
      this.$emit('load-more')
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

.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60px 0;
}
</style>