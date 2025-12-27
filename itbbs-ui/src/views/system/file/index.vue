<template>
  <div class="file-container">
    <div>
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="文件名称">
          <el-input v-model="searchForm.fileName" placeholder="请输入文件名称" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="medium" @click="getList">搜索</el-button>
          <el-button size="medium" @click="searchForm = {}">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="btn-div">
      <div>
        <el-button plain size="mini" type="primary" @click="handleAdd">新增</el-button>
        <el-button plain size="mini" type="danger" :disabled="multipleSelectionIds.length === 0" @click="handleDelete(multipleSelectionIds)">批量删除</el-button>
      </div>
      <div />
    </div>
    <div>
      <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column key="filename" label="文件名称" align="center" prop="filename" :show-overflow-tooltip="true" />
        <el-table-column key="totalSize" label="文件大小" align="center" prop="totalSize" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{ formatFileSize(scope.row.totalSize) }}</span>
          </template>
        </el-table-column>
        <el-table-column key="fileType" label="文件类型" align="center" prop="fileType" :show-overflow-tooltip="true" />
        <el-table-column key="fileMd5" label="文件MD5" align="center" prop="fileMd5" :show-overflow-tooltip="true" />
        <el-table-column key="ossType" label="OSS类型" align="center" prop="ossType" :show-overflow-tooltip="true" width="150">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.system_oss_type" :value="scope.row.ossType" />
          </template>
        </el-table-column>
        <el-table-column key="filePath" label="文件路径" align="center" prop="filePath" :show-overflow-tooltip="true" />
        <el-table-column key="userId" label="上传者ID" align="center" prop="userId" :show-overflow-tooltip="true" />
        <el-table-column key="status" label="状态" align="center" width="100">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.common_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="150" />
        <el-table-column
          label="操作"
          align="center"
          width="150"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete([scope.row.fileId])"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <pagination
      v-show="searchForm.total > 0"
      :total="searchForm.total"
      :page.sync="searchForm.pageNum"
      :limit.sync="searchForm.pageSize"
      @pagination="getList"
    />
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-form ref="form" :model="form" label-width="80px" :rules="rules">
        <el-form-item label="文件名称" prop="filename">
          <el-input v-model="form.filename" />
        </el-form-item>
        <el-form-item label="文件大小" prop="totalSize">
          <el-input v-model="form.totalSize" disabled />
        </el-form-item>
        <el-form-item label="文件类型" prop="fileType">
          <el-input v-model="form.fileType" disabled />
        </el-form-item>
        <el-form-item label="文件MD5" prop="fileMd5">
          <el-input v-model="form.fileMd5" disabled />
        </el-form-item>
        <el-form-item label="OSS类型" prop="ossType">
          <el-select v-model="form.ossType" placeholder="请选择OSS类型" disabled>
            <el-option
              v-for="item in dict.type.system_oss_type"
              :key="item.value"
              :label="item.label"
              :value="parseInt(item.value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文件路径" prop="filePath">
          <el-input v-model="form.filePath" disabled />
        </el-form-item>
        <el-form-item label="文件状态">
          <el-radio-group v-model="form.status">
            <el-radio v-for="(item, index) in dict.type.common_status" :key="index" :label="parseInt(item.value)">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="onSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getFilePage, addFile, updateFile, deleteFile } from '@/api/system/file'
import { formatFileSize } from '@/utils/index'

export default {
  name: 'File',
  dicts: ['common_status', 'system_oss_type'],
  data() {
    return {
      searchForm: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      form: {},
      tableList: [],
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      dialogTitle: null,
      rules: {
        fileName: [
          { required: true, message: '文件名称不能为空', trigger: 'blur' }
        ],
        fileKey: [
          { required: true, message: '文件字符不能为空', trigger: 'blur' }
        ]
      },
      multipleSelectionIds: []
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    formatFileSize,
    getList() {
      this.loading = true
      getFilePage(this.searchForm).then(res => {
        this.tableList = res.data.list
        this.searchForm.total = parseInt(res.data.total)
        this.loading = false
      }).catch(error => {
        this.loading = false
      })
    },
    onSubmit() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.fileId != undefined) {
            updateFile(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.submitLoading = false
              this.dialogVisible = false
              this.getList()
            }).catch(error => {
              this.submitLoading = false
            })
          } else {
            addFile(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.submitLoading = false
              this.dialogVisible = false
              this.getList()
            }).catch(error => {
              this.submitLoading = false
            })
          }
        }
      })
    },
    handleUpdate(item) {
      this.form = item
      this.dialogTitle = '修改文件'
      this.dialogVisible = true
    },
    handleDelete(ids) {
      if (ids === null || ids.length === 0) {
        this.$modal.msgWarning('未选中文件列表')
        return
      }
      this.$modal.confirm('是否确认删除文件编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true
        return deleteFile({
          fileIds: ids
        })
      }).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.loading = false
        this.getList()
      }).catch((error) => {
        this.loading = false
      })
    },
    handleAdd() {
      this.dialogTitle = '新增文件'
      this.dialogVisible = true
    },
    handleClose() {
      this.form = {
        menuCheckStrictly: true
      }
      this.dialogVisible = false
    },
    handleSelectionChange(val) {
      this.multipleSelectionIds = val.map(item => item.fileId)
    }
  }
}
</script>

<style lang="scss" scoped>
.file-container {
  padding: 20px;
}

.btn-div {
  display: flex;
  justify-content: space-between;
  align-content: center;
}

</style>
