import type { BasePageReqParams, PageRsp } from "../common/types";
import { requestClient } from '#/api/request';
export namespace attachmentApi {
  export const baseUri = '/attachment'

  /** 分页参数 */
  export interface pageParams extends BasePageReqParams {
    type: number;
  }
  export interface  AttachmentRsp {
    attachmnetId: string;

    originName :string;

    /**
     * 文件类型
     */
    type:number;
    /**
     * 文件大小 单位字节
     */
    size:number;
  

    createTime:string;
}
  
}
export async function page(data: attachmentApi.pageParams) {
  return requestClient.post<PageRsp<attachmentApi.AttachmentRsp>>(attachmentApi.baseUri + '/page', data);
}
export async function upload(data: any) {
  return requestClient.post(attachmentApi.baseUri + '/upload', data,{
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export async function remove(id : string) {
  return requestClient.delete(attachmentApi.baseUri + '/' + id)
}
export async function url(id : string,expiry:number) {
  return requestClient.get(attachmentApi.baseUri + '/url/' + id + '/' + expiry)
}
