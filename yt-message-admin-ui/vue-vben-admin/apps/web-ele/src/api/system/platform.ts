import type { BasePageReqParams, PageRsp } from "../common/types";
import { requestClient } from '#/api/request';
export namespace platformApi {
  export const baseUri = '/platform'

  /** 分页参数 */
  export interface pageParams extends BasePageReqParams {
    messageType: number;
  }
  export interface PlatformRsp {
    platformId: string;
    platformName: string;
    messageType: number;
    rateLimitKey: string;
    rateLimitScale: number;
    rateLimitInterval: number;
    signName: string;
    host: string;
    port: number;
    handlerName: string;
    version:number;
    
  }
}
export async function page(data: platformApi.pageParams) {
  return requestClient.post<PageRsp<platformApi.PlatformRsp>>(platformApi.baseUri + '/page', data);
}
export async function update(data: any) {
  return requestClient.put(platformApi.baseUri, data)
}
export async function add(data: any) {
  return requestClient.post(platformApi.baseUri, data)
}
export async function remove(id : string) {
  return requestClient.delete(platformApi.baseUri + '/' + id)
}
export async function status(data:any) {
  return requestClient.patch(platformApi.baseUri + '/status', data)
  
}
