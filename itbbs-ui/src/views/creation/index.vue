<template>
  <div class="creation-page">
    <TencentDocEditor
      ref="editorRef"
      :title="pageTitle"
      :content="html"
      :cover="articleSettings.cover"
      :article-data="articleSettings"
      :common-tags="commonTags"
      :categories="categories"
      @publish="handlePublish"
      @saveDraft="handleSaveDraft"
      @change="handleContentChange"
      @settingsChange="handleSettingsChange"
    />
  </div>
</template>

<script>
import TencentDocEditor from '@/components/Editor/index.vue'
import { addArticleDraft, updateArticle } from '@/api/blog/article'
import { getCategoryPage } from '@/api/blog/category'

export default {
  name: 'CreationPage',
  components: {
    TencentDocEditor
  },
  dicts: ['blog_article_visible_range', 'blog_article_creation_statement', 'blog_article_type'],
  data() {
    return {
      // 页面标题
      pageTitle: '',

      // 编辑器内容
      html: '',

      // 文章设置
      articleSettings: {
        tags: [],
        category: '',
        summary: '',
        cover: '',
        articleType: 'original',
        reprintStatement: '',
        visibility: 'public',
        creationStatement: [],
        status: null,
        articleId: null
      },

      // 常用标签
      commonTags: [
        { value: '技术', label: '技术' },
        { value: '生活', label: '生活' },
        { value: '娱乐', label: '娱乐' },
        { value: '教育', label: '教育' },
        { value: '健康', label: '健康' }
      ],

      // 分类
      categories: []
    }
  },

  mounted() {
    this.loadArticle()
    // 从API加载分类数据
    this.loadCategories()
  },

  beforeDestroy() {

  },

  methods: {
    loadArticle() {
      const articleId = this.$route.query.articleId || localStorage.getItem('creation_article_draft_id')
      if (articleId) {
        // 调用API获取文章数据
        this.getArticle(articleId)
      } else {
        // 获取文章草稿数据
        this.getArticleDraft()
        // 如果没有文章ID，则重置编辑器
        this.resetEditor()
      }
    },

    getArticleDraft() {
      // 调用API获取文章草稿数据
      addArticleDraft().then(res => {
        this.articleSettings.articleId = res.data.articleId
      })
    },

    /**
     * 加载分类数据
     */
    loadCategories() {
      // 调用API获取分类列表
      getCategoryPage({}).then(res => {
        // 将返回的分类数据转换为下拉框需要的格式
        this.categories = res.data.list.map(category => {
          return {
            value: category.categoryId,
            label: category.categoryName
          }
        })
      })
    },

    /**
     * 处理发布事件
     */
    handlePublish(articleData) {
      articleData.status = 2
      articleData.articleId = this.articleSettings.articleId
      updateArticle(articleData).then(res => {
        this.$message.success('发布成功')
        localStorage.removeItem('creation_article_draft_id')
      })
    },

    /**
     * 处理保存草稿事件
     */
    handleSaveDraft(draftData) {
      draftData.stauts = 1
      updateArticle(draftData).then(res => {
        this.$message.success('保存成功')
      })
    },

    /**
     * 处理内容变化事件
     */
    handleContentChange(content) {
      this.html = content

      // 可以在这里实现自动保存功能
      // this.autoSave();
    },

    /**
     * 处理设置变化事件
     */
    handleSettingsChange(settings) {
      this.articleSettings = { ...this.articleSettings, ...settings }
    },

    /**
     * 获取编辑器内容
     */
    getEditorContent() {
      if (this.$refs.editorRef && this.$refs.editorRef.getEditorContent) {
        return this.$refs.editorRef.getEditorContent()
      }
      return null
    },

    /**
     * 重置编辑器
     */
    resetEditor(title = '', content = '') {
      if (this.$refs.editorRef && this.$refs.editorRef.resetEditor) {
        this.$refs.editorRef.resetEditor(title, content)
      }
    },

    /**
     * 自动保存功能
     */
    autoSave() {
      // 实现自动保存逻辑
      const content = this.getEditorContent()
      if (content) {
        console.log('自动保存内容:', content)
        // 可以调用API保存到后端
      }
    }
  }
}
</script>

<style scoped>
.creation-page {
  background-color: #f5f5f5;
  overflow-y: hidden;
}
</style>
