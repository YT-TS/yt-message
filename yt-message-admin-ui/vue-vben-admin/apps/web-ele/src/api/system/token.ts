import { requestClient } from '#/api/request';
const baseUri = '/platform/token'
export async function list() {
  return requestClient.get<any[]>(baseUri);
}
export async function update(id:any) {
  return requestClient.put<any[]>(baseUri+'/'+id);
}
