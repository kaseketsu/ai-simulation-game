// @ts-ignore
/* eslint-disable */
import request from './axios.config'

/** 此处后端没有提供注释 POST /sysCode/add */
export async function addSysCode(body: API.SysCodeAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/sysCode/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
