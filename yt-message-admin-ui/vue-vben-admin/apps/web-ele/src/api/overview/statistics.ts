import { requestClient } from '#/api/request';
const baseUri = '/statistics'
export async function hour() {
  return requestClient.get<any>(baseUri + '/hour');
}
export async function total() {
  return requestClient.get<any>(baseUri + '/total');
}

export async function today() {
  return requestClient.get<any>(baseUri + '/today');
}
