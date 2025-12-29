<template>
  <div class="creation-btn">
    <el-button type="primary" round @click="toCreation">创作</el-button>
  </div>
</template>

<script>
export default {
  name: 'CreationBtn',
  data() {
    return {

    }
  },
  methods: {
    toCreation() {
      this.$router.push('/creation')
    }
  }
}
</script>

<style>

</style>
