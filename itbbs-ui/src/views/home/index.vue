<template>
  <div class="page">
    <!-- 二级分类导航 -->
    <CategoryChips :categories="categories" />

    <!-- 主体 -->
    <el-container class="layout">
      <el-main class="main">
        <!-- 广告横幅 -->
        <Banner />

        <!-- 内容列表 -->
        <div class="feed">
          <div v-if="feed && feed.length > 0">
            <FeedItem
              v-for="(item, index) in feed"
              :key="index"
              :item="item"
            />
          </div>
          <div v-else class="empty-container">
            暂无数据
            <!-- <el-empty description="暂无数据"></el-empty> -->
          </div>
        </div>
      </el-main>

      <!-- 右侧栏 -->
      <el-aside width="320px" class="aside">
        <AuthorRecommend :authors="authors" />
        <OfficialBlog :official="official" />
      </el-aside>
    </el-container>
  </div>
</template>

<script>
import CategoryChips from '@/components/CategoryChips'
import Banner from '@/components/Banner'
import FeedItem from '@/components/FeedItem'
import AuthorRecommend from '@/components/AuthorRecommend'
import OfficialBlog from '@/components/OfficialBlog'
import { getArticlePage } from '@/api/blog/article'

export default {
  name: 'Home',
  components: {
    CategoryChips,
    Banner,
    FeedItem,
    AuthorRecommend,
    OfficialBlog
  },

  data() {
    return {
      keyword: 'jenkins springboot',
      activeNav: 'recommend',
      avatarUrl:
        'https://dummyimage.com/80x80/ffd27a/333.png&text=U',
      categories: [
        '全部',
        '后端',
        '前端',
        '移动开发',
        'Java',
        'Python',
        '人工智能',
        'AIGC',
        '大数据',
        '数据库',
        '云原生',
        '运维',
        '服务器',
        '操作系统',
        '硬件开发'
      ],
      feed: [],
      // 分页相关
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false,
      hasMore: true, // 是否还有更多数据
      authors: [
        {
          id: 1,
          name: '人工智能博士',
          desc: '王博Kings，985AI博士在读',
          avatar: 'https://dummyimage.com/80x80/8ad/fff.png&text=AI'
        },
        {
          id: 2,
          name: '在奋斗的大道',
          desc: '程序员成长之路',
          avatar: 'https://dummyimage.com/80x80/f8a/fff.png&text=Dev'
        },
        {
          id: 3,
          name: 'xiangzhihong8',
          desc: '《React Native移动开发实战》作者',
          avatar: 'https://dummyimage.com/80x80/9c9/fff.png&text=RN'
        },
        {
          id: 4,
          name: '算法与编程之美',
          desc: 'CSDN官方认证博客专家',
          avatar: 'https://dummyimage.com/80x80/fcc/333.png&text=Algo'
        }
      ],
      official: ['CSDN 官方账号入驻', '技术博客', '社区精选', '活动中心']
    }
  },

  // 监听滚动事件
  mounted() {
    // 初始化加载数据
    this.loadArticles()

    // 添加滚动事件监听
    window.addEventListener('scroll', this.handleScroll)
  },

  // 组件销毁前移除事件监听
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll)
  },

  methods: {
    /**
     * 加载文章列表
     */
    loadArticles() {
      if (this.loading || !this.hasMore) return

      this.loading = true

      getArticlePage({
        pageNum: this.pagination.current,
        pageSize: this.pagination.size,
        status: 3
      }).then(res => {
        const articles = res.data.list

        this.feed = [...this.feed, ...articles]

        // 更新分页信息
        this.pagination.total = res.data.total
        this.pagination.current++

        // 检查是否还有更多数据
        this.hasMore = this.feed.length < res.data.total
      }).catch(error => {
        console.error('获取文章列表异常:', error)
      }).finally(() => {
        this.loading = false
      })
    },

    /**
     * 处理滚动事件
     */
    handleScroll() {
      // 计算页面滚动到底部的距离
      const scrollTop = document.documentElement.scrollTop || document.body.scrollTop
      const windowHeight = document.documentElement.clientHeight || document.body.clientHeight
      const scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight

      // 当距离底部小于100px时加载更多
      if (scrollHeight - scrollTop - windowHeight < 100) {
        if (!this.loading && this.hasMore) {
          this.loadArticles()
        }
      }
    },

    /**
     * 格式化时间显示
     */
    formatTime(dateString) {
      if (!dateString) return '刚刚'

      const date = new Date(dateString)
      const now = new Date()
      const diffMs = now - date
      const diffMinutes = Math.floor(diffMs / 60000)
      const diffHours = Math.floor(diffMs / 3600000)
      const diffDays = Math.floor(diffMs / 86400000)

      if (diffMinutes < 1) {
        return '刚刚'
      } else if (diffHours < 1) {
        return `${diffMinutes}分钟前`
      } else if (diffDays < 1) {
        return `${diffHours}小时前`
      } else if (diffDays < 7) {
        return `${diffDays}天前`
      } else {
        return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
      }
    }
  }
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  color: #1f2329;
}

/* 顶部 */
.topbar {
  position: sticky;
  top: 0;
  z-index: 50;
  background: #fff;
  border-bottom: 1px solid #eef0f5;
}
.topbar-inner {
  margin: 0 auto;
  padding: 10px 12px;
  display: flex;
  align-items: center;
  gap: 12px;
}
.brand {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 140px;
}
.logo-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #ff4d4f;
}
.brand-text {
  font-weight: 700;
  letter-spacing: 0.2px;
}
.search {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
}
.search-input {
  max-width: 520px;
}
.btn-search {
  border-radius: 18px;
  padding: 10px 16px;
}
.btn-ai {
  border-radius: 18px;
  padding: 10px 16px;
}
.top-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}
.link-btn {
  color: #303133;
}
.create-btn {
  border-radius: 18px;
  padding: 10px 14px;
}
.avatar {
  border: 1px solid #f0f0f0;
}

/* 主体两栏 */
.layout {
  margin: 14px auto 28px;
  padding: 0 12px;
}
.main {
  padding: 0 12px 0 0 !important;
}
.aside {
  padding: 0 0 0 12px;
  background: #fff;
}

/* Feed */
.feed {
  display: flex;
  flex-direction: column;
}

.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 0;
}

::v-deep .el-card {
    border: none !important;
}

/* 响应式 */
@media (max-width: 1080px) {
  .aside {
    display: none;
  }
  .main {
    padding-right: 0 !important;
  }
  .feed-right {
    display: none;
  }
}

</style>
