<template>
  <div class="app-container">
    <el-card>
      <el-button v-if="hasPermission('permission:add')" type="primary" @click="handleAdd" :icon="Plus" style="margin-bottom: 20px">新增权限</el-button>
      
      <el-table v-loading="loading" :data="permissionList" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="permissionKey" label="权限标识" />
        <el-table-column prop="permissionName" label="权限名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button v-if="hasPermission('permission:edit')" type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="hasPermission('permission:delete')" type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="getList"
        style="margin-top: 20px"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="权限标识" prop="permissionKey">
          <el-input v-model="form.permissionKey" placeholder="如: user:add" />
        </el-form-item>
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="form.permissionName" placeholder="如: 添加用户" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getPermissionList, addPermission, updatePermission, deletePermission } from '@/api/permission'
import { hasPermission } from '@/utils/permission'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const permissionList = ref([])
const total = ref(0)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10
})

const form = reactive({
  id: null,
  permissionKey: '',
  permissionName: '',
  description: ''
})

const rules = {
  permissionKey: [{ required: true, message: '请输入权限标识', trigger: 'blur' }],
  permissionName: [{ required: true, message: '请输入权限名称', trigger: 'blur' }]
}

const getList = async () => {
  loading.value = true
  try {
    const { data } = await getPermissionList(queryParams)
    permissionList.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增权限'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑权限'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该权限吗?', '提示', {
    type: 'warning'
  }).then(async () => {
    await deletePermission(row.id)
    ElMessage.success('删除成功')
    getList()
  })
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (form.id) {
        await updatePermission(form.id, form)
      } else {
        await addPermission(form)
      }
      ElMessage.success('操作成功')
      dialogVisible.value = false
      getList()
    }
  })
}

onMounted(() => {
  getList()
})
</script>