interface BasePageReqParams{
  pageNum? : number;
  pageSize? : number; 
}
interface PageRsp<T = any>{
  pageNum : number;
  pageSize : number; 
  pageCount : number;
  totalCount : number;
  records : T[];

}

export type {BasePageReqParams,PageRsp}
