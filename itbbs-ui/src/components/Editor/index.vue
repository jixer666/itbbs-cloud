<template>
  <div class="wangeditor-tencent-doc">
    <!-- 工具栏区域 -->
    <div style="border-bottom: 1px solid #e8e8e8; box-sizing: border-box">
      <div id="editor-toolbar">
        <!-- Vue组件化工具栏 -->
        <Toolbar :editor="editor" :default-config="toolbarConfig" :mode="mode" />
      </div>
    </div>

    <!-- 内容区域 -->
    <div id="content">
      <div id="editor-container-wrapper">
        <div id="editor-container">
          <!-- 标题输入 -->
          <div id="title-container">
            <el-input
              v-model="pageTitle"
              :placeholder="titlePlaceholder"
              @keyup.enter="focusEditor"
            />
          </div>

          <!-- 编辑器区域 -->
          <div id="editor-text-area" @click="handleEditorClick">
            <!-- Vue组件化编辑器 -->
            <Editor
              ref="editorRef"
              v-model="html"
              :default-config="editorConfig"
              :mode="mode"
              @onCreated="onCreated"
              @onChange="onChange"
              @onBlur="onBlur"
              @onFocus="onFocus"
            />
          </div>

          <!-- 目录区域 -->
          <div id="toc-container">
            <div class="toc-title">目录</div>
            <div class="toc-content">
              <ul>
                <template v-if="tocList.length > 0">
                  <li
                    v-for="item in tocList"
                    :key="item.id"
                    :class="['toc-item', { active: item.id === activeTocId }]"
                  >
                    <a
                      :title="item.text"
                      :data-level="item.level"
                      @click="scrollToHeading(item.id)"
                    >{{ item.text }}</a>
                  </li>
                </template>
                <li v-else class="toc-item">
                  <span class="no-content">暂无目录项</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部固定操作栏 -->
    <div class="editor-footer">
      <el-button type="info" @click="openSettingsDrawer">文章设置</el-button>
      <el-button @click="saveAsDraft">保存草稿</el-button>
      <el-button type="primary" @click="publishArticle">发布文章</el-button>
    </div>

    <!-- 文章设置抽屉 -->
    <el-drawer
      title="文章设置"
      :visible.sync="settingsDrawerVisible"
      direction="rtl"
      size="40%"
    >
      <div class="drawer-content">
        <el-tabs v-model="activeSettingTab" type="card">
          <!-- 基本设置 -->
          <el-tab-pane label="基本设置" name="basic">
            <el-form
              :model="articleSettings"
              label-width="80px"
              label-position="left"
            >
              <!-- 文章标签 -->
              <el-form-item label="文章标签">
                <el-select
                  v-model="articleSettings.tags"
                  multiple
                  filterable
                  allow-create
                  default-first-option
                  placeholder="请选择或输入标签"
                  style="width: 100%"
                >
                  <el-option
                    v-for="tag in commonTags"
                    :key="tag.value"
                    :label="tag.label"
                    :value="tag.value"
                  />
                </el-select>
              </el-form-item>

              <!-- 文章分类 -->
              <el-form-item label="文章分类">
                <el-select
                  v-model="articleSettings.category"
                  placeholder="请选择分类"
                  style="width: 100%"
                >
                  <el-option
                    v-for="category in categories"
                    :key="category.value"
                    :label="category.label"
                    :value="category.value"
                  />
                </el-select>
              </el-form-item>

              <!-- 文章摘要 -->
              <el-form-item label="文章摘要">
                <el-input
                  v-model="articleSettings.summary"
                  type="textarea"
                  :rows="3"
                  :placeholder="summaryPlaceholder"
                />
              </el-form-item>

              <!-- 封面图片 -->
              <el-form-item label="封面图片">
                <el-upload
                  class="cover-uploader"
                  action="#"
                  :auto-upload="false"
                  :show-file-list="false"
                  :on-change="handleCoverChange"
                >
                  <img
                    v-if="articleSettings.cover"
                    :src="articleSettings.cover"
                    class="cover-preview"
                  >
                  <i v-else class="el-icon-plus cover-uploader-icon" />
                </el-upload>
                <div class="cover-hint">建议尺寸：800x600像素</div>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <!-- 发布设置 -->
          <el-tab-pane label="发布设置" name="publish">
            <el-form
              :model="articleSettings"
              label-width="80px"
              label-position="left"
            >
              <!-- 文章类型 -->
              <el-form-item label="文章类型">
                <el-radio-group v-model="articleSettings.articleType">
                  <el-radio
                    v-for="(item, index) in dict.type.blog_article_type"
                    :key="index"
                    :label="item.value"
                  >{{ item.label }}</el-radio>
                </el-radio-group>
              </el-form-item>

              <!-- 文章范围 -->
              <el-form-item label="文章范围">
                <el-radio-group v-model="articleSettings.visibility">
                  <el-radio
                    v-for="(item, index) in dict.type
                      .blog_article_visible_range"
                    :key="index"
                    :label="item.value"
                  >{{ item.label }}</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="价格" v-if="articleSettings.visibility === '5'">
                <el-input-number
                  v-model="articleSettings.price"
                  :min="0"
                  :max="999999"
                  :precision="2"
                  :step="0.01"
                  placeholder="请输入价格"
                  style="width: 100%"
                />
              </el-form-item>

              <!-- 创作声明 -->
              <el-form-item label="创作声明">
                <el-radio-group v-model="articleSettings.creationStatement">
                  <el-radio
                    v-for="(item, index) in dict.type
                      .blog_article_creation_statement"
                    :key="index"
                    :label="item.value"
                  >{{ item.label }}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>

        <!-- 抽屉底部按钮 -->
        <div class="drawer-footer">
          <el-button @click="closeSettingsDrawer">取消</el-button>
          <el-button type="primary" @click="saveSettings">保存设置</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

