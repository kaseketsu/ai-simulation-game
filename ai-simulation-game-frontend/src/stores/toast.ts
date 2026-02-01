import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface Toast {
  id: number
  message: string
  type: 'success' | 'error' | 'warning' | 'info'
}

export const useToastStore = defineStore('toast', () => {
  const toasts = ref<Toast[]>([])
  let nextId = 0

  /**
   * Add a toast notification
   * @param {string} message - The message to display
   * @param {'success'|'error'|'warning'|'info'} type - Type of toast
   * @param {number} duration - Duration in ms, default 2000
   */
  function add(message: string, type: Toast['type'] = 'info', duration = 2000) {
    const id = nextId++
    const toast: Toast = { id, message, type }
    toasts.value.push(toast)

    if (duration > 0) {
      setTimeout(() => {
        remove(id)
      }, duration)
    }
  }

  function remove(id: number) {
    const index = toasts.value.findIndex(t => t.id === id)
    if (index !== -1) {
      toasts.value.splice(index, 1)
    }
  }

  function success(message: string, duration?: number) {
    add(message, 'success', duration)
  }

  function error(message: string, duration?: number) {
    add(message, 'error', duration)
  }

  function warning(message: string, duration?: number) {
    add(message, 'warning', duration)
  }

  function info(message: string, duration?: number) {
    add(message, 'info', duration)
  }

  return {
    toasts,
    add,
    remove,
    success,
    error,
    warning,
    info
  }
})
