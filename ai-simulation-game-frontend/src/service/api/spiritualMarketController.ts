// @ts-ignore
/* eslint-disable */
import request from "@/request.ts";

/** 此处后端没有提供注释 POST /market/buy/spiritualMaterial */
export async function buySpiritualMaterial(
  body: API.BuyMaterialRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString>("/market/buy/spiritualMaterial", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /market/query/base/spiritualMaterial */
export async function queryBaseSpiritualMaterials(
  body: API.SpiritualMaterialBaseRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageVOSpiritualMaterialAllCat>(
    "/market/query/base/spiritualMaterial",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}
