// @ts-ignore
/* eslint-disable */
import request from "@/request.ts";

/** 此处后端没有提供注释 POST /dish/add/seasoning/batch */
export async function addSeasoningByBatch(
  body: API.SeasoningBatchAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString>("/dish/add/seasoning/batch", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /dish/createNewMeal */
export async function createNewMeal(
  body: API.NewMealGenerateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseNewMaelInfoVO>("/dish/createNewMeal", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /dish/fetch/spiritualSeasoning */
export async function fetchSpiritualSeasoningByPage(
  body: API.PageRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageVOSpiritualSeasoningBase>(
    "/dish/fetch/spiritualSeasoning",
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

/** 此处后端没有提供注释 POST /dish/fetchMaterials */
export async function fetchMaterials(
  body: API.SpiritualRepoQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageVOMaterialVO>("/dish/fetchMaterials", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