export default {
  name: 'TencentDocEditor',
  components: {
    Editor,
    Toolbar
  },
  dicts: [
    'blog_article_visible_range',
    'blog_article_creation_statement',
    'blog_article_type'
  ],
  props: {
    // 标题相关
    title: {
      type: String,
      default: ''
    },
    titlePlaceholder: {
      type: String,
      default: '请输入文章标题...'
    },

    // 内容相关
    content: {
      type: String,
      default: ''
    },

    // 封面图片
    cover: {
      type: String,
      default: ''
    },

    // 文章设置
    articleData: {
      type: Object,
      default: () => ({
        tags: [],
        category: '',
        summary: '',
        articleType: 'original',
        visibility: 'public',
        creationStatement: []
      })
    },

    // 标签选项
    commonTags: {
      type: Array,
      default: () => [
        { value: '技术', label: '技术' },
        { value: '生活', label: '生活' },
        { value: '娱乐', label: '娱乐' },
        { value: '教育', label: '教育' },
        { value: '健康', label: '健康' }
      ]
    },

    // 分类选项
    categories: {
      type: Array,
      default: () => [
        { value: '前端', label: '前端' },
        { value: '后端', label: '后端' },
        { value: '设计', label: '设计' },
        { value: '产品', label: '产品' },
        { value: '运营', label: '运营' }
      ]
    },

    // 摘要占位符
    summaryPlaceholder: {
      type: String,
      default: '请输入文章摘要，如果不填写将自动截取正文前200字'
    }
  },
  data() {
    return {
      // 编辑器实例
      editor: null,

      // 页面标题
      pageTitle: this.title,

      // 编辑器内容
      html: this.content,

      // 工具栏配置
      toolbarConfig: {
        // 排除的菜单项（去掉全屏）
        excludeKeys: 'fullScreen',

        // 自定义工具栏配置
        toolbarKeys: [
          // 默认分组
          'headerSelect',
          '|',
          'bold',
          'italic',
          'underline',
          'through',
          '|',
          'color',
          'bgColor',
          '|',
          'fontSize',
          'fontFamily',
          '|',
          'lineHeight',
          '|',
          'bulletedList',
          'numberedList',
          'todo',
          '|',
          'insertTable',
          'insertImage',
          'insertLink',
          'code',
          'codeBlock',
          'divider',
          '|',
          'undo',
          'redo',
          '|',
          'clearStyle'
        ]
      },

      // 编辑器配置
      editorConfig: {
        placeholder: '在这里输入内容...',
        scroll: false, // 禁止编辑器自身滚动
        autoFocus: false,

        // 菜单配置
        MENU_CONF: {
          // 字体配置
          fontSize: {
            fontSizeList: [
              '12px',
              '14px',
              '16px',
              '18px',
              '20px',
              '24px',
              '28px',
              '32px',
              '40px'
            ]
          },

          // 字体配置
          fontFamily: {
            fontFamilyList: [
              '宋体',
              '仿宋',
              '黑体',
              '楷体',
              '微软雅黑',
              'Arial',
              'Tahoma',
              'Verdana',
              'Times New Roman',
              'Courier New'
            ]
          },

          // 行高配置
          lineHeight: {
            lineHeightList: ['1', '1.15', '1.5', '1.7', '2', '2.5', '3']
          },

          // 上传图片配置
          uploadImage: {
            fieldName: 'file',
            maxFileSize: 10 * 1024 * 1024, // 10M
            base64LimitSize: 5 * 1024 * 1024, // 5M以下转base64

            // 自定义上传（根据您的后端API调整）
            async customUpload(file, insertFn) {
              // 这里实现上传逻辑
              console.log('准备上传图片:', file)

              // 模拟上传
              const reader = new FileReader()
              reader.readAsDataURL(file)
              reader.onload = () => {
                // 插入base64图片
                insertFn(reader.result, '图片', reader.result)
              }

              // 实际项目中：
              // 1. 上传到服务器
              // 2. 获取服务器返回的URL
              // 3. 调用 insertFn(imageUrl, alt, href)
            },

            // 自定义插入图片
            customInsert(res, insertFn) {
              // 处理服务器返回的数据
              const url = res.data.url || res.url
              insertFn(url, '图片', url)
            }
          },

          // 上传视频配置
          uploadVideo: {
            fieldName: 'file',
            maxFileSize: 100 * 1024 * 1024 // 100M
          },

          // 插入链接配置
          insertLink: {
            checkLink(text, url) {
              // 检查链接是否合法
              if (!url) {
                alert('请输入链接地址')
                return false
              }
              if (!url.startsWith('http')) {
                alert('链接必须以 http 或 https 开头')
                return false
              }
              return true
            }
          },

          // 表格配置
          insertTable: {
            maxRow: 20,
            maxCol: 10
          },

          // 代码块配置
          codeBlock: {
            showLanguageSelect: true,
            languages: [
              { languageName: 'Plain Text', language: 'text' },
              { languageName: 'HTML/XML', language: 'html' },
              { languageName: 'CSS', language: 'css' },
              { languageName: 'JavaScript', language: 'javascript' },
              { languageName: 'TypeScript', language: 'typescript' },
              { languageName: 'Vue', language: 'vue' },
              { languageName: 'Java', language: 'java' },
              { languageName: 'Python', language: 'python' },
              { languageName: 'SQL', language: 'sql' },
              { languageName: 'JSON', language: 'json' }
            ]
          }
        }
      },

      // 编辑器模式
      mode: 'default', // 'default' 或 'simple'

      // 语言设置（可选）
      language: 'zh-CN',

      // 文章设置抽屉可见性
      settingsDrawerVisible: false,

      // 文章设置
      articleSettings: {
        tags: this.articleData.tags,
        category: this.articleData.category,
        summary: this.articleData.summary,
        cover: this.cover,
        articleType: this.articleData.articleType,
        reprintStatement: this.articleData.reprintStatement,
        visibility: this.articleData.visibility,
        creationStatement: this.articleData.creationStatement,
        price: this.articleData.price || 0
      },

      // 当前激活的设置标签
      activeSettingTab: 'basic',

      // 目录列表
      tocList: [],
      activeTocId: null,

      // 防抖定时器
      tocDebounceTimer: null
    }
  },

  methods: {
    /**
     * 发布文章
     */
    publishArticle() {
      if (!this.pageTitle) {
        this.$message.warning('请输入文章标题')
        return
      }

      if (!this.html) {
        this.$message.warning('请输入文章内容')
        return
      }

      // 检查必填设置项
      if (!this.articleSettings.category) {
        this.$message.warning('请选择文章分类')
        return
      }

      if (this.articleSettings.tags.length === 0) {
        this.$message.warning('请至少选择一个标签')
        return
      }
      console.log(this.html)

      // 构造发布数据
      const articleData = {
        title: this.pageTitle,
        content: this.editor.getHtml(),
        summary: this.articleSettings.summary,
        categoryId: this.articleSettings.category,
        tagDetailsList: this.articleSettings.tags,
        cover: this.articleSettings.cover,
        type: this.articleSettings.articleType,
        reprintStatement: this.articleSettings.reprintStatement,
        visibleRange: this.articleSettings.visibility,
        creationStatement: this.articleSettings.creationStatement,
        price: this.articleSettings.price,
        articleId: this.articleSettings.articleId
      }

      this.$emit('publish', articleData)
    },

    /**
     * 保存草稿
     */
    saveAsDraft() {
      // 构造草稿数据
      const draftData = {
        title: this.pageTitle,
        content: this.html,
        summary: this.articleSettings.summary,
        category: this.articleSettings.category,
        tagDetailsList: this.articleSettings.tags,
        cover: this.articleSettings.cover,
        type: this.articleSettings.articleType,
        reprintStatement: this.articleSettings.reprintStatement,
        visibleRange: 'draft',
        creationStatement: this.articleSettings.creationStatement,
        price: this.articleSettings.price,
        articleId: this.articleSettings.articleId
      }

      this.$emit('saveDraft', draftData)
      console.log('保存草稿:', draftData)
    },

    /**
     * 编辑器创建完成回调
     */
    onCreated(editor) {
      console.log('编辑器创建完成', editor)
      this.editor = Object.seal(editor)

      // 设置语言（如果需要）
      // editor.i18nChangeLanguage(this.language)

      // 初始化目录
      this.generateToc()

      // 添加编辑器内容滚动监听器
      this.addEditorScrollListener()
    },

    /**
     * 内容变化回调
     */
    onChange(editor) {
      this.html = editor.getHtml()
      this.$emit('update:content', this.html)
      this.$emit('change', this.html)

      // 防抖处理目录生成
      this.debounceGenerateToc()

      // 可以在这里保存内容到后端
      // this.saveContent()
    },

    /**
     * 编辑器失去焦点
     */
    onBlur(editor) {
      console.log('编辑器失去焦点')
    },

    /**
     * 编辑器获得焦点
     */
    onFocus(editor) {
      console.log('编辑器获得焦点')
    },

    /**
     * 点击编辑器区域
     */
    handleEditorClick(e) {
      if (e.target.id === 'editor-text-area') {
        if (this.editor) {
          this.editor.blur()
          this.editor.focus(true) // 聚焦到末尾
        }
      }
    },

    /**
     * 聚焦到编辑器
     */
    focusEditor() {
      if (this.editor) {
        this.editor.focus(true)
      }
    },

    /**
     * 返回上一页
     */
    handleBack() {
      if (window.history.length > 1) {
        this.$router.go(-1)
      } else {
        window.location.href = '/'
      }
    },

    /**
     * 保存内容（示例方法）
     */
    async saveContent() {
      try {
        const content = {
          title: this.pageTitle,
          content: this.html,
          updateTime: new Date().toISOString()
        }

        console.log('保存内容:', content)

        // 实际项目中发送到后端
        // const response = await this.$http.post('/api/document/save', content)
        // console.log('保存成功:', response)

        this.$message.success('内容已保存')
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error('保存失败')
      }
    },

    /**
     * 导出内容
     */
    exportContent() {
      if (!this.editor) return

      const content = this.editor.getHtml()
      const title = this.pageTitle || 'document'

      // 创建Blob对象
      const blob = new Blob([content], { type: 'text/html' })
      const url = URL.createObjectURL(blob)

      // 创建下载链接
      const a = document.createElement('a')
      a.href = url
      a.download = `${title}.html`
      document.body.appendChild(a)
      a.click()

      // 清理
      document.body.removeChild(a)
      URL.revokeObjectURL(url)

      this.$message.success('导出成功')
    },

    /**
     * 插入示例内容
     */
    insertSampleContent() {
      if (!this.editor) return

      const sampleHtml = ``

      this.editor.dangerouslyInsertHtml(sampleHtml)
      this.$message.info('已插入示例内容')
    },

    /**
     * 自动生成摘要
     */
    generateSummary() {
      // 从正文中提取前200个字符作为摘要
      const text = this.html.replace(/<[^>]+>/g, '') // 去除HTML标签
      return text.substring(0, 200) + (text.length > 200 ? '...' : '')
    },

    /**
     * 打开设置抽屉
     */
    openSettingsDrawer() {
      this.settingsDrawerVisible = true
    },

    /**
     * 关闭设置抽屉
     */
    closeSettingsDrawer() {
      this.settingsDrawerVisible = false
    },

    /**
     * 保存设置
     */
    saveSettings() {
      this.settingsDrawerVisible = false
      this.$message.success('设置已保存')
      this.$emit('settingsChange', this.articleSettings)
    },

    /**
     * 封面图片上传处理
     */
    handleCoverChange(file) {
      const isImage = file.raw.type.startsWith('image/')
      const isLt2M = file.raw.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('上传文件必须是图片格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      if (isImage && isLt2M) {
        this.articleSettings.cover = URL.createObjectURL(file.raw)
        this.$emit('update:cover', this.articleSettings.cover)
      }
    },

    /**
     * 重置编辑器内容
     */
    resetEditor(title = '', content = '') {
      this.pageTitle = title
      this.html = content

      if (this.editor) {
        this.editor.setHtml(content)
      }
    },

    /**
     * 生成目录
     */
    generateToc() {
      if (!this.html) {
        this.tocList = []
        return
      }

      // 创建临时DOM元素来解析HTML
      const tempDiv = document.createElement('div')
      tempDiv.innerHTML = this.html

      // 查找所有标题元素 (h1-h6)
      const headings = tempDiv.querySelectorAll('h1, h2, h3, h4, h5, h6')

      const toc = []
      headings.forEach((heading, index) => {
        // 创建ID，如果不存在则生成
        const id =
          heading.id ||
          `toc-${heading.tagName.toLowerCase()}-${index}-${Date.now()}`
        if (!heading.id) {
          heading.id = id
        }

        toc.push({
          id: id,
          text: heading.textContent.trim(),
          level: parseInt(heading.tagName.charAt(1))
        })
      })

      this.tocList = toc
    },

    /**
     * 防抖处理的目录生成
     */
    debounceGenerateToc() {
      // 清除之前的定时器
      if (this.tocDebounceTimer) {
        clearTimeout(this.tocDebounceTimer)
      }

      // 设置新的定时器，延迟300ms执行
      this.tocDebounceTimer = setTimeout(() => {
        this.generateToc()
      }, 300)
    },

    /**
     * 更新编辑器内容中的标题ID
     */
    updateHeadingIds() {
      if (!this.editor || !this.html) return

      // 创建临时DOM元素来解析HTML
      const tempDiv = document.createElement('div')
      tempDiv.innerHTML = this.html

      // 查找所有标题元素 (h1-h6)
      const headings = tempDiv.querySelectorAll('h1, h2, h3, h4, h5, h6')

      // 为没有ID的标题添加ID
      headings.forEach((heading, index) => {
        if (!heading.id) {
          heading.id = `toc-${heading.tagName.toLowerCase()}-${index}-${Date.now()}`
        }
      })

      // 更新编辑器内容
      if (headings.length > 0 && this.html !== tempDiv.innerHTML) {
        this.editor.setHtml(tempDiv.innerHTML)
      }
    },

    /**
     * 滚动到指定标题
     */
    scrollToHeading(id) {
      // 在编辑器内容中查找目标元素
      const editorContent = document.querySelector(
        '#editor-text-area .w-e-text'
      )
      if (editorContent) {
        const element = editorContent.querySelector(`#${id}`)
        if (element) {
          // 计算元素相对于编辑器容器的偏移量
          const offsetTop = element.offsetTop

          // 滚动到指定位置
          editorContent.scrollTo({
            top: offsetTop - 100, // 添加偏移量以便标题显示在合适位置
            behavior: 'smooth'
          })

          this.activeTocId = id

          // 清除激活状态，防止滚动后状态未更新
          setTimeout(() => {
            this.activeTocId = id
          }, 300)
        }
      } else {
        // 后备方案：尝试直接查找元素
        const element = document.getElementById(id)
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' })
          this.activeTocId = id

          // 清除激活状态，防止滚动后状态未更新
          setTimeout(() => {
            this.activeTocId = id
          }, 300)
        }
      }
    },

    /**
     * 更新当前激活的目录项
     */
    updateActiveToc() {
      if (!this.tocList.length) return

      // 在编辑器内容中查找滚动位置
      const editorContent = document.querySelector(
        '#editor-text-area .w-e-text'
      )
      if (editorContent) {
        const scrollPosition = editorContent.scrollTop + 100 // 添加偏移量以便提前识别

        // 查找当前可视区域的标题
        for (let i = this.tocList.length - 1; i >= 0; i--) {
          const heading = editorContent.querySelector(`#${this.tocList[i].id}`)
          if (heading && heading.offsetTop <= scrollPosition + 100) {
            this.activeTocId = this.tocList[i].id
            break
          }
        }
      } else {
        // 后备方案：使用窗口滚动位置
        const scrollPosition = window.scrollY + 200 // 添加偏移量以便提前识别

        // 查找当前可视区域的标题
        for (let i = this.tocList.length - 1; i >= 0; i--) {
          const heading = document.getElementById(this.tocList[i].id)
          if (heading && heading.offsetTop <= scrollPosition) {
            this.activeTocId = this.tocList[i].id
            break
          }
        }
      }
    },

    /**
     * 获取编辑器内容
     */
    getEditorContent() {
      return {
        title: this.pageTitle,
        content: this.html,
        articleSettings: this.articleSettings
      }
    },

    /**
     * 为编辑器内容添加滚动监听器
     */
    addEditorScrollListener() {
      // 等待编辑器完全加载后添加滚动监听器
      this.$nextTick(() => {
        // 由于wangeditor编辑器的内容是动态生成的，我们需要等待一段时间再添加监听器
        setTimeout(() => {
          const editorContent = document.querySelector(
            '#editor-text-area .w-e-text'
          )
          if (editorContent) {
            editorContent.addEventListener('scroll', this.updateActiveToc)
          }
        }, 1000) // 等待1秒以确保编辑器完全加载
      })
    },

    /**
     * 移除编辑器内容滚动监听器
     */
    removeEditorScrollListener() {
      const editorContent = document.querySelector(
        '#editor-text-area .w-e-text'
      )
      if (editorContent) {
        editorContent.removeEventListener('scroll', this.updateActiveToc)
      }
    },

    mounted() {
      // 组件挂载后可以初始化一些数据
      console.log('编辑器组件已挂载')

      // 添加滚动事件监听器
      window.addEventListener('scroll', this.updateActiveToc)

      // 模拟加载数据
      setTimeout(() => {
        // 这里可以加载后端数据
        // this.loadDocumentData()

        // 初始化目录
        this.generateToc()
      }, 500)

      // 等待DOM更新后尝试添加编辑器内容滚动监听器
      this.$nextTick(() => {
        this.addEditorScrollListener()
      })
    },

    beforeDestroy() {
      // 组件销毁前销毁编辑器实例
      if (this.editor) {
        this.editor.destroy()
        this.editor = null
      }

      // 移除滚动事件监听器
      window.removeEventListener('scroll', this.updateActiveToc)

      // 移除编辑器内容滚动监听器
      this.removeEditorScrollListener()
    },

    watch: {
      title: {
        handler(newVal) {
          this.pageTitle = newVal
        },
        immediate: true
      },
      content: {
        handler(newVal) {
          this.html = newVal
          if (this.editor) {
            this.editor.setHtml(newVal)
          }
        },
        immediate: true
      },
      cover: {
        handler(newVal) {
          this.articleSettings.cover = newVal
        },
        immediate: true
      },
      articleData: {
        handler(newVal) {
          this.articleSettings = { ...this.articleSettings, ...newVal }
        },
        deep: true,
        immediate: true
      },
      html: {
        handler(newVal) {
          // 更新编辑器内容中的标题ID
          this.updateHeadingIds()
        },
        immediate: false
      }
    }
  }
}
</script>

