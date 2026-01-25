import axios from 'axios'
import { BASE_URL } from './env'

// 自定义 axios 实例
const myAxios = axios.create({
  baseURL: BASE_URL,
  timeout: 60000,
  withCredentials: true,
})

export default myAxios
