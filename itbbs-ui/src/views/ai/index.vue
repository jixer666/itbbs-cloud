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
                v-model="editingTitleValue"
                class="edit-title-input"
                size="mini"
                @blur="saveEditedTitle(index)"
                @keyup.enter.native="saveEditedTitle(index)"
                ref="titleInput"
              ></el-input>
            </div>
            <el-button
              v-if="chatHistory.length > 1"
              type="text"
              size="mini"
              class="delete-btn"
              icon="el-icon-delete"
              @click.stop="deleteChat(index)"
            ></el-button>
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
        <div class="messages-container" ref="messagesContainer">
          <div
            v-for="(message, index) in currentMessages"
            :key="index"
            :class="['message', message.role]"
          >
            <div class="avatar">
              <i v-if="message.role === 'user'" class="el-icon-user-solid"></i>
              <i v-else class="el-icon-chat-dot-round"></i>
            </div>
            <div class="content">
              <div class="text">{{ message.content }}</div>
              <div class="timestamp">{{ formatDate(message.timestamp) }}</div>
            </div>
          </div>

          <!-- 显示加载状态 -->
          <div v-if="loading" class="message ai">
            <div class="avatar">
              <i class="el-icon-chat-dot-round"></i>
            </div>
            <div class="content">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
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
            @click="sendMessage"
            class="send-btn"
          >
            发送
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { chatAiStream, chatAi } from '@/api/ai/bot';

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

    // 发送消息
    async sendMessage() {
      const message = this.inputMessage.trim();
      if (!message || this.loading) return;

      // 添加用户消息
      const userMessage = {
        role: "user",
        content: message,
        timestamp: new Date(),
      };

      // 如果是第一个消息，设置对话标题
      if (this.currentMessages.length === 0) {
        this.$set(this.chatHistory, this.currentChatIndex, {
          ...this.chatHistory[this.currentChatIndex],
          title: message.substring(0, 20) + (message.length > 20 ? "..." : ""),
          messages: [userMessage],
        });
      } else {
        this.currentMessages.push(userMessage);
      }

      this.inputMessage = "";
      this.loading = true;

      try {
        // 调用SSE流式AI接口
        await this.callAiStreamApi(message);
      } catch (error) {
        console.error("发送消息失败:", error);
        this.currentMessages.push({
          role: "ai",
          content: "抱歉，暂时无法处理您的请求，请稍后再试。",
          timestamp: new Date(),
        });
        this.loading = false;
      }
    },

    // 调用SSE流式AI接口
    async callAiStreamApi(message) {
      // 创建一个空的AI消息对象，用于逐步填充内容
      const aiMessage = {
        role: "ai",
        content: "",
        timestamp: new Date(),
      };
      
      this.currentMessages.push(aiMessage);
      
      // 准备请求数据
      const requestData = {
        content: message,
        chatId: this.currentChatIndex.toString()
      };
      
      try {
        // 发起SSE请求
        const response = await chatAiStream(requestData);
        
        // 处理SSE响应
        if (response && response.data) {
          const reader = response.data.getReader();
          const decoder = new TextDecoder('utf-8');
          let done = false;
          
          while (!done) {
            const { value, done: readerDone } = await reader.read();
            done = readerDone;
            
            if (value) {
              const chunk = decoder.decode(value, { stream: true });
              const lines = chunk.split('\n');
              
              for (const line of lines) {
                if (line.startsWith('data: ')) {
                  const dataStr = line.slice(6); // 移除 'data: ' 前缀
                  if (dataStr && dataStr !== '[DONE]') {
                    try {
                      const data = JSON.parse(dataStr);
                      if (data.content) {
                        // 更新AI消息的内容
                        aiMessage.content += data.content;
                        // 触发视图更新
                        this.$set(this.currentMessages, this.currentMessages.length - 1, aiMessage);
                        // 确保滚动到底部
                        this.scrollToBottom();
                      }
                    } catch (e) {
                      console.error('解析SSE数据出错:', e);
                    }
                  }
                }
              }
            }
          }
        }
      } catch (error) {
        console.error('SSE请求失败:', error);
        // 在错误情况下更新最后一条消息为错误信息
        const errorMessage = {
          role: "ai",
          content: "抱歉，AI服务暂时不可用，请稍后再试。",
          timestamp: new Date(),
        };
        this.currentMessages.pop(); // 移除空的消息占位符
        this.currentMessages.push(errorMessage); // 添加错误消息
      } finally {
        this.loading = false;
      }
    },

    // 生成模拟响应（实际应用中替换为真实的AI接口调用）
    generateMockResponse(input) {
      const responses = [
        `关于"${input}"的问题，我可以为您提供以下信息：这是一段模拟的AI回复内容，实际应用中这里会连接到真实的AI服务。`,
        `您问的是"${input}"吗？这是一个很好的问题。基于我的理解，相关的知识点包括...`,
        `感谢您的提问：${input}。根据我的分析，这个问题可以从以下几个方面来解答...`,
        `针对"${input}"，我的建议是：首先了解基本概念，然后实践操作，最后总结经验。`,
        `关于"${input}"，目前主流的解决方案包括几种方法，您可以根据具体需求选择合适的方式。`,
      ];
      return responses[Math.floor(Math.random() * responses.length)];
    },

    // 新建对话
    newChat() {
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
      const now = new Date();
      const diffMs = now - date;
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
              background-color: #1890ff;
              color: white;
              border-radius: 18px 4px 18px 18px;
            }
          }
        }

        &.ai {
          align-self: flex-start;

          .avatar {
            background-color: #f0f0f0;
            color: #666;
          }

          .content {
            .text {
              background-color: white;
              border-radius: 4px 18px 18px 18px;
              border: 1px solid #eee;
              padding: 12px 16px;
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

          i {
            font-size: 18px;
          }
        }

        .content {
          display: flex;
          flex-direction: column;
          min-width: 0;

          .text {
            padding: 12px 16px;
            word-wrap: break-word;
            white-space: pre-wrap;
            line-height: 1.5;
          }

          .timestamp {
            font-size: 12px;
            color: #999;
            margin-top: 4px;
            text-align: right;
          }
        }
      }

      .typing-indicator {
        display: flex;
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
