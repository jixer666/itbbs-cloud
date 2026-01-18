<template>
  <div class="resource-hall-container">
    <!-- 主体内容 -->
    <el-container class="layout">
      <el-main class="main">
        <!-- 搜索和筛选工具栏 -->
        <div class="toolbar">
          <el-row :gutter="20">
            <el-col :span="16">
              <el-input 
                v-model="searchForm.keyword" 
                placeholder="搜索资源名称、描述..." 
                prefix-icon="el-icon-search"
                clearable
                @keyup.enter.native="handleSearch"
              />
            </el-col>
            <el-col :span="8">
              <el-select 
                v-model="searchForm.resourceType" 
                placeholder="资源类型" 
                clearable
                @change="handleSearch"
                style="width: 100%;"
              >
                <el-option label="文档" value="doc"></el-option>
                <el-option label="代码" value="code"></el-option>
                <el-option label="视频" value="video"></el-option>
                <el-option label="电子书" value="book"></el-option>
                <el-option label="源码" value="source"></el-option>
              </el-select>
            </el-col>
          </el-row>
          
          <div class="filter-options">
            <el-radio-group v-model="searchForm.sortType" size="small" @change="handleSortChange">
              <el-radio-button label="newest">最新</el-radio-button>
              <el-radio-button label="popular">热门</el-radio-button>
              <el-radio-button label="download">下载量</el-radio-button>
              <el-radio-button label="free">免费</el-radio-button>
            </el-radio-group>
          </div>
        </div>

        <!-- 资源列表 -->
        <ResourceList 
          :resources="resources" 
          :loading="loading"
          :hasMore="hasMore"
          @resource-click="viewResource"
          @load-more="loadMore"
        />
      </el-main>

      <!-- 右侧栏 -->
      <el-aside width="320px" class="aside">
        <!-- 热门资源榜 -->
        <el-card class="aside-card">
          <div slot="header" class="card-header">
            <span>热门资源榜</span>
          </div>
          <div 
            v-for="(hotRes, index) in hotResources" 
            :key="hotRes.id" 
            class="hot-item"
            @click="viewResource(hotRes)"
          >
            <span class="rank-num" :class="{ top: index < 3 }">{{ index + 1 }}</span>
            <div class="hot-info">
              <h4 class="hot-title">{{ hotRes.title }}</h4>
              <div class="hot-meta">
                <span class="hot-download"><i class="el-icon-download"></i> {{ hotRes.downloadCount }}</span>
                <span class="hot-price" v-if="hotRes.price > 0">¥{{ hotRes.price }}</span>
                <span class="hot-price free" v-else>免费</span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 推荐作者 -->
        <el-card class="aside-card">
          <div slot="header" class="card-header">
            <span>推荐作者</span>
          </div>
          <div 
            v-for="author in recommendAuthors" 
            :key="author.id" 
            class="author-item"
          >
            <el-avatar :size="40" :src="author.avatar"></el-avatar>
            <div class="author-info">
              <h4>{{ author.name }}</h4>
              <p>{{ author.desc }}</p>
            </div>
            <el-button size="mini" type="primary">关注</el-button>
          </div>
        </el-card>
      </el-aside>
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
    categories: {
      type: Array,
      default: () => [
        '全部', '后端开发', '前端开发', '移动开发', '人工智能', 
        '云计算', '数据库', '运维', '测试', 'UI设计', 
        '游戏开发', '物联网', '安全', '区块链', '其他'
      ]
    },
    resources: {
      type: Array,
      default: () => []
    },
    hotResources: {
      type: Array,
      default: () => []
    },
    recommendAuthors: {
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
    },
    searchForm: {
      type: Object,
      default: () => ({
        keyword: '',
        resourceType: '',
        sortType: 'newest',
        pageNum: 1,
        pageSize: 16,
        total: 0
      })
    }
  },

  methods: {
    /**
     * 处理分类切换
     */
    handleCategoryChange(category) {
      this.$emit('category-change', category)
    },

    /**
     * 处理搜索
     */
    handleSearch() {
      this.$emit('search')
    },

    /**
     * 处理排序变化
     */
    handleSortChange() {
      this.$emit('sort-change')
    },

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
  background-color: #f5f7fa;
}

.layout {
  margin: 14px auto;
  padding: 0 12px;
  max-width: 1400px;
}

.main {
  padding: 0 12px 0 0 !important;
}

.aside {
  padding: 0 0 0 12px;
}

.toolbar {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.filter-options {
  margin-top: 15px;
}

.aside-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.aside-card:last-child {
  margin-bottom: 0;
}

.card-header {
  font-weight: 600;
  font-size: 16px;
}

.hot-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f4f4f5;
  cursor: pointer;
}

.hot-item:last-child {
  border-bottom: none;
}

.hot-item:hover {
  background-color: #f5f7fa;
}

.rank-num {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f2f5;
  border-radius: 50%;
  font-size: 12px;
  margin-right: 12px;
  color: #909399;
}

.rank-num.top {
  background-color: #f56c6c;
  color: white;
}

.hot-info {
  flex: 1;
}

.hot-title {
  margin: 0 0 5px;
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.hot-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.hot-download {
  display: flex;
  align-items: center;
  gap: 2px;
}

.hot-price {
  font-weight: bold;
}

.hot-price.free {
  color: #67c23a;
}

.author-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  gap: 12px;
}

.author-info {
  flex: 1;
}

.author-info h4 {
  margin: 0 0 4px;
  font-size: 14px;
  font-weight: 500;
}

.author-info p {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 1080px) {
  .aside {
    display: none;
  }
  
  .main {
    padding-right: 0 !important;
  }
  
  .layout {
    padding: 0 8px;
  }
}
</style>