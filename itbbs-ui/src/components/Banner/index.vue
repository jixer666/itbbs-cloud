<template>
  <div class="banner">
    <div class="banner-left">
      <div class="banner-title">VIP年卡买1送1</div>
      <div class="banner-sub">1元秒杀 · 领50元助学金 · 低价购智能硬件</div>
    </div>
    <el-button type="danger" round class="banner-btn">去抢购</el-button>
    <div class="banner-ad">广告</div>
  </div>
</template>

<script>
export default {
  name: 'Banner'
}
</script>

<style scoped>
.banner {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  padding: 18px 18px;
  background: linear-gradient(90deg, #ff5a3c, #ff3d7b);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}
.banner-left {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.banner-title {
  font-size: 30px;
  font-weight: 800;
  letter-spacing: 0.3px;
}
.banner-sub {
  opacity: 0.92;
}
.banner-btn {
  padding: 10px 18px;
}
.banner-ad {
  position: absolute;
  right: 12px;
  top: 10px;
  background: rgba(255, 255, 255, 0.22);
  padding: 4px 8px;
  border-radius: 999px;
  font-size: 12px;
}
</style>
