<template>
  <div class="article-editor-container">
    <!-- 顶部操作栏 -->
    <div class="editor-header">
      <div class="header-left">
        <span class="editor-title">
          <i class="el-icon-edit-outline" />
          文章编辑器
        </span>
        <el-breadcrumb separator="/" class="breadcrumb">
          <el-breadcrumb-item>创作中心</el-breadcrumb-item>
          <el-breadcrumb-item>发布文章</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="header-right">
        <el-button
          size="medium"
          icon="el-icon-document"
          :loading="saving"
          @click="saveDraft"
        >
          保存草稿
        </el-button>
        <el-button
          type="primary"
          size="medium"
          icon="el-icon-position"
          @click="previewArticle"
        >
          预览文章
        </el-button>
        <el-button
          type="success"
          size="medium"
          icon="el-icon-s-promotion"
          :loading="publishing"
          @click="publishArticle"
        >
          发布文章
        </el-button>
      </div>
    </div>

    <!-- 文章基本信息卡片 -->
    <el-card shadow="never" class="article-info-card">
      <div slot="header" class="clearfix">
        <span>文章信息</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="toggleAdvanced">
          {{ showAdvanced ? '收起' : '更多选项' }}
          <i :class="showAdvanced ? 'el-icon-arrow-up' : 'el-icon-arrow-down'" />
        </el-button>
      </div>

      <!-- 标题输入 -->
      <div class="title-section">
        <el-input
          v-model="articleTitle"
          placeholder="请输入文章标题（建议 5-30 字）"
          maxlength="100"
          show-word-limit
          size="large"
          class="title-input"
        >
          <template slot="prepend">
            <i class="el-icon-edit-outline" />
          </template>
        </el-input>
      </div>

      <!-- 标签和分类 -->
      <el-row :gutter="20" class="meta-section">
        <el-col :span="12">
          <div class="meta-item">
            <label><i class="el-icon-collection-tag" /> 文章标签</label>
            <el-select
              v-model="selectedTags"
              multiple
              filterable
              allow-create
              default-first-option
              placeholder="请选择或输入标签"
              style="width: 100%"
            >
              <el-option
                v-for="tag in availableTags"
                :key="tag.value"
                :label="tag.label"
                :value="tag.value"
              />
            </el-select>
          </div>
        </el-col>

        <el-col :span="12">
          <div class="meta-item">
            <label><i class="el-icon-folder-opened" /> 文章分类</label>
            <el-select
              v-model="selectedCategory"
              placeholder="请选择分类"
              style="width: 100%"
            >
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </div>
        </el-col>
      </el-row>

      <!-- 高级选项 -->
      <el-collapse-transition>
        <div v-show="showAdvanced" class="advanced-section">
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="meta-item">
                <label><i class="el-icon-picture-outline" /> 封面图片</label>
                <el-upload
                  class="cover-upload"
                  action="/api/upload"
                  :show-file-list="false"
                  :on-success="handleCoverSuccess"
                  :before-upload="beforeCoverUpload"
                >
                  <div v-if="!coverImage" class="upload-area">
                    <i class="el-icon-plus" />
                    <div class="upload-text">上传封面</div>
                  </div>
                  <div v-else class="cover-preview">
                    <img :src="coverImage" alt="封面预览">
                    <div class="cover-actions">
                      <el-button type="text" @click.stop="removeCover">移除</el-button>
                    </div>
                  </div>
                </el-upload>
              </div>
            </el-col>

            <el-col :span="12">
              <div class="meta-item">
                <label><i class="el-icon-view" /> 可见范围</label>
                <el-radio-group v-model="visibility">
                  <el-radio label="public">公开</el-radio>
                  <el-radio label="private">私有</el-radio>
                  <el-radio label="password">密码保护</el-radio>
                </el-radio-group>
              </div>

              <div v-if="visibility === 'password'" class="meta-item">
                <label><i class="el-icon-lock" /> 访问密码</label>
                <el-input
                  v-model="articlePassword"
                  placeholder="请输入访问密码"
                  show-password
                />
              </div>
            </el-col>
          </el-row>
        </div>
      </el-collapse-transition>
    </el-card>

    <!-- 编辑器工具栏 -->
    <el-card shadow="never" class="editor-toolbar-card">
      <div class="toolbar">
        <!-- 文本格式 -->
        <div class="toolbar-group">
          <el-tooltip content="加粗 (Ctrl+B)" placement="top">
            <el-button
              size="mini"
              :type="editor && editor.isActive('bold') ? 'primary' : 'text'"
              class="tool-btn"
              @click="editor.chain().focus().toggleBold().run()"
            >
              <i class="el-icon-bold" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="斜体 (Ctrl+I)" placement="top">
            <el-button
              size="mini"
              :type="editor && editor.isActive('italic') ? 'primary' : 'text'"
              class="tool-btn"
              @click="editor.chain().focus().toggleItalic().run()"
            >
              <i class="el-icon-italic" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="下划线" placement="top">
            <el-button
              size="mini"
              :type="editor && editor.isActive('underline') ? 'primary' : 'text'"
              class="tool-btn"
              @click="editor.chain().focus().toggleUnderline().run()"
            >
              <i class="el-icon-underline" />
            </el-button>
          </el-tooltip>

          <el-divider direction="vertical" />

          <!-- 标题选择 -->
          <el-dropdown trigger="click" @command="setHeading">
            <el-button size="mini" class="tool-btn">
              标题<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="1">
                <h1 style="margin: 0; font-size: 16px;">一级标题</h1>
              </el-dropdown-item>
              <el-dropdown-item command="2">
                <h2 style="margin: 0; font-size: 14px;">二级标题</h2>
              </el-dropdown-item>
              <el-dropdown-item command="3">
                <h3 style="margin: 0; font-size: 12px;">三级标题</h3>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>

          <el-divider direction="vertical" />

          <!-- 对齐方式 -->
          <el-tooltip content="左对齐" placement="top">
            <el-button
              size="mini"
              :type="isTextAlignActive('left') ? 'primary' : 'text'"
              class="tool-btn"
              @click="setTextAlign('left')"
            >
              <i class="el-icon-align-left" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="居中对齐" placement="top">
            <el-button
              size="mini"
              :type="isTextAlignActive('center') ? 'primary' : 'text'"
              class="tool-btn"
              @click="setTextAlign('center')"
            >
              <i class="el-icon-align-center" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="右对齐" placement="top">
            <el-button
              size="mini"
              :type="isTextAlignActive('right') ? 'primary' : 'text'"
              class="tool-btn"
              @click="setTextAlign('right')"
            >
              <i class="el-icon-align-right" />
            </el-button>
          </el-tooltip>

          <el-divider direction="vertical" />

          <!-- 列表 -->
          <el-tooltip content="无序列表" placement="top">
            <el-button
              size="mini"
              :type="editor && editor.isActive('bulletList') ? 'primary' : 'text'"
              class="tool-btn"
              @click="editor.chain().focus().toggleBulletList().run()"
            >
              <i class="el-icon-list" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="有序列表" placement="top">
            <el-button
              size="mini"
              :type="editor && editor.isActive('orderedList') ? 'primary' : 'text'"
              class="tool-btn"
              @click="editor.chain().focus().toggleOrderedList().run()"
            >
              <i class="el-icon-sort" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="任务列表" placement="top">
            <el-button
              size="mini"
              :type="editor && editor.isActive('taskList') ? 'primary' : 'text'"
              class="tool-btn"
              @click="editor.chain().focus().toggleTaskList().run()"
            >
              <i class="el-icon-check" />
            </el-button>
          </el-tooltip>
        </div>

        <!-- 插入元素 -->
        <div class="toolbar-group">
          <el-divider direction="vertical" />

          <el-tooltip content="插入链接" placement="top">
            <el-button
              size="mini"
              :type="editor && editor.isActive('link') ? 'primary' : 'text'"
              class="tool-btn"
              @click="insertLink"
            >
              <i class="el-icon-link" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="插入图片" placement="top">
            <el-button
              size="mini"
              type="text"
              class="tool-btn"
              @click="insertImage"
            >
              <i class="el-icon-picture" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="插入表格" placement="top">
            <el-button
              size="mini"
              type="text"
              class="tool-btn"
              @click="insertTable"
            >
              <i class="el-icon-s-grid" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="插入代码块" placement="top">
            <el-button
              size="mini"
              :type="editor && editor.isActive('codeBlock') ? 'primary' : 'text'"
              class="tool-btn"
              @click="editor.chain().focus().toggleCodeBlock().run()"
            >
              <i class="el-icon-document" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="插入引用" placement="top">
            <el-button
              size="mini"
              :type="editor && editor.isActive('blockquote') ? 'primary' : 'text'"
              class="tool-btn"
              @click="editor.chain().focus().toggleBlockquote().run()"
            >
              <i class="el-icon-chat-line-round" />
            </el-button>
          </el-tooltip>
        </div>

        <!-- 右侧操作 -->
        <div class="toolbar-actions">
          <el-tooltip content="清除格式" placement="top">
            <el-button
              size="mini"
              type="text"
              class="tool-btn"
              @click="clearFormat"
            >
              <i class="el-icon-delete" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="撤销 (Ctrl+Z)" placement="top">
            <el-button
              size="mini"
              type="text"
              :disabled="!editor || !editor.can().undo()"
              class="tool-btn"
              @click="editor.chain().focus().undo().run()"
            >
              <i class="el-icon-refresh-left" />
            </el-button>
          </el-tooltip>

          <el-tooltip content="重做 (Ctrl+Y)" placement="top">
            <el-button
              size="mini"
              type="text"
              :disabled="!editor || !editor.can().redo()"
              class="tool-btn"
              @click="editor.chain().focus().redo().run()"
            >
              <i class="el-icon-refresh-right" />
            </el-button>
          </el-tooltip>

          <el-divider direction="vertical" />

          <el-button
            size="mini"
            type="text"
            class="tool-btn"
            @click="toggleFullscreen"
          >
            <i :class="isFullscreen ? 'el-icon-close' : 'el-icon-full-screen'" />
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 编辑器主体 -->
    <el-card shadow="never" class="editor-main-card" :class="{ 'fullscreen': isFullscreen }">
      <div class="editor-container">
        <editor-content :editor="editor" class="editor-content" />

        <!-- 编辑器占位提示 -->
        <div v-if="!editor || editor.isEmpty" class="editor-placeholder">
          <i class="el-icon-edit-outline" />
          <p>开始撰写您的精彩文章...</p>
          <p class="placeholder-tips">支持 Markdown 语法，可插入图片、表格、代码块等丰富内容</p>
        </div>
      </div>

      <!-- 底部状态栏 -->
      <div class="editor-statusbar">
        <div class="status-left">
          <span class="status-item">
            <i class="el-icon-document" />
            字数: {{ wordCount }}
          </span>
          <span class="status-item">
            <i class="el-icon-time" />
            预计阅读: {{ readingTime }}分钟
          </span>
        </div>
        <div class="status-right">
          <el-progress
            :percentage="completionPercentage"
            :stroke-width="6"
            :show-text="false"
            class="completion-progress"
          />
          <span class="completion-text">完成度 {{ completionPercentage }}%</span>
        </div>
      </div>
    </el-card>

    <!-- 侧边帮助面板 -->
    <el-drawer
      title="编辑指南"
      :visible.sync="helpDrawerVisible"
      direction="rtl"
      size="300px"
    >
      <div class="help-content">
        <h3><i class="el-icon-info" /> 快捷键</h3>
        <ul>
          <li><strong>Ctrl+B</strong> - 加粗</li>
          <li><strong>Ctrl+I</strong> - 斜体</li>
          <li><strong>Ctrl+K</strong> - 插入链接</li>
          <li><strong>Ctrl+Z</strong> - 撤销</li>
          <li><strong>Ctrl+Y</strong> - 重做</li>
        </ul>

        <h3><i class="el-icon-warning-outline" /> 注意事项</h3>
        <p>1. 文章会自动保存到草稿箱</p>
        <p>2. 支持上传本地图片，大小不超过5MB</p>
        <p>3. 发布前请仔细预览内容</p>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { Editor, EditorContent } from '@tiptap/vue-2'
