import type { BasePageReqParams, PageRsp } from "../common/types";
import { requestClient } from '#/api/request';

export namespace templateApi {
  export const baseUri = '/template'

  /** 分页参数 */
  export interface pageParams extends BasePageReqParams {

  }
  export interface TemplateRsp {
    /**
  * 模板id
  */

    templateId: string;

    /**
     * 模板名
     */
    templateName: string

    /**
     * 平台id
     */
    platformId: string

    /**
     * 对应第三方平台模板id
     */
    platformTemplateId: string

    /**
     * message_usage
     */
    messageUsage: number

    /**
     * 是否需要持久化
     */
    requirePersist: number

    /**
     * 是否需要验证重复消费
     */
    requireRepeatConsume: number

  

    /**
     * 是否需要限流
     */

    requireRateLimit: number

    rateLimitStrategy?: number;

    /**
     * 发送账户
     */

    sendAccount: string

    /**
     * 用户名
     */
    username: string

    /**
     * 密码
     */
    password: string

    /**
     * 是否需要ssl发送
     */

    requireSsl: number


    /**
     * 标题(可包含占位符{})
     */

    subject: string


    /**
     * 正文(可包含占位符{})
     */

    content: string

    /**
     * 正文内容是否是html格式
     */

    requireHtml: number

    messageType?: number
    version:number;
  }
}

export async function page(data: templateApi.pageParams) {
  return requestClient.post<PageRsp<templateApi.TemplateRsp>>(templateApi.baseUri + '/page', data);
}
export async function update(data: any) {
  return requestClient.put(templateApi.baseUri, data)
}
export async function create(data: any) {
  return requestClient.post(templateApi.baseUri, data)
}
export async function remove(id : string) {
  return requestClient.delete(templateApi.baseUri + '/' + id)
}
export async function sendMessage(data: any) {
  return requestClient.post(templateApi.baseUri+'/send', data)
}
