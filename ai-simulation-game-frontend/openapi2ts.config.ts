/**
 * openapi2ts 配置文件
 */
export default {
    /**
     * 请求库路径
     */
    requestLibPath: "import request from '@/request.ts'",
    /**
     * 接口文档路径
     */
    schemaPath: 'http://localhost:8080/v3/api-docs',
    /**
     * ts 文件输出路径
     */
    serverPath: './src'
}
