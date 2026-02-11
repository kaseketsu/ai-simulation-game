// @ts-ignore
/* eslint-disable */
import request from "@/request.ts";

/** 此处后端没有提供注释 POST /spiritualMaterials/batchAdd */
export async function batchAddBaseSpiritualMaterials(
  body: API.BaseSpiritualMaterialsAddReq[],
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString>("/spiritualMaterials/batchAdd", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
