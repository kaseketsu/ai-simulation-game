import { createVNode } from 'vue'
import { render } from 'vue'
import globalToast from '@/components/globalComponents/globalToast.vue'
// 创建气泡容器并挂载
const toastContainer = document.createElement("div");
document.body.appendChild(toastContainer);

// 创建 toast 方法
const toast = (options: {
  message: string;
  type?: 'success' | 'warning' | 'error' | 'info';
  duration?: number;
}) => {
  // 销毁已有气泡
  render(null, toastContainer)
  // 创建虚拟节点
  const vNode = createVNode(globalToast, {
    visible: true,
    message: options.message,
    type: options.type,
    duration: options.duration || 1500,
    onClose: () => {
      render(null, toastContainer)
    }
  })
  // 渲染组件
  render(vNode, toastContainer)
}

// 创建快捷方法
toast.error = (message: string, duration = 1500) => toast({ message, type: 'error', duration })
toast.success = (message: string, duration = 1500) => toast({ message, type: 'success', duration })
toast.warning = (message: string, duration = 1500) => toast({ message, type: 'warning', duration })
toast.info = (message: string, duration = 1500) => toast({ message, type: 'info', duration })

// 导出 toast 类
export default toast