import StarterKit from '@tiptap/starter-kit'
import Underline from '@tiptap/extension-underline'
import Link from '@tiptap/extension-link'
import Image from '@tiptap/extension-image'
import Table from '@tiptap/extension-table'
import TableRow from '@tiptap/extension-table-row'
import TableCell from '@tiptap/extension-table-cell'
import TableHeader from '@tiptap/extension-table-header'
import TextAlign from '@tiptap/extension-text-align'
import TaskList from '@tiptap/extension-task-list'
import TaskItem from '@tiptap/extension-task-item'
import CodeBlock from '@tiptap/extension-code-block'

export default {
  name: 'ArticleEditor',
  components: {
    EditorContent
  },

  data() {
    return {
      editor: null,
      articleTitle: '',
      selectedTags: [],
      selectedCategory: null,
      coverImage: '',
      visibility: 'public',
      articlePassword: '',
      showAdvanced: false,
      isFullscreen: false,
      saving: false,
      publishing: false,
      helpDrawerVisible: false,

      // 示例数据
      availableTags: [
        { label: 'JavaScript', value: 'javascript' },
        { label: 'Vue.js', value: 'vue' },
        { label: '前端开发', value: 'frontend' },
        { label: '教程', value: 'tutorial' },
        { label: '技术分享', value: 'tech' }
      ],

      categories: [
        { id: 1, name: '技术文章' },
        { id: 2, name: '经验分享' },
        { id: 3, name: '教程指南' },
        { id: 4, name: '资源整理' }
      ],

      lastAutoSave: null,
      autoSaveInterval: null
    }
  },

  computed: {
    wordCount() {
      if (!this.editor) return 0
      return this.editor.storage.characterCount
        ? this.editor.storage.characterCount.characters()
        : this.editor.getText().length
    },

    readingTime() {
      const wordsPerMinute = 200
      return Math.ceil(this.wordCount / wordsPerMinute)
    },

    completionPercentage() {
      if (!this.articleTitle.trim() && !this.wordCount) return 0

      let score = 0
      if (this.articleTitle.trim()) score += 20
      if (this.wordCount > 100) score += Math.min(this.wordCount / 10, 50)
      if (this.selectedTags.length > 0) score += 15
      if (this.selectedCategory) score += 15
      if (this.coverImage) score += 10

      return Math.min(Math.round(score), 100)
    }
  },

  mounted() {
    this.initEditor()
    this.startAutoSave()
  },

  beforeDestroy() {
    if (this.editor) {
      this.editor.destroy()
    }
    if (this.autoSaveInterval) {
      clearInterval(this.autoSaveInterval)
    }
  },

  methods: {
    initEditor() {
      this.editor = new Editor({
        content: `
          <h2>欢迎使用文章编辑器</h2>
          <p>这是一个基于 <strong>Tiptap</strong> 和 <strong>Element UI</strong> 构建的专业文章编辑器。</p>
          <p>您可以在这里：</p>
          <ul>
            <li>使用丰富的格式化工具</li>
            <li>插入图片、表格和代码块</li>
            <li>实时查看编辑统计信息</li>
            <li>自动保存草稿内容</li>
          </ul>
          <blockquote>
            <p>开始创作您的精彩内容吧！</p>
          </blockquote>
        `,
        extensions: [
          StarterKit,
          Underline,
          Link.configure({
            openOnClick: false
          }),
          Image.configure({
            inline: true,
            allowBase64: true
          }),
          Table.configure({
            resizable: true
          }),
          TableRow,
          TableHeader,
          TableCell,
          TextAlign.configure({
            types: ['heading', 'paragraph']
          }),
          TaskList,
          TaskItem.configure({
            nested: true
          }),
          CodeBlock
        ],
        onUpdate: ({ editor }) => {
          this.handleContentChange(editor.getHTML())
        },
        onFocus: () => {
          console.log('编辑器获得焦点')
        },
        onBlur: () => {
          console.log('编辑器失去焦点')
        }
      })
    },

    startAutoSave() {
      this.autoSaveInterval = setInterval(() => {
        this.autoSaveDraft()
      }, 30000) // 30秒自动保存一次
    },

    autoSaveDraft() {
      if (!this.editor || !this.articleTitle.trim()) return

      const draft = {
        title: this.articleTitle,
        content: this.editor.getHTML(),
        tags: this.selectedTags,
        category: this.selectedCategory,
        updatedAt: new Date().toISOString()
      }

      // 这里可以调用API保存到后端
      localStorage.setItem('article_draft', JSON.stringify(draft))
      this.lastAutoSave = new Date().toLocaleTimeString()

      this.$message.success({
        message: '草稿已自动保存',
        showClose: true,
        duration: 2000
      })
    },

    setHeading(level) {
      this.editor.chain().focus().toggleHeading({ level: parseInt(level) }).run()
    },

    setTextAlign(align) {
      this.editor.chain().focus().setTextAlign(align).run()
    },

    isTextAlignActive(align) {
      return this.editor && this.editor.isActive({ textAlign: align })
    },

    insertLink() {
      this.$prompt('请输入链接地址', '插入链接', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^(http|https):\/\/.+/,
        inputErrorMessage: '请输入有效的URL地址'
      }).then(({ value }) => {
        this.editor.chain().focus().setLink({ href: value }).run()
      }).catch(() => {
        // 用户取消
      })
    },

    insertImage() {
      this.$prompt('请输入图片URL', '插入图片', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '请输入图片地址'
      }).then(({ value }) => {
        this.editor.chain().focus().setImage({ src: value }).run()
      }).catch(() => {
        // 用户取消
      })
    },

    insertTable() {
      this.$prompt('请输入表格尺寸 (例如: 3x4)', '插入表格', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^\d+x\d+$/,
        inputErrorMessage: '请输入正确的格式，如 3x4'
      }).then(({ value }) => {
        const [rows, cols] = value.split('x').map(Number)
        this.editor.chain().focus().insertTable({
          rows,
          cols,
          withHeaderRow: true
        }).run()
      }).catch(() => {
        // 用户取消
      })
    },

    clearFormat() {
      this.editor.chain().focus().clearNodes().unsetAllMarks().run()
    },

    toggleFullscreen() {
      this.isFullscreen = !this.isFullscreen
    },

    toggleAdvanced() {
      this.showAdvanced = !this.showAdvanced
    },

    handleCoverSuccess(response, file) {
      this.coverImage = URL.createObjectURL(file.raw)
      this.$message.success('封面图片上传成功')
    },

    beforeCoverUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isLt5M = file.size / 1024 / 1024 < 5

      if (!isJPG && !isPNG) {
        this.$message.error('封面图片只能是 JPG/PNG 格式!')
        return false
      }
      if (!isLt5M) {
        this.$message.error('封面图片大小不能超过 5MB!')
        return false
      }
      return true
    },

    removeCover() {
      this.coverImage = ''
    },

    saveDraft() {
      this.saving = true

      // 模拟API调用
      setTimeout(() => {
        this.autoSaveDraft()
        this.saving = false
        this.$message({
          message: '草稿保存成功',
          type: 'success',
          duration: 2000
        })
      }, 1000)
    },

    previewArticle() {
      if (!this.articleTitle.trim()) {
        this.$message.warning('请先输入文章标题')
        return
      }

      const content = this.editor.getHTML()
      console.log('预览内容:', content)

      // 这里可以跳转到预览页面或打开预览弹窗
      this.$alert('预览功能已准备好', '文章预览', {
        confirmButtonText: '确定'
      })
    },

    publishArticle() {
      if (!this.articleTitle.trim()) {
        this.$message.warning('请先输入文章标题')
        return
      }

      if (!this.editor.getText().trim()) {
        this.$message.warning('文章内容不能为空')
        return
      }

      this.publishing = true

      // 模拟发布过程
      setTimeout(() => {
        this.publishing = false
        this.$message({
          message: '文章发布成功',
          type: 'success',
          duration: 3000
        })

        // 这里可以跳转到文章详情页或列表页
        console.log('发布数据:', {
          title: this.articleTitle,
          content: this.editor.getHTML(),
          tags: this.selectedTags,
          category: this.selectedCategory,
          cover: this.coverImage,
          visibility: this.visibility
        })
      }, 2000)
    },

    handleContentChange(content) {
      // 内容变化时的处理
      console.log('内容已更新，字数:', this.wordCount)
    }
  }
}
</script>

