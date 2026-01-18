<template>
  <div class="resource-hall-container">
    <!-- 主体内容 -->
    <el-container class="layout">
      <el-main class="main">
        <!-- 资源列表 -->
        <ResourceList 
          :resources="resources" 
          :loading="loading"
          :hasMore="hasMore"
          @resource-click="viewResource"
          @load-more="loadMore"
        />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import ResourceList from '@/components/ResourceList'

export default {
  name: 'ResourceHallContainer',
  components: {
    ResourceList
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
     * 查看资源详情
     */
    viewResource(resource) {
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
.resource-hall-container {
  min-height: 100vh;
  background-color: #ffffff;
}

.layout {
  margin: 14px auto;
  max-width: 1400px;
}

.main {
  padding: 0 12px !important;
}

</style>