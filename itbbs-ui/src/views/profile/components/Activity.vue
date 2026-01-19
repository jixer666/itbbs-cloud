<template>
  <div class="user-activity">
    <div class="post-card">
      <div class="post-header">
        <div class="user-avatar">
          <img :src="'https://wpimg.wallstcn.com/57ed425a-c71e-4201-9428-68760c0537c4.jpg'+avatarPrefix" alt="user">
        </div>
        <div class="post-info">
          <div class="user-name">Iron Man</div>
          <div class="post-meta">
            <span class="time">分享于 - 今天 7:30 PM</span>
          </div>
        </div>
      </div>
      <div class="post-content">
        <p>
          Lorem ipsum represents a long-held tradition for designers,
          typographers and the like. Some people hate it and argue for
          its demise, but others ignore the hate as they create awesome
          tools to help create filler text for everyone from bacon lovers
          to Charlie Sheen fans.
        </p>
      </div>
      <div class="post-actions">
        <el-button size="small" type="text" class="action-btn">
          <i class="el-icon-share"></i>
          分享
        </el-button>
        <el-button size="small" type="text" class="action-btn">
          <svg-icon icon-class="like" />
          赞同
        </el-button>
        <el-button size="small" type="text" class="action-btn">
          <i class="el-icon-chat-line-square"></i>
          评论
        </el-button>
      </div>
    </div>
    
    <div class="post-card">
      <div class="post-header">
        <div class="user-avatar">
          <img :src="'https://wpimg.wallstcn.com/9e2a5d0a-bd5b-457f-ac8e-86554616c87b.jpg'+avatarPrefix" alt="user">
        </div>
        <div class="post-info">
          <div class="user-name">Captain America</div>
          <div class="post-meta">
            <span class="time">私信于 - 昨天</span>
          </div>
        </div>
      </div>
      <div class="post-content">
        <p>
          Lorem ipsum represents a long-held tradition for designers,
          typographers and the like. Some people hate it and argue for
          its demise, but others ignore the hate as they create awesome
          tools to help create filler text for everyone from bacon lovers
          to Charlie Sheen fans.
        </p>
      </div>
      <div class="post-actions">
        <el-button size="small" type="text" class="action-btn">
          <i class="el-icon-share"></i>
          分享
        </el-button>
        <el-button size="small" type="text" class="action-btn">
          <svg-icon icon-class="like" />
          赞同
        </el-button>
        <el-button size="small" type="text" class="action-btn">
          <i class="el-icon-chat-line-square"></i>
          评论
        </el-button>
      </div>
    </div>
    
    <div class="post-card">
      <div class="post-header">
        <div class="user-avatar">
          <img :src="'https://wpimg.wallstcn.com/fb57f689-e1ab-443c-af12-8d4066e202e2.jpg'+avatarPrefix" alt="user">
        </div>
        <div class="post-info">
          <div class="user-name">Spider Man</div>
          <div class="post-meta">
            <span class="time">发布了4张照片 - 2天前</span>
          </div>
        </div>
      </div>
      <div class="post-images">
        <el-carousel :interval="6000" type="card" height="220px" class="activity-carousel">
          <el-carousel-item v-for="item in carouselImages" :key="item">
            <div class="carousel-image-container">
              <img :src="item+carouselPrefix" class="carousel-image" alt="post image">
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="post-actions">
        <el-button size="small" type="text" class="action-btn">
          <i class="el-icon-share"></i>
          分享
        </el-button>
        <el-button size="small" type="text" class="action-btn">
          <svg-icon icon-class="like" />
          赞同
        </el-button>
        <el-button size="small" type="text" class="action-btn">
          <i class="el-icon-chat-line-square"></i>
          评论
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
const avatarPrefix = '?imageView2/1/w/80/h/80'
const carouselPrefix = '?imageView2/2/h/440'

export default {
  data() {
    return {
      carouselImages: [
        'https://wpimg.wallstcn.com/9679ffb0-9e0b-4451-9916-e21992218054.jpg',
        'https://wpimg.wallstcn.com/bcce3734-0837-4b9f-9261-351ef384f75a.jpg',
        'https://wpimg.wallstcn.com/d1d7b033-d75e-4cd6-ae39-fcd5f1c0a7c5.jpg',
        'https://wpimg.wallstcn.com/50530061-851b-4ca5-9dc5-2fead928a939.jpg'
      ],
      avatarPrefix,
      carouselPrefix
    }
  }
}
</script>

<style lang="scss" scoped>
.user-activity {
  padding: 10px 0;
}

.post-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
  overflow: hidden;
  transition: all 0.3s ease;
  
  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
    transform: translateY(-2px);
  }
}

.post-header {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f5f7fa;
  
  .user-avatar {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 12px;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  
  .post-info {
    flex: 1;
    
    .user-name {
      font-weight: 600;
      color: #303133;
      margin-bottom: 4px;
    }
    
    .post-meta {
      .time {
        font-size: 13px;
        color: #909399;
      }
    }
  }
}

.post-content {
  padding: 15px 20px;
  color: #606266;
  line-height: 1.6;
  
  p {
    margin: 0;
  }
}

.post-images {
  padding: 0 20px;
  
  .activity-carousel {
    border-radius: 8px;
    overflow: hidden;
    
    ::v-deep .el-carousel__container {
      border-radius: 8px;
    }
    
    .carousel-image-container {
      width: 100%;
      height: 220px;
      border-radius: 8px;
      overflow: hidden;
    }
    
    .carousel-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
}

.post-actions {
  display: flex;
  padding: 12px 20px;
  border-top: 1px solid #f5f7fa;
  background-color: #fafafa;
  
  .action-btn {
    margin-right: 20px;
    color: #909399;
    font-size: 14px;
    
    &:hover {
      color: #409eff;
    }
    
    i {
      margin-right: 4px;
    }
  }
}

@media (max-width: 768px) {
  .post-header {
    padding: 12px 15px;
    
    .user-avatar {
      width: 40px;
      height: 40px;
      margin-right: 10px;
    }
  }
  
  .post-content,
  .post-images {
    padding: 12px 15px;
  }
  
  .post-actions {
    padding: 10px 15px;
    
    .action-btn {
      margin-right: 15px;
      font-size: 13px;
    }
  }
}
</style>