<style scoped>
.article-editor-container {
  background-color: #f5f7fa;
  min-height: 100vh;
  padding: 20px;
}

/* 顶部操作栏 */
.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.editor-title {
  font-size: 22px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 10px;
}

.breadcrumb >>> .el-breadcrumb__inner {
  color: rgba(255, 255, 255, 0.8) !important;
}

.breadcrumb >>> .el-breadcrumb__separator {
  color: rgba(255, 255, 255, 0.6) !important;
}

.header-right {
  display: flex;
  gap: 15px;
}

/* 文章信息卡片 */
.article-info-card {
  margin-bottom: 20px;
  border-radius: 12px;
  border: 1px solid #ebeef5;
}

.title-section {
  margin-bottom: 25px;
}

.title-input >>> .el-input-group__prepend {
  background-color: #f5f7fa;
  border-right: 1px solid #dcdfe6;
  padding: 0 15px;
}

.meta-section {
  margin-bottom: 20px;
}

.meta-item {
  margin-bottom: 20px;
}

.meta-item label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #606266;
  font-size: 14px;
}

.meta-item label i {
  margin-right: 6px;
  color: #409eff;
}

/* 封面图片上传 */
.cover-upload {
  width: 100%;
}

.upload-area {
  width: 100%;
  height: 120px;
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #909399;
  cursor: pointer;
  transition: border-color 0.3s;
}