<style scoped>
.editor-toolbar {
  height: 40px;
}

#top-container {
  border-bottom: 1px solid #e8e8e8;
  padding: 10px 30px;
  background-color: #fafafa;
}

#top-container p {
  margin: 0;
}

#top-container a {
  color: #1890ff;
  text-decoration: none;
  font-size: 14px;
}

#top-container a:hover {
  color: #40a9ff;
  text-decoration: underline;
}

#content {
  height: calc(100vh - 91px);
  background-color: #fff;
  overflow-y: auto;
  position: relative;
}

#editor-container {
  width: 850px;
  margin: 30px auto 150px auto;
  background-color: #fff;
  padding: 20px 50px 50px 50px;
  border: 1px solid #e8e8e8;
  box-shadow: 0 2px 10px rgb(0 0 0 / 12%);
  border-radius: 2px;
}

#title-container {
  padding: 20px 0;
  border-bottom: 1px solid #e8e8e8;
  margin-bottom: 20px;
}

#title-container .el-input {
  font-size: 30px;
  border: none !important;
  outline: none !important;
  width: 100%;
  line-height: 1.5;
  font-weight: 500;
  color: #1d2129;
  background: transparent;
}

#title-container .el-input::placeholder {
  color: #8a919f;
}

#editor-text-area {
  min-height: 900px;
  cursor: text;
}

