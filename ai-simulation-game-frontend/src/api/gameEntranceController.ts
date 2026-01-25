// @ts-ignore
/* eslint-disable */
import request from '@/request.ts'

/** 此处后端没有提供注释 POST /game/entrance/start */
export async function gameStart(body: API.GameInitRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseGameInitStateVO>('/game/entrance/start', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
