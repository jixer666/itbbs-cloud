<template>
  <div class="creation-btn">
    <el-dropdown>
      <el-button type="success" round>
        创作<i class="el-icon-arrow-down el-icon--right"></i>
      </el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <i class="el-icon-edit"></i>
          <span @click="toCreation">写文章</span>
        </el-dropdown-item>
        <el-dropdown-item>
          <i class="el-icon-upload"></i>
          <span @click="toResource">传资源</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    
  </div>
</template>

<script>
export default {
  name: "CreationBtn",
  data() {
    return {};
  },
  methods: {
    toCreation() {
      this.$router.push("/creation");
    },
    toResource() {
      this.$router.push("/resource");
    }
  },
};
</script>

<style></style>
