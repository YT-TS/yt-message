import type { BasePageReqParams, PageRsp } from "../common/types";
import { requestClient } from '#/api/request';
export namespace preparedTemplateApi {
  export let baseUri = '/preparedTemplate'

  /** 分页参数 */
  export interface pageParams extends BasePageReqParams {
    templateId:string
    
  }
  export interface PreparedTemplateRsp {
    preparedTemplateId :string;

    templateName :string;

    templateId :string;

    subjectParams :any;


    contentParams:any;


    receiveAccounts:string;


    accountGroupId:string;

    requireAsync :number;
    messageType?: number
  }
}
export async function page(data: preparedTemplateApi.pageParams) {
  return requestClient.post<PageRsp<preparedTemplateApi.PreparedTemplateRsp>>(preparedTemplateApi.baseUri + '/page', data);
}
export async function update(data: any) {
  return requestClient.put(preparedTemplateApi.baseUri, data)
}
export async function add(data: any) {
  return requestClient.post(preparedTemplateApi.baseUri, data)
}
export async function remove(id : string) {
  return requestClient.delete(preparedTemplateApi.baseUri + '/' + id)
}
export async function send(id : string) {
  return requestClient.post(preparedTemplateApi.baseUri + '/send/' + id)
}