/* 底部固定操作栏 */
.editor-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  padding: 10px 20px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  z-index: 1200;
}

/* 抽屉内容样式 */
.drawer-content {
  padding: 20px;
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}

.drawer-content .el-tabs {
  flex: 1;
  overflow-y: auto;
}

.drawer-footer {
  padding: 20px 0 0 0;
  text-align: right;
}

/* 封面上传样式 */
.cover-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.cover-uploader .el-upload:hover {
  border-color: #409eff;
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.cover-preview {
  width: 178px;
  height: 178px;
  display: block;
}

.cover-hint {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  #editor-container {
    max-width: 850px;
    margin: 30px auto 150px auto;
    padding: 20px 50px 50px 50px;
  }

  #toc-container {
    position: fixed;
    bottom: 80px;
    left: 20px;
    width: 250px;
    max-height: 300px;
    z-index: 999;
  }
}

@media (max-width: 992px) {
  #editor-container {
    max-width: 90%;
    margin: 30px auto 150px auto;
    padding: 20px 30px 30px 30px;
  }

  #toc-container {
    position: fixed;
    bottom: 80px;
    left: 10px;
    width: 200px;
    max-height: 250px;
    z-index: 999;
  }
}

@media (max-width: 768px) {
  #editor-container {
    width: 95%;
    padding: 15px;
    margin: 15px auto 100px auto;
  }

  #title-container input {
    font-size: 24px;
  }

  #content {
    height: calc(100vh - 100px);
  }

  .editor-footer {
    padding: 10px;
  }

  /* 移动端抽屉适配 */
  .drawer-content {
    padding: 10px;
  }

  .drawer-content .el-tabs__content {
    padding: 10px 0;
  }

  .cover-uploader .el-upload {
    width: 120px;
    height: 120px;
  }

  .cover-uploader-icon {
    width: 120px;
    height: 120px;
    line-height: 120px;
  }

  .cover-preview {
    width: 120px;
    height: 120px;
  }

  #toc-container {
    position: fixed;
    bottom: 70px;
    left: 10px;
    width: 200px;
    max-height: 200px;
    z-index: 999;
  }
}

