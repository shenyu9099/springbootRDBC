<template>
  <div class="app-container">
    <el-card>
      <el-button v-if="hasPermission('menu:add')" type="primary" @click="handleAdd" :icon="Plus" style="margin-bottom: 20px">新增菜单</el-button>
      
      <el-table v-loading="loading" :data="menuList" row-key="id" border default-expand-all>
        <el-table-column prop="menuName" label="菜单名称" min-width="200" />
        <el-table-column prop="icon" label="图标" width="100">
          <template #default="{ row }">
            <el-icon v-if="row.icon">
              <component :is="formatIconName(row.icon)" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.type === 0">目录</el-tag>
            <el-tag v-else-if="row.type === 1" type="success">菜单</el-tag>
            <el-tag v-else type="warning">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button v-if="hasPermission('menu:edit')" type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="hasPermission('menu:delete')" type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="父菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="menuTreeData"
            :props="{ label: 'menuName', children: 'children' }"
            node-key="id"
            check-strictly
            :render-after-expand="false"
            placeholder="选择父菜单"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio :label="0">目录</el-radio>
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="路由路径" v-if="form.type !== 2">
          <el-input v-model="form.path" placeholder="如: /system/user" />
        </el-form-item>
        <el-form-item label="组件路径" v-if="form.type === 1">
          <el-input v-model="form.component" placeholder="如: system/user/index" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.icon" placeholder="如: User" />
        </el-form-item>
        <el-form-item label="权限标识">
          <el-input v-model="form.permission" placeholder="如: user:view" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '@/api/menu'
import { hasPermission } from '@/utils/permission'

// 动态导入Element Plus图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const menuList = ref([])

const form = reactive({
  id: null,
  parentId: 0,
  menuName: '',
  path: '',
  component: '',
  icon: '',
  sort: 0,
  type: 1,
  permission: '',
  status: 1
})

const rules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }]
}

// 格式化图标名称，将kebab-case转换为PascalCase
const formatIconName = (iconName) => {
  if (!iconName) return ''
  // 如果图标名称已经是PascalCase格式，直接返回
  if (/^[A-Z]/.test(iconName)) {
    return ElementPlusIconsVue[iconName] || iconName
  }
  // 将kebab-case转换为PascalCase
  const pascalCase = iconName.split('-').map(word => 
    word.charAt(0).toUpperCase() + word.substr(1).toLowerCase()
  ).join('')
  
  return ElementPlusIconsVue[pascalCase] || iconName
}

const menuTreeData = computed(() => {
  return [{ id: 0, menuName: '顶级菜单', children: menuList.value }]
})

const getList = async () => {
  loading.value = true
  try {
    const { data } = await getMenuTree()
    menuList.value = data
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增菜单'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑菜单'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该菜单吗?', '提示', {
    type: 'warning'
  }).then(async () => {
    await deleteMenu(row.id)
    ElMessage.success('删除成功')
    getList()
  })
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (form.id) {
        await updateMenu(form.id, form)
      } else {
        await addMenu(form)
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