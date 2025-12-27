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
          <FeedItem
            v-for="item in feed"
            :key="item.id"
            :item="item"
          />
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
      feed: [
        {
          id: 1,
          author: '汽车电子实验室',
          authorAvatar: 'https://dummyimage.com/40x40/9bd/fff.png&text=A',
          time: '刚刚',
          title: '电子电气架构 —— 新能源汽车领域有哪些新技术（下）',
          desc: '从域控到中央计算平台，围绕整车以太网、服务化架构、OTA、安全与功能升级做一次梳理……',
          views: '41',
          likes: '0',
          favs: '0',
          thumb: 'https://dummyimage.com/260x160/ddd/666.png&text=EV'
        },
        {
          id: 2,
          author: 'OpenTiny社区',
          authorAvatar: 'https://dummyimage.com/40x40/7c9/fff.png&text=O',
          time: '2小时前',
          title: 'Vue2/Vue3 迁移头秃？Renderless 架构让组件“无缝穿梭”',
          desc: '问题来了：这个组件只能在 Vue3 中使用？如何同时支持 Vue2 和 Vue3？',
          views: '608',
          likes: '17',
          favs: '18',
          thumb: 'https://dummyimage.com/260x160/f2b/fff.png&text=Vue'
        },
        {
          id: 3,
          author: '码界奇点',
          authorAvatar: 'https://dummyimage.com/40x40/f99/fff.png&text=M',
          time: '昨天',
          title: '基于 Spring Boot 的后台管理系统设计与实现',
          desc: '一个采用 Java 语言开发的现代企业级后台管理平台，重点包括：登录鉴权、权限模型、菜单路由、系统监控……',
          views: '1.0k',
          likes: '8',
          favs: '24',
          thumb: 'https://dummyimage.com/260x160/cde/345.png&text=Boot'
        },
        {
          id: 4,
          author: '代码方舟',
          authorAvatar: 'https://dummyimage.com/40x40/6aa/fff.png&text=J',
          time: '3天前',
          title: 'Java 进阶：基于 Spring Boot 集成云端数据人脸比对 V3 的最佳实践',
          desc: '企业级应用常见的数据加密与身份验证需求，如何安全对接远程 API、处理回调、重试与幂等……',
          views: '805',
          likes: '20',
          favs: '13',
          thumb: 'https://dummyimage.com/260x160/ddd/666.png&text=API'
        }
      ],
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
