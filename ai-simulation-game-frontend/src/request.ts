import axios from "axios";

// 全局配置 axios 实例
const myAxios = axios.create({
    // 暂时用 localhost
    baseURL: "http://localhost:8080",
    timeout: 5000,
    withCredentials: true,
})

// 响应拦截器
myAxios.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 请求拦截器
myAxios.interceptors.request.use(
    (config) => {
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 导出 axios 实例
export default myAxios;
