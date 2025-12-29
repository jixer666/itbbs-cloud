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
        creationStatement: []
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
      categories: [
        { value: '前端', label: '前端' },
        { value: '后端', label: '后端' },
        { value: '设计', label: '设计' },
        { value: '产品', label: '产品' },
        { value: '运营', label: '运营' }
      ]
    }
  },

  mounted() {
    // 页面挂载后可以初始化一些数据
    console.log('创作页面已挂载')

    // 可以从路由参数或API加载已有文章数据
    // this.loadArticleData();
  },

  beforeDestroy() {
    // 页面销毁前可以执行清理操作
    console.log('创作页面即将销毁')
  },

  methods: {
    /**
     * 处理发布事件
     */
    handlePublish(articleData) {
      console.log('发布文章:', articleData)

      // 这里可以添加实际的发布逻辑
      this.$message.success('文章发布成功')

      // 发布成功后可以跳转到其他页面
      // this.$router.push("/");
    },

    /**
     * 处理保存草稿事件
     */
    handleSaveDraft(draftData) {
      console.log('保存草稿:', draftData)

      // 这里可以添加实际的保存草稿逻辑
      this.$message.success('草稿保存成功')
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
      console.log('设置变化:', settings)
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