/* 目录样式 */
#editor-container {
  max-width: 850px;
  margin: 30px auto 150px auto;
  background-color: #fff;
  padding: 20px 50px 50px 50px;
  border: 1px solid #e8e8e8;
  box-shadow: 0 2px 10px rgb(0 0 0 / 12%);
  border-radius: 2px;
  position: relative;
}

#toc-container {
  width: 280px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  padding: 20px;
  position: fixed;
  top: 120px;
  right: 30px;
  height: auto;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-height: 300px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.toc-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e8e8e8;
  color: #333;
}

.toc-content {
  max-height: calc(100vh - 150px);
  overflow-y: auto;
  flex: 1;
}

.toc-content ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.toc-item {
  margin: 8px 0;
  padding: 4px 0;
}

.toc-item a {
  display: block;
  padding: 5px 8px;
  color: #666;
  text-decoration: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 14px;
}

.toc-item a:hover {
  background-color: #e6f7ff;
  color: #1890ff;
}

.toc-item.active a {
  background-color: #1890ff;
  color: #fff;
}

/* 根据标题级别缩进 */
.toc-item a[data-level="1"] {
  padding-left: 5px;
  font-weight: bold;
}

.toc-item a[data-level="2"] {
  padding-left: 15px;
}

.toc-item a[data-level="3"] {
  padding-left: 25px;
}