.upload-area:hover {
  border-color: #409eff;
}

.upload-area .el-icon-plus {
  font-size: 32px;
  margin-bottom: 10px;
}

.upload-text {
  font-size: 14px;
}

.cover-preview {
  position: relative;
  width: 100%;
  height: 120px;
  overflow: hidden;
  border-radius: 8px;
}

.cover-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-actions {
  position: absolute;
  top: 5px;
  right: 5px;
}

/* 编辑器工具栏 */
.editor-toolbar-card {
  margin-bottom: 20px;
  border-radius: 12px;
  border: 1px solid #ebeef5;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
}

.toolbar-group {
  display: flex;
  gap: 5px;
  align-items: center;
}

.toolbar-actions {
  display: flex;
  gap: 5px;
  align-items: center;
}

.tool-btn {
  min-width: 36px;
  height: 36px;
  padding: 0;
}

/* 编辑器主体 */
.editor-main-card {
  border-radius: 12px;
  border: 1px solid #ebeef5;
  transition: all 0.3s ease;
}

.editor-main-card.fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 2000;
  margin: 0;
  border-radius: 0;
}

.editor-container {
  position: relative;
  min-height: 500px;
}

.editor-content {
  min-height: 500px;
  padding: 25px;
  background-color: #ffffff;
  font-size: 16px;
  line-height: 1.8;
  color: #303133;
}

