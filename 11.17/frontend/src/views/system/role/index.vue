<template>
  <div class="app-container">
    <el-card>
      <el-button v-if="hasPermission('role:add')" type="primary" @click="handleAdd" :icon="Plus" style="margin-bottom: 20px">新增角色</el-button>
      
      <el-table v-loading="loading" :data="roleList" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleKey" label="角色标识" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="380" fixed="right">
          <template #default="{ row }">
            <el-button v-if="hasPermission('role:edit')" type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="hasPermission('role:edit')" type="warning" size="small" @click="handleAssignPermission(row)">分配权限</el-button>
            <el-button v-if="hasPermission('role:edit')" type="success" size="small" @click="handleAssignMenu(row)">分配菜单</el-button>
            <el-button v-if="hasPermission('role:delete')" type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="getList"
        @current-change="getList"
        style="margin-top: 20px"
      />
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色标识" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="如: admin" />
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="如: 管理员" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog v-model="permissionDialogVisible" title="分配权限" width="600px">
      <el-tree
        ref="permissionTreeRef"
        :data="permissionTree"
        :props="{ children: 'children', label: 'permissionName' }"
        show-checkbox
        check-strictly
        node-key="id"
        :default-checked-keys="checkedPermissions"
      />
      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPermissions">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配菜单对话框 -->
    <el-dialog v-model="menuDialogVisible" title="分配菜单" width="600px">
      <el-tree
        ref="menuTreeRef"
        :data="menuTree"
        :props="{ children: 'children', label: 'menuName' }"
        show-checkbox
        check-strictly
        node-key="id"
        :default-checked-keys="checkedMenus"
      />
      <template #footer>
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitMenus">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getRoleList, addRole, updateRole, deleteRole, assignPermissions, assignMenus } from '@/api/role'
import { getPermissionTree } from '@/api/permission'
import { getMenuTree } from '@/api/menu'
import { hasPermission } from '@/utils/permission'

const loading = ref(false)
const dialogVisible = ref(false)
const permissionDialogVisible = ref(false)
const menuDialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const permissionTreeRef = ref(null)
const menuTreeRef = ref(null)
const roleList = ref([])
const permissionTree = ref([])
const menuTree = ref([])
const checkedPermissions = ref([])
const checkedMenus = ref([])
const currentRoleId = ref(null)
const total = ref(0)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10
})

const form = reactive({
  id: null,
  roleKey: '',
  roleName: '',
  description: '',
  status: 1
})

const rules = {
  roleKey: [{ required: true, message: '请输入角色标识', trigger: 'blur' }],
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
}

const getList = async () => {
  loading.value = true
  try {
    const { data } = await getRoleList(queryParams)
    roleList.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增角色'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑角色'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleAssignPermission = async (row) => {
  currentRoleId.value = row.id
  
  // 获取所有权限树
  const { data } = await getPermissionTree()
  permissionTree.value = data
  
  // 获取当前角色已有权限
  checkedPermissions.value = row.permissions ? row.permissions.map(p => p.id) : []
  
  permissionDialogVisible.value = true
}

const submitPermissions = async () => {
  const checkedKeys = permissionTreeRef.value.getCheckedKeys()
  const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
  const permissionIds = [...checkedKeys, ...halfCheckedKeys]
  
  try {
    await assignPermissions(currentRoleId.value, permissionIds)
    ElMessage.success('分配权限成功')
    permissionDialogVisible.value = false
    getList()
  } catch (error) {
    ElMessage.error('分配权限失败')
  }
}

const handleAssignMenu = async (row) => {
  currentRoleId.value = row.id
  
  // 获取所有菜单树
  const { data } = await getMenuTree()
  menuTree.value = data
  
  // 获取当前角色已有菜单
  checkedMenus.value = row.menus ? row.menus.map(m => m.id) : []
  
  menuDialogVisible.value = true
}

const submitMenus = async () => {
  const checkedKeys = menuTreeRef.value.getCheckedKeys()
  const halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys()
  const menuIds = [...checkedKeys, ...halfCheckedKeys]
  
  try {
    await assignMenus(currentRoleId.value, menuIds)
    ElMessage.success('分配菜单成功')
    menuDialogVisible.value = false
    getList()
  } catch (error) {
    ElMessage.error('分配菜单失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该角色吗?', '提示', {
    type: 'warning'
  }).then(async () => {
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    getList()
  })
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (form.id) {
        await updateRole(form.id, form)
      } else {
        await addRole(form)
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