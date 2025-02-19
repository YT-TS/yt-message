import { requestClient } from '#/api/request';
const baseUri = '/system'


export async function page(data:any) {
  return requestClient.post<any>(baseUri + '/page', data);
}
