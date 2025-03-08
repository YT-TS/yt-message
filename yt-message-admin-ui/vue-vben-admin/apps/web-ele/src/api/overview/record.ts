import type { BasePageReqParams } from "../common/types";
import { requestClient } from '#/api/request';

export namespace recordApi {
  export let baseUri = '/record'

  /** 分页参数 */
  export interface pageParams extends BasePageReqParams {
    
  }
  export interface Record {
    templateId: string;
    messageType: number;
    biz_id: string;
    step:number;
    hand_timestamp: string;
  }
  export interface TrackStatus {
    issue?:boolean
    issueTimestamp?:number
    issueNote?:string
    send?:boolean
    sendTimestamp?:number
    sendNote?:string
  }
  
}

export async function page(data: recordApi.pageParams) {
  return requestClient.post<any>(recordApi.baseUri + '/page', data);
}
export async function track(id:any) {
  return requestClient.get<any>(recordApi.baseUri + '/track/'+ id);
}

