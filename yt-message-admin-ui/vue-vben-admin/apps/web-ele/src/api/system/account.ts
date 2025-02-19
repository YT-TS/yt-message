import type { BasePageReqParams, PageRsp } from "../common/types";
import { requestClient } from '#/api/request';
export namespace accountApi {
  export let baseUri = '/account-group'

  /** 分页参数 */
  export interface pageParams extends BasePageReqParams {
    type: number;
  }
  export interface AccountGroupRsp {
    groupId: string;
    groupName: string;
    members: string;
    type: number;
  }
}
export async function page(data: accountApi.pageParams) {
  return requestClient.post<PageRsp<accountApi.AccountGroupRsp>>(accountApi.baseUri + '/page', data);
}
export async function update(data: any) {
  return requestClient.put(accountApi.baseUri, data)
}
export async function add(data: any) {
  return requestClient.post(accountApi.baseUri, data)
}
export async function remove(id : string) {
  return requestClient.delete(accountApi.baseUri + '/' + id)
}
export async function members(id : string) {
  return requestClient.get(accountApi.baseUri + '/members/' + id)
}