.toc-item a[data-level="4"] {
  padding-left: 35px;
}

.toc-item a[data-level="5"] {
  padding-left: 45px;
}

.toc-item a[data-level="6"] {
  padding-left: 55px;
}
</style>

<style>
/* 全局样式（非scoped）用于编辑器内部 */
@import "~@wangeditor/editor/dist/css/style.css";

/* 自定义编辑器样式 */
.w-e-text-container {
  line-height: 1.7 !important;
  font-size: 16px;
  color: #1d2129;
}

.w-e-text-container p {
  margin-bottom: 1em;
}

.w-e-text-container h1,
.w-e-text-container h2,
.w-e-text-container h3,
.w-e-text-container h4,
.w-e-text-container h5,
.w-e-text-container h6 {
  margin-top: 1.2em;
  margin-bottom: 0.6em;
  font-weight: 600;
}

.w-e-text-container ul,
.w-e-text-container ol {
  padding-left: 2em;
  margin-bottom: 1em;
}

.w-e-text-container table {
  border-collapse: collapse;
  width: 100%;
  margin-bottom: 1em;
}

.w-e-text-container table th,
.w-e-text-container table td {
  border: 1px solid #e8e8e8;
  padding: 8px 12px;
  text-align: left;
}

.w-e-text-container blockquote {
  border-left: 4px solid #1890ff;
  padding-left: 1em;
  margin: 1em 0;
  color: #666;
}

.w-e-text-container pre {
  background-color: #f6f8fa;
  border-radius: 6px;
  padding: 16px;
  overflow: auto;
  margin-bottom: 1em;
}

.w-e-text-container code {
  background-color: #f6f8fa;
  padding: 2px 4px;
  border-radius: 4px;
  font-family: "SFMono-Regular", Consolas, "Liberation Mono", Menlo, monospace;
}

::v-deep .el-input__inner {
  box-shadow: none !important;
  border: none !important;
}

</style>
