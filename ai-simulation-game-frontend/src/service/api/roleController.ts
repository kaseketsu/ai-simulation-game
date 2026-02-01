// @ts-ignore
/* eslint-disable */
import request from "@/request.ts";

/** 此处后端没有提供注释 POST /role/add */
export async function addRole(
  body: API.RoleAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString>("/role/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
