<template>
  <div class="chat-container">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="new-chat-btn">
        <el-button type="primary" icon="el-icon-plus" @click="newChat"
          >新建对话</el-button
        >
      </div>
      <div class="chat-history">
        <ul>
          <li
            v-for="(chat, index) in chatHistory"
            :key="index"
            :class="{ active: currentChatIndex === index }"
            @click="switchChat(index)"
          >
            <div class="chat-item-content">
              <span
                v-if="!isEditingTitle[index]"
                class="chat-title"
                @dblclick="startEditTitle(index, $event)"
              >
                {{ chat.title || `对话 ${index + 1}` }}
              </span>
              <el-input
                v-else
                ref="titleInput"
                v-model="editingTitleValue"
                class="edit-title-input"
                size="mini"
                @blur="saveEditedTitle(index)"
                @keyup.enter.native="saveEditedTitle(index)"
              />
            </div>
            <el-button
              v-if="chatHistory.length > 1"
              type="text"
              size="mini"
              class="delete-btn"
              icon="el-icon-delete"
              @click.stop="deleteChat(index)"
            />
          </li>
        </ul>
      </div>
    </div>

    <!-- 主聊天区 -->
    <div class="main-content">
      <div class="chat-header">
        <h2>{{ currentChatTitle }}</h2>
      </div>

      <div class="message-main-container">
        <div ref="messagesContainer" class="messages-container">
          <div
            v-for="(message, index) in currentMessages"
            :key="index"
            :class="['message', message.role]"
          >
            <div class="avatar">
              <i v-if="message.role === 'user'" class="el-icon-user-solid" />
              <i v-else class="el-icon-chat-dot-round" />
            </div>
            <div class="content">
              <div v-if="message.reasoning_content" class="reasoning-text">
                {{ message.reasoning_content }}
              </div>
              <div class="text">{{ message.content }}</div>
              <div class="timestamp">{{ formatDate(message.timestamp) }}</div>
            </div>
          </div>

          <!-- 显示加载状态 -->
          <div v-if="loading" class="message ai">
            <div class="avatar">
              <i class="el-icon-chat-dot-round" />
            </div>
            <div class="content">
              <div class="typing-indicator">
                <span />
                <span />
                <span />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-container">
        <div class="input-main">
          <el-input
            v-model="inputMessage"
            :rows="3"
            type="textarea"
            placeholder="请输入您的问题..."
            maxlength="1000"
            show-word-limit
            @keyup.enter.native="sendMessage"
          />
          <el-button
            type="primary"
            :disabled="!inputMessage.trim() || loading"
            class="send-btn"
            @click="sendMessage"
          >
            发送
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { chatAiStream } from "@/api/ai/bot";
import { fetchEventSource } from "@microsoft/fetch-event-source";

