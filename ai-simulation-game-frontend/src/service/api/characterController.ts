// @ts-ignore
/* eslint-disable */
import request from "@/request.ts";

/** 此处后端没有提供注释 POST /character/levelUp */
export async function levelUp(
  body: API.LevelUpRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString>("/character/levelUp", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
