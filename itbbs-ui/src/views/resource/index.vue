<template>
  <ResourceHall 
    :categories="categories"
    :resources="resources"
    :hotResources="hotResources"
    :recommendAuthors="recommendAuthors"
    :loading="loading"
    :hasMore="hasMore"
    :searchForm="searchForm"
    @category-change="handleCategoryChange"
    @search="handleSearch"
    @sort-change="handleSortChange"
    @resource-click="viewResource"
    @load-more="loadMore"
  />
</template>

<script>
import ResourceHall from '@/components/ResourceHall'
import { getFilePage } from '@/api/system/file'

export default {
  name: 'ResourceHallPage',
  components: {
    ResourceHall
  },

  data() {
    return {
      categories: [
        '全部', '后端开发', '前端开发', '移动开发', '人工智能', 
        '云计算', '数据库', '运维', '测试', 'UI设计', 
        '游戏开发', '物联网', '安全', '区块链', '其他'
      ],
      resources: [],
      hotResources: [],
      recommendAuthors: [],
      searchForm: {
        keyword: '',
        resourceType: '',
        sortType: 'newest',
        pageNum: 1,
        pageSize: 16,
        total: 0
      },
      loading: false,
      hasMore: true,
      currentCategory: '全部'
    }
  },

  mounted() {
    this.loadResources()
    this.loadHotResources()
    this.loadRecommendAuthors()
  },

  methods: {
    /**
     * 加载资源列表
     */
    loadResources() {
      if (this.loading || !this.hasMore) return

      this.loading = true

      // 这里应该调用实际的资源API，目前使用模拟数据
      // 在实际项目中，这里应该是真实的API调用
      setTimeout(() => {
        const mockData = this.generateMockData(this.searchForm.pageNum)
        
        if (this.searchForm.pageNum === 1) {
          this.resources = mockData
        } else {
          this.resources = [...this.resources, ...mockData]
        }

        // 模拟总数量
        this.searchForm.total = 100
        this.searchForm.pageNum++
        
        this.hasMore = this.resources.length < this.searchForm.total
        
        this.loading = false
      }, 500)
    },

    /**
     * 生成模拟数据
     */
    generateMockData(pageNum) {
      const types = ['doc', 'code', 'video', 'book', 'source']
      const typeNames = ['文档', '代码', '视频', '电子书', '源码']
      
      return Array.from({ length: 16 }, (_, index) => {
        const idx = (pageNum - 1) * 16 + index
        const typeIndex = idx % types.length
        
        return {
          id: idx + 1,
          title: `精品${typeNames[typeIndex]}资源-${idx + 1}`,
          description: `这是一个高质量的${typeNames[typeIndex]}资源，适用于学习和开发。包含了详细的内容说明和实用的示例代码。`,
          type: types[typeIndex],
          cover: this.getDefaultCover(types[typeIndex]),
          price: idx % 5 === 0 ? 0 : Math.floor(Math.random() * 100),
          downloadCount: Math.floor(Math.random() * 10000),
          starCount: Math.floor(Math.random() * 1000),
          author: {
            name: `作者${idx + 1}`,
            avatar: `https://dummyimage.com/40x40/409eff/fff.png&text=A${(idx % 10) + 1}`
          }
        }
      })
    },

    /**
     * 获取默认封面
     */
    getDefaultCover(type) {
      const covers = {
        doc: 'https://dummyimage.com/300x200/4a90e2/fff.png&text=文档',
        code: 'https://dummyimage.com/300x200/50e3c2/fff.png&text=代码',
        video: 'https://dummyimage.com/300x200/e6745a/fff.png&text=视频',
        book: 'https://dummyimage.com/300x200/9013fe/fff.png&text=电子书',
        source: 'https://dummyimage.com/300x200/7ed321/fff.png&text=源码'
      }
      return covers[type] || covers.doc
    },

    /**
     * 查看资源详情
     */
    viewResource(resource) {
      this.$router.push(`/resource/detail/${resource.id}`)
    },

    /**
     * 处理分类切换
     */
    handleCategoryChange(category) {
      this.currentCategory = category
      this.resetPagination()
      this.loadResources()
    },

    /**
     * 处理搜索
     */
    handleSearch() {
      this.resetPagination()
      this.loadResources()
    },

    /**
     * 处理排序变化
     */
    handleSortChange() {
      this.resetPagination()
      this.loadResources()
    },

    /**
     * 加载更多
     */
    loadMore() {
      this.loadResources()
    },

    /**
     * 重置分页
     */
    resetPagination() {
      this.resources = []
      this.searchForm.pageNum = 1
      this.hasMore = true
    },

    /**
     * 加载热门资源
     */
    loadHotResources() {
      // 模拟热门资源数据
      this.hotResources = [
        { id: 1, title: 'Spring Boot实战开发指南', downloadCount: 12500, price: 29.9 },
        { id: 2, title: 'Vue.js从入门到精通', downloadCount: 9800, price: 0 },
        { id: 3, title: '微服务架构设计模式', downloadCount: 8700, price: 49.9 },
        { id: 4, title: '人工智能算法详解', downloadCount: 7600, price: 0 },
        { id: 5, title: 'React Native跨平台开发', downloadCount: 6500, price: 39.9 }
      ]
    },

    /**
     * 加载推荐作者
     */
    loadRecommendAuthors() {
      // 模拟推荐作者数据
      this.recommendAuthors = [
        { id: 1, name: '技术专家张三', desc: '全栈开发工程师', avatar: 'https://dummyimage.com/40x40/409eff/fff.png&text=张' },
        { id: 2, name: '前端大神李四', desc: 'Vue.js技术专家', avatar: 'https://dummyimage.com/40x40/50e3c2/fff.png&text=李' },
        { id: 3, name: 'AI研究员王五', desc: '机器学习专家', avatar: 'https://dummyimage.com/40x40/e6745a/fff.png&text=王' },
        { id: 4, name: '架构师赵六', desc: '分布式系统专家', avatar: 'https://dummyimage.com/40x40/9013fe/fff.png&text=赵' }
      ]
    }
  }
}
</script>

<style scoped>
.resource-hall {
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