/* 编辑器占位提示 */
.editor-placeholder {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: #909399;
  width: 100%;
  pointer-events: none;
  z-index: 1;
}

.editor-placeholder i {
  font-size: 64px;
  margin-bottom: 20px;
  color: #dcdfe6;
}

.editor-placeholder p {
  font-size: 18px;
  margin-bottom: 10px;
  color: #606266;
}

.placeholder-tips {
  font-size: 14px;
  color: #909399;
  margin-top: 10px;
}

/* 状态栏 */
.editor-statusbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 25px;
  background-color: #f5f7fa;
  border-top: 1px solid #ebeef5;
  border-radius: 0 0 12px 12px;
}

.status-left {
  display: flex;
  gap: 20px;
}

.status-item {
  font-size: 14px;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 6px;
}

.status-item i {
  color: #409eff;
}

.status-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.completion-progress {
  width: 120px;
}

.completion-text {
  font-size: 14px;
  color: #67c23a;
  font-weight: 500;
}

/* 编辑器内容样式 */
.editor-content >>> .ProseMirror {
  min-height: 450px;
  outline: none;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.editor-content >>> .ProseMirror p {
  margin-bottom: 1.2em;
  line-height: 1.8;
  color: #303133;
}

.editor-content >>> .ProseMirror h1 {
  font-size: 2.2em;
  margin: 1.5em 0 0.8em;
  color: #303133;
  font-weight: 600;
  border-bottom: 2px solid #e4e7ed;
  padding-bottom: 10px;
}

.editor-content >>> .ProseMirror h2 {
  font-size: 1.8em;
  margin: 1.5em 0 0.7em;
  color: #303133;
  font-weight: 600;
}

.editor-content >>> .ProseMirror h3 {
  font-size: 1.4em;
  margin: 1.5em 0 0.6em;
  color: #606266;
  font-weight: 600;
}

.editor-content >>> .ProseMirror ul,
.editor-content >>> .ProseMirror ol {
  padding-left: 2.2em;
  margin: 1.2em 0;
}

.editor-content >>> .ProseMirror li {
  margin-bottom: 0.5em;
}

.editor-content >>> .ProseMirror blockquote {
  border-left: 4px solid #409eff;
  padding: 1em 1.5em;
  margin: 1.5em 0;
  background: #ecf5ff;
  border-radius: 0 8px 8px 0;
  color: #303133;
  font-style: italic;
}

.editor-content >>> .ProseMirror a {
  color: #409eff;
  text-decoration: none;
  border-bottom: 1px solid #409eff;
}

.editor-content >>> .ProseMirror a:hover {
  color: #66b1ff;
  border-bottom: 2px solid #66b1ff;
}

.editor-content >>> .ProseMirror code {
  background: #f5f7fa;
  color: #e96900;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 0.9em;
}

.editor-content >>> .ProseMirror pre {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 1.5em;
  margin: 1.5em 0;
  overflow-x: auto;
  border: 1px solid #e4e7ed;
}

.editor-content >>> .ProseMirror pre code {
  background: none;
  color: #303133;
  padding: 0;
}

.editor-content >>> .ProseMirror table {
  border-collapse: collapse;
  width: 100%;
  margin: 1.5em 0;
  border: 1px solid #e4e7ed;
}

.editor-content >>> .ProseMirror table td,
.editor-content >>> .ProseMirror table th {
  border: 1px solid #e4e7ed;
  padding: 12px 16px;
  text-align: left;
}

.editor-content >>> .ProseMirror table th {
  background-color: #f5f7fa;
  font-weight: 600;
}

/* 帮助面板 */
.help-content {
  padding: 20px;
}

.help-content h3 {
  color: #303133;
  margin: 20px 0 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.help-content h3 i {
  color: #409eff;
}

.help-content ul {
  padding-left: 20px;
  margin-bottom: 20px;
}

.help-content li {
  margin-bottom: 8px;
  color: #606266;
}

.help-content p {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 10px;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .article-editor-container {
    padding: 15px;
  }

  .editor-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }

  .header-right {
    width: 100%;
    justify-content: flex-end;
  }
}

@media (max-width: 768px) {
  .meta-section .el-col {
    width: 100%;
    margin-bottom: 15px;
  }

  .toolbar {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }

  .toolbar-actions {
    width: 100%;
    justify-content: flex-end;
  }

  .editor-statusbar {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }

  .status-right {
    width: 100%;
    justify-content: flex-start;
  }
}

/* 滚动条美化 */
.editor-content >>> .ProseMirror::-webkit-scrollbar {
  width: 8px;
}

.editor-content >>> .ProseMirror::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.editor-content >>> .ProseMirror::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.editor-content >>> .ProseMirror::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.article-editor-container {
  animation: fadeIn 0.5s ease-out;
}

/* 全屏模式样式 */
.editor-main-card.fullscreen .editor-content {
  min-height: calc(100vh - 120px);
  max-height: calc(100vh - 120px);
  overflow-y: auto;
}
</style>
