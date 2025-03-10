import { requestClient } from '#/api/request';
export interface Dic<T = any> {
  label: string;
  key?: string;
  value: T;
}
export interface TreeDic<T = any> extends Dic<T> {
  children?: TreeDic<T>[];
}
export interface OneLayerTreeDic<T = any, C = any> extends Dic<T> {
  children?: Dic<C>[];
}

let baseUri = '/dic/'
export async function dic(dicName: string) {
  return requestClient.get<Dic[]>(baseUri + dicName);
}

let treeDicUri = '/dic/tree/'
export async function treeDic(dicName: string) {
  return requestClient.get<TreeDic[] | OneLayerTreeDic[]>(treeDicUri + dicName);
}
