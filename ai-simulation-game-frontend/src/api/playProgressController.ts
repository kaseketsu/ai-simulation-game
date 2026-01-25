// @ts-ignore
/* eslint-disable */
import request from '@/request.ts'

/** 此处后端没有提供注释 POST /playProgress/query/playProgress */
export async function queryPlayProgress(
  body: API.PlayProgressQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePlayProgressVO>('/playProgress/query/playProgress', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /playProgress/save/playProgress */
export async function savePlayProgress(
  body: API.PlayProgressSaveRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString>('/playProgress/save/playProgress', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
