<template>
  <div class="nav">
    <div class="nav-inner">
      <div class="chips">
        <el-tag
          v-for="c in categories"
          :key="c"
          effect="plain"
          size="small"
          class="chip"
        >{{ c }}</el-tag>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CategoryChips',
  props: {
    categories: {
      type: Array,
      default: () => []
    }
  }
}
</script>

<style scoped>
.nav {
  background: #fff;
  border-bottom: 1px solid #eef0f5;
}
.nav-inner {
  margin: 0 auto;
  padding: 8px 12px 10px;
}
.chips {
  margin-top: 8px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.chip {
  border-radius: 16px;
}
</style>