export default {
  name: "AiChat",
  data() {
    return {
      chatHistory: [
        {
          title: "欢迎使用AI助手",
          messages: [
            {
              role: "ai",
              content:
                "您好！我是AI助手，可以回答问题、创作文字、编程等。请问有什么可以帮助您的？",
              timestamp: new Date(),
            },
          ],
        },
      ],
      currentChatIndex: 0,
      inputMessage: "",
      loading: false,
      isEditingTitle: [], // 控制哪个对话标题正在被编辑
      editingTitleValue: "", // 编辑中的标题值
      sseController: null, // 用于控制SSE连接
    };
  },
  computed: {
    currentMessages() {
      return this.chatHistory[this.currentChatIndex]?.messages || [];
    },
    currentChatTitle() {
      return this.chatHistory[this.currentChatIndex]?.title || "AI助手";
    },
  },
  mounted() {
    this.scrollToBottom();
  },
  updated() {
    this.scrollToBottom();
  },
  beforeDestroy() {
    // 组件销毁前断开SSE连接
    this.abortSSE();
  },
  methods: {
    // 开始编辑对话标题
    startEditTitle(index, event) {
      event.stopPropagation(); // 阻止冒泡，避免切换对话
      this.editingTitleValue =
        this.chatHistory[index].title || `对话 ${index + 1}`;
      // 初始化isEditingTitle数组
      if (!Array.isArray(this.isEditingTitle)) {
        this.isEditingTitle = [];
      }
      // 清除所有编辑状态
      this.isEditingTitle = this.isEditingTitle.map(() => false);
      // 设置当前索引为编辑状态
      this.$set(this.isEditingTitle, index, true);

      // 等待DOM更新后聚焦到输入框
      this.$nextTick(() => {
        const inputRef = this.$refs.titleInput;
        if (inputRef && inputRef[0]) {
          inputRef[0].focus();
          inputRef[0].select();
        }
      });
    },

    // 保存编辑后的标题
    saveEditedTitle(index) {
      if (this.editingTitleValue.trim()) {
        this.$set(
          this.chatHistory[index],
          "title",
          this.editingTitleValue.trim()
        );
      } else {
        // 如果标题为空，恢复原来的标题
        this.editingTitleValue =
          this.chatHistory[index].title || `对话 ${index + 1}`;
      }

      // 关闭编辑状态
      this.$set(this.isEditingTitle, index, false);
    },

    // 删除对话
    deleteChat(index) {
      this.$confirm("确定要删除这个对话吗？", "删除对话", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 如果需要删除的对话正在进行SSE流，先中断
          if (index === this.currentChatIndex && this.loading) {
            this.abortSSE();
          }

          this.chatHistory.splice(index, 1);
          // 同步更新isEditingTitle数组
          this.isEditingTitle.splice(index, 1);

          // 如果删除的是当前选中的对话，则切换到第一个对话
          if (
            this.currentChatIndex >= this.chatHistory.length &&
            this.chatHistory.length > 0
          ) {
            this.currentChatIndex = 0;
          } else if (this.chatHistory.length === 0) {
            // 如果删除了所有对话，创建一个新的默认对话
            this.newChat();
          }

          this.$message({
            type: "success",
            message: "删除成功!",
          });
        })
        .catch(() => {
          // 用户取消删除操作
        });
    },

    // 中断SSE连接
    abortSSE() {
      if (this.sseController) {
        this.sseController.abort();
        this.sseController = null;
      }
      this.loading = false;
    },

    // 发送消息
    async sendMessage() {
      const message = this.inputMessage.trim();
      if (!message || this.loading) return;

      // 中断之前的SSE连接（如果有）
      this.abortSSE();

      // 添加用户消息
      const userMessage = {
        role: "user",
        content: message,
        timestamp: new Date(),
      };

      // 使用响应式方式更新消息
      if (this.currentMessages.length === 0) {
        // 如果是第一个消息，设置对话标题
        const newChat = {
          ...this.chatHistory[this.currentChatIndex],
          title: message.substring(0, 20) + (message.length > 20 ? "..." : ""),
          messages: [userMessage],
        };
        this.$set(this.chatHistory, this.currentChatIndex, newChat);
      } else {
        // 追加用户消息
        this.$set(this.chatHistory[this.currentChatIndex], "messages", [
          ...this.currentMessages,
          userMessage,
        ]);
      }

      this.inputMessage = "";
      this.loading = true;

      try {
        // 调用SSE流式AI接口
        await this.callAiStreamApi(message);
      } catch (error) {
        console.error("发送消息失败:", error);
        // 添加错误消息
        const errorMessage = {
          role: "ai",
          content: "抱歉，暂时无法处理您的请求，请稍后再试。",
          timestamp: new Date(),
        };
        this.$set(this.chatHistory[this.currentChatIndex], "messages", [
          ...this.currentMessages,
          errorMessage,
        ]);
        this.loading = false;
      }
    },

    // 调用SSE流式AI接口
    async callAiStreamApi(message) {
      // 准备请求数据
      const requestData = {
        content: message,
        chatId: this.currentChatIndex.toString(),
        mode: 2,
      };

      try {
        // 创建AbortController用于控制SSE连接
        const controller = new AbortController();
        this.sseController = controller;

        // 创建一个AI消息对象
        const aiMessage = {
          role: "ai",
          content: "",
          reasoning_content: "",
          timestamp: new Date(),
        };

        // 使用响应式方式添加AI消息占位符
        this.$set(this.chatHistory[this.currentChatIndex], "messages", [
          ...this.currentMessages,
          aiMessage,
        ]);

        // 获取当前消息的索引
        const currentChatIndex = this.currentChatIndex;
        const messageIndex = this.currentMessages.length - 1;

        // 缓存组件实例引用
        const self = this;

        // 使用fetchEventSource发起SSE请求
        await fetchEventSource(
          process.env.VUE_APP_AI_BASE_API + "/ai/bot/chat/stream",
          {
            // 使用与API模块一致的路径
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              // 如果需要认证头，添加相应逻辑
              ...this.getAuthHeaders(),
            },
            body: JSON.stringify(requestData),
            signal: controller.signal,
            onopen(response) {
              if (response.ok) {
                console.log("SSE连接已打开");
              } else {
                throw new Error(`SSE连接失败: ${response.status}`);
              }
            },
            onmessage(msg) {
              try {
                const data = JSON.parse(msg.data);

                // 根据步骤处理不同的响应
                if (data.step !== undefined) {
                  const currentMessages = [
                    ...self.chatHistory[currentChatIndex].messages,
                  ];

                  switch (data.step) {
                    case 0: // START
                      // 开始阶段，可以显示加载提示
                      break;
                    case 1: // FIRST - 问题分析
                      // 问题分析阶段，可以添加状态提示，但不应清空内容
                      // 此阶段仅做标记或状态更新，不修改实际内容
                      break;
                    case 2: // SECOND - 回答问题
                      // 获取实际内容
                      if (data.data) {
                        // 分别处理思考内容和主要内容
                        if (data.data.reasoning_content) {
                          currentMessages[messageIndex].reasoning_content +=
                            data.data.reasoning_content;
                        }
                        if (data.data.content) {
                          currentMessages[messageIndex].content +=
                            data.data.content;
                        }

                        // 使用$set确保响应式更新
                        self.$set(
                          self.chatHistory[currentChatIndex],
                          "messages",
                          currentMessages
                        );
                        // 滚动到底部
                        self.$nextTick(() => {
                          self.scrollToBottom();
                        });
                      }
                      break;
                    case 3: // THIRD - 完成回答
                      // 完成回答阶段，可以添加处理状态
                      break;
                    case 4: // FOURTH - 参考内容
                      // 参考内容阶段，可以添加处理状态
                      break;
                    case -1: // END - 结束
                      // 结束处理，显式关闭加载状态
                      self.loading = false;
                      controller.abort(); // 主动关闭连接
                      break;
                    default:
                      // 其他步骤，暂不处理
                      break;
                  }
                }
              } catch (e) {
                console.error("解析SSE数据出错:", e);
              }
            },
            onclose() {
              console.log("SSE连接已关闭");
              // 确保关闭加载状态
              self.loading = false;
              // 清除控制器引用
              self.sseController = null;
            },
            onerror(err) {
              console.error("SSE连接错误:", err);
              throw err; // 抛出错误以便catch捕获
            },
          }
        );
      } catch (error) {
        if (error.name !== "AbortError") {
          console.error("SSE请求失败:", error);
          // 在错误情况下更新最后一条消息为错误信息
          const errorMessage = {
            role: "ai",
            content: "抱歉，AI服务暂时不可用，请稍后再试。",
            timestamp: new Date(),
          };
          // 替换最后一条消息为错误消息
          this.$set(this.chatHistory[this.currentChatIndex], "messages", [
            ...this.currentMessages.slice(0, -1),
            errorMessage,
          ]);
        }
      } finally {
        // 清除控制器引用
        this.sseController = null;
        // 确保在任何情况下都会关闭loading状态
        if (this.loading) {
          this.loading = false;
        }
      }
    },

    // 获取认证头部信息
    getAuthHeaders() {
      // 这里需要根据项目的认证方式获取token
      const token = localStorage.getItem("access_token");
      if (token) {
        return { Authorization: `Bearer ${token}` };
      }
      return {};
    },

    // 新建对话
    newChat() {
      // 中断当前SSE连接
      this.abortSSE();

      const newChat = {
        title: "新的对话",
        messages: [],
      };
      this.chatHistory.push(newChat);
      this.currentChatIndex = this.chatHistory.length - 1;

      // 同步更新isEditingTitle数组
      this.$set(this.isEditingTitle, this.chatHistory.length - 1, false);
    },

    // 切换对话
    switchChat(index) {
      // 中断当前SSE连接
      this.abortSSE();

      this.currentChatIndex = index;
    },

    // 滚动到底部
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer;
        if (container) {
          container.scrollTop = container.scrollHeight;
        }
      });
    },

    // 格式化时间
    formatDate(date) {
      if (!date) return "";

      const now = new Date();
      const messageDate = date instanceof Date ? date : new Date(date);
      const diffMs = now - messageDate;
      const diffMins = Math.floor(diffMs / 60000);

      if (diffMins < 1) {
        return "刚刚";
      } else if (diffMins < 60) {
        return `${diffMins}分钟前`;
      } else if (diffMins < 1440) {
        // 小于一天
        return `${Math.floor(diffMins / 60)}小时前`;
      } else {
        return `${Math.floor(diffMins / 1440)}天前`;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.chat-container {
  display: flex;
  height: calc(100vh - 50px);
  width: 100%;
  background-color: #f5f7fa;

  .sidebar {
    width: 260px;
    background-color: #fff;
    border-right: 1px solid #e6e6e6;
    display: flex;
    flex-direction: column;

    .new-chat-btn {
      padding: 20px;
      border-bottom: 1px solid #eee;

      .el-button {
        width: 100%;
      }
    }

    .chat-history {
      flex: 1;
      overflow-y: auto;
      padding: 20px;

      h3 {
        margin-top: 0;
        margin-bottom: 15px;
        font-size: 16px;
        color: #333;
      }

      ul {
        list-style: none;
        padding: 0;
        margin: 0;

        li {
          padding: 10px 12px;
          border-radius: 6px;
          cursor: pointer;
          margin-bottom: 8px;
          transition: background-color 0.3s;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          display: flex;
          justify-content: space-between;
          align-items: center;

          &:hover {
            background-color: #f0f0f0;

            .delete-btn {
              opacity: 1;
            }
          }

          &.active {
            background-color: #e6f7ff;
            color: #1890ff;
          }

          .chat-item-content {
            flex: 1;
            display: flex;
            align-items: center;
          }

          .chat-title {
            flex: 1;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .edit-title-input {
            flex: 1;

            ::v-deep .el-input__inner {
              border: 1px solid #dcdfe6;
              border-radius: 4px;
              font-size: 14px;
              padding: 2px 6px;
            }
          }

          .delete-btn {
            opacity: 0;
            transition: opacity 0.3s;
            margin-left: 8px;

            &:hover {
              color: #f56c6c;
            }
          }
        }
      }
    }
  }

  .main-content {
    display: flex;
    flex-direction: column;
    position: relative;
    margin: 0 auto;
    width: 100%;

    .chat-header {
      padding: 16px 24px;
      border-bottom: 1px solid #e6e6e6;
      background-color: #fff;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

      h2 {
        margin: 0;
        font-size: 18px;
        color: #333;
      }
    }

    .message-main-container {
      flex: 1;
      display: flex;
      justify-content: center;
      overflow-y: auto;
      scrollbar-width: none;
      -ms-overflow-style: none;

      div::-webkit-scrollbar {
        display: none;
      }
    }

    .messages-container {
      width: 840px;
      overflow-y: auto;
      padding: 24px 0;
      display: flex;
      flex-direction: column;
      gap: 20px;
      div::-webkit-scrollbar {
        display: none;
      }

      .message {
        display: flex;
        gap: 12px;
        max-width: 80%;
        animation: fadeIn 0.3s ease-out;

        &.user {
          align-self: flex-end;
          flex-direction: row-reverse;

          .avatar {
            background-color: #1890ff;
            color: white;

            i {
              color: white;
            }
          }

          .content {
            .text {
              color: #24292f;
            }
          }
        }

        &.ai {
          align-self: flex-start;

          .avatar {
            background-color: #f6f8fa;
            color: #555;
            border: 1px solid #e1e5e9;
          }

          .content {
            .text {
              background-color: #ffffff;
              border-radius: 4px 18px 18px 18px;
              padding: 6px 8px;
              word-break: break-word;
              white-space: pre-wrap;
              line-height: 1.6;
              color: #24292f;
            }
          }
        }

        .avatar {
          width: 36px;
          height: 36px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          flex-shrink: 0;
          box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);

          i {
            font-size: 18px;
          }
        }

        .content {
          display: flex;
          flex-direction: column;
          min-width: 0;
          background-color: #ffffff;
          border-radius: 12px;
          padding: 12px 16px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

          .text {
            padding: 8px 0 0 0;
            word-wrap: break-word;
            white-space: pre-wrap;
            line-height: 1.6;
            color: #24292f;
          }

          .timestamp {
            font-size: 12px;
            color: #999;
            margin-top: 8px;
            text-align: right;
          }
        }
      }

      .typing-indicator {
        display: flex;
        color: #24292f;
        align-items: center;
        padding: 12px 16px;

        span {
          width: 8px;
          height: 8px;
          background-color: #999;
          border-radius: 50%;
          display: inline-block;
          margin: 0 2px;
          animation: typing 1s infinite;

          &:nth-child(2) {
            animation-delay: 0.2s;
          }

          &:nth-child(3) {
            animation-delay: 0.4s;
          }
        }
      }
    }

    .input-container {
      display: flex;
      justify-content: center;
      margin: 10px;

      .input-main {
        padding: 20px;
        width: 840px;
        display: flex;
        flex-direction: column;
        background-color: #fff;
        gap: 12px;
        border-radius: 10px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.02),
          0 2px 4px rgba(0, 0, 0, 0.04);

        .el-textarea {
          ::v-deep textarea {
            border-radius: 8px;
            border: none;
            resize: vertical;
            min-height: 80px !important;
            max-height: 120px;
          }
        }
      }

      .send-btn {
        align-self: flex-end;
        min-width: 80px;
      }
    }
  }
}

.reasoning-text {
  background-color: #f8f9fa; /* 类似DeepSeek的浅灰色背景 */
  border-left: 4px solid #87ceeb; /* 左侧蓝色装饰线 */
  border-radius: 8px 0 0 8px;
  padding: 12px 16px;
  margin-bottom: 8px;
  font-size: 14px;
  line-height: 1.6;
  color: #6c757d; /* 灰色字体，与主要内容区分 */
  font-style: italic; /* 斜体显示，表示这是推理过程 */
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: monospace; /* 等宽字体，更像代码推理 */
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes typing {
  0%,
  100% {
    transform: scale(0.8);
    opacity: 0.6;
  }
  50% {
    transform: scale(1.1);
    opacity: 1;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .chat-container {
    flex-direction: column;
    height: auto;

    .sidebar {
      width: 100%;
      height: auto;
      border-right: none;
      border-bottom: 1px solid #e6e6e6;

      .chat-history {
        display: none; /* 移动端隐藏历史记录 */
      }
    }

    .main-content {
      height: 70vh;
    }
  }
}
</style>
