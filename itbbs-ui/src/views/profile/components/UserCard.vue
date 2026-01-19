<template>
  <el-card class="user-card" shadow="hover">
    <div class="user-profile">
      <div class="user-avatar-container">
        <pan-thumb 
          :image="user.avatar" 
          :height="'120px'" 
          :width="'120px'" 
          :hoverable="false"
          class="user-avatar">
          <div class="welcome-text">Hello</div>
          <div class="user-role-badge">{{ user.role }}</div>
        </pan-thumb>
      </div>
      
      <div class="user-info">
        <div class="user-name">{{ user.name }}</div>
        <div class="user-role text-muted">{{ user.role | uppercaseFirst }}</div>
        
        <div class="user-contact">
          <div class="contact-item">
            <i class="el-icon-message"></i>
            <span>{{ user.email || 'admin@test.com' }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="user-stats">
      <div class="stat-item">
        <div class="stat-number">126</div>
        <div class="stat-label">文章</div>
      </div>
      <div class="stat-item">
        <div class="stat-number">24</div>
        <div class="stat-label">关注者</div>
      </div>
      <div class="stat-item">
        <div class="stat-number">36</div>
        <div class="stat-label">关注</div>
      </div>
    </div>

    <div class="user-bio">
      <div class="section-title">
        <i class="el-icon-user"></i>
        <span>个人信息</span>
      </div>
      <div class="bio-item">
        <div class="bio-label">职业</div>
        <div class="bio-value">软件工程师</div>
      </div>
      <div class="bio-item">
        <div class="bio-label">公司</div>
        <div class="bio-value">ITBBS科技有限公司</div>
      </div>
      <div class="bio-item">
        <div class="bio-label">地点</div>
        <div class="bio-value">中国, 北京</div>
      </div>

      <div class="section-title mt-20">
        <i class="el-icon-setting"></i>
        <span>技能掌握</span>
      </div>
      <div class="skills-list">
        <div class="skill-item">
          <span class="skill-name">Vue.js</span>
          <el-progress :percentage="85" :show-text="false" :stroke-width="8" class="skill-progress" />
        </div>
        <div class="skill-item">
          <span class="skill-name">JavaScript</span>
          <el-progress :percentage="75" :show-text="false" :stroke-width="8" class="skill-progress" />
        </div>
        <div class="skill-item">
          <span class="skill-name">Node.js</span>
          <el-progress :percentage="70" :show-text="false" :stroke-width="8" class="skill-progress" />
        </div>
        <div class="skill-item">
          <span class="skill-name">CSS/SCSS</span>
          <el-progress :percentage="80" :show-text="false" :stroke-width="8" class="skill-progress" />
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import PanThumb from '@/components/PanThumb'

export default {
  components: { PanThumb },
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: '',
          email: '',
          avatar: '',
          role: ''
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.user-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08) !important;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12) !important;
  }
}

.user-profile {
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}

.user-avatar-container {
  margin-bottom: 15px;
}

.user-avatar {
  position: relative;
  display: inline-block;
  border: 4px solid #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  overflow: hidden;
}

.welcome-text {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  opacity: 0;
  transition: opacity 0.3s;
}

.user-avatar:hover .welcome-text {
  opacity: 1;
}

.user-role-badge {
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  background: #409eff;
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  white-space: nowrap;
}

.user-info {
  .user-name {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 5px;
  }

  .user-role {
    font-size: 14px;
    color: #909399;
    margin-bottom: 15px;
  }
}

.user-contact {
  .contact-item {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: #606266;
    font-size: 14px;
    
    i {
      color: #409eff;
    }
  }
}

.user-stats {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;

  .stat-item {
    text-align: center;
    
    .stat-number {
      font-size: 24px;
      font-weight: 600;
      color: #409eff;
      margin-bottom: 4px;
    }
    
    .stat-label {
      font-size: 12px;
      color: #909399;
    }
  }
}

.user-bio {
  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 15px;
    
    i {
      color: #409eff;
    }
  }

  .bio-item {
    display: flex;
    margin-bottom: 12px;
    
    .bio-label {
      width: 60px;
      color: #909399;
      font-size: 14px;
    }
    
    .bio-value {
      flex: 1;
      color: #606266;
      font-size: 14px;
    }
  }
}

.skills-list {
  .skill-item {
    margin-bottom: 15px;
    
    .skill-name {
      display: block;
      font-size: 14px;
      color: #606266;
      margin-bottom: 6px;
      font-weight: 500;
    }
    
    .skill-progress {
      ::v-deep .el-progress-bar__outer {
        background-color: #f5f7fa;
        border-radius: 4px;
      }
    }
  }
}

.mt-20 {
  margin-top: 20px;
}

.text-muted {
  color: #909399;
}
</style>