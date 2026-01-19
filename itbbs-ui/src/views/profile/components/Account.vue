<template>
  <div class="account-container">
    <el-form ref="accountForm" :model="user" :rules="rules" label-position="top" class="account-form">
      <el-form-item label="用户名" prop="name">
        <el-input 
          v-model.trim="user.name" 
          placeholder="请输入您的用户名"
          size="large"
          prefix-icon="el-icon-user"
        />
      </el-form-item>
      
      <el-form-item label="邮箱地址" prop="email">
        <el-input 
          v-model.trim="user.email" 
          placeholder="请输入您的邮箱地址"
          size="large"
          prefix-icon="el-icon-message"
        />
      </el-form-item>
      
      <el-form-item label="手机号码">
        <el-input 
          v-model.trim="user.phone" 
          placeholder="请输入您的手机号码"
          size="large"
          prefix-icon="el-icon-phone"
        />
      </el-form-item>
      
      <el-form-item label="个人简介">
        <el-input 
          v-model.trim="user.bio" 
          type="textarea"
          :rows="4"
          placeholder="介绍一下自己..."
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item>
        <el-button 
          type="primary" 
          @click="submit"
          size="large"
          class="submit-btn"
          :loading="submitting"
        >
          <i class="el-icon-check"></i>
          更新资料
        </el-button>
        <el-button 
          @click="resetForm"
          size="large"
          class="reset-btn"
        >
          <i class="el-icon-refresh-left"></i>
          重置
        </el-button>
      </el-form-item>
    </el-form>
    
    <div class="account-security">
      <div class="section-title">安全设置</div>
      <ul class="security-list">
        <li class="security-item">
          <div class="security-info">
            <div class="security-type">账户密码</div>
            <div class="security-desc">安全性高的密码可以保护您的账户安全</div>
          </div>
          <el-button size="small" type="text" class="security-action">修改</el-button>
        </li>
        <li class="security-item">
          <div class="security-info">
            <div class="security-type">密保手机</div>
            <div class="security-desc">用于保护账户安全和找回密码</div>
          </div>
          <el-button size="small" type="text" class="security-action">修改</el-button>
        </li>
        <li class="security-item">
          <div class="security-info">
            <div class="security-type">备用邮箱</div>
            <div class="security-desc">可用于验证身份和接收通知</div>
          </div>
          <el-button size="small" type="text" class="security-action">绑定</el-button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: '',
          email: '',
          phone: '',
          bio: ''
        }
      }
    }
  },
  data() {
    return {
      submitting: false,
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submit() {
      this.$refs.accountForm.validate((valid) => {
        if (valid) {
          this.submitting = true;
          
          // 模拟提交过程
          setTimeout(() => {
            this.submitting = false;
            this.$message({
              message: '用户信息已成功更新！',
              type: 'success',
              duration: 5 * 1000
            });
          }, 1500);
        } else {
          this.$message.error('请填写正确的信息');
          return false;
        }
      });
    },
    resetForm() {
      this.$refs.accountForm.resetFields();
      this.$message.info('已重置表单');
    }
  }
}
</script>

<style lang="scss" scoped>
.account-container {
  padding: 10px 0;
}

.account-form {
  max-width: 600px;
  margin: 0 auto 30px;
  
  ::v-deep .el-form-item__label {
    font-weight: 600;
    color: #303133;
    font-size: 14px;
  }
  
  .submit-btn {
    width: 120px;
    margin-right: 15px;
  }
  
  .reset-btn {
    width: 100px;
  }
}

.account-security {
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
  
  .section-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 20px;
  }
  
  .security-list {
    list-style: none;
    padding: 0;
    margin: 0;
  }
  
  .security-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px 0;
    border-bottom: 1px solid #f5f7fa;
    
    &:last-child {
      border-bottom: none;
    }
    
    .security-info {
      flex: 1;
      
      .security-type {
        font-size: 15px;
        color: #303133;
        margin-bottom: 4px;
        font-weight: 500;
      }
      
      .security-desc {
        font-size: 13px;
        color: #909399;
      }
    }
    
    .security-action {
      color: #409eff;
    }
  }
}

@media (max-width: 768px) {
  .security-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    
    .security-action {
      align-self: flex-end;
    }
  }
}
</style>