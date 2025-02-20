import type {
  VbenFormSchema as FormSchema,
} from '@vben/common-ui';

// [
//   {
//     component: 'Select',
//     componentProps: {
//       options: computed(() => messageTypeDic.value),
//       placeholder: '消息类型',
//     },
//     fieldName: 'messageType',
//     label: '消息类型',
//     rules: 'required',
//   },
//   {
//     component: 'Input',
//     // 对应组件的参数
//     componentProps: {
//       placeholder: '平台名',
//     },
//     // 字段名
//     fieldName: 'platformName',
//     // 界面显示的label
//     label: '平台名',
//     rules: 'required',
//   },
//   {
//     component: 'Input',
//     componentProps: {
//       placeholder: '服务器地址或webhook 地址',
//     },
//     fieldName: 'host',
//     label: '服务器地址或webhook 地址',
//     rules: 'required',
//   },
//   {
//     component: 'InputNumber',
//     componentProps: {
//       placeholder: '请求端口',
//       min: 0,
//       step: 1,
//       'step-strictly': true,
//     },
//     fieldName: 'port',
//     label: '请求端口',
//     dependencies: {
//       if(values) {
//         return values.messageType == 1 || values.messageType == 2;
//       },
//       triggerFields: ['messageType'],
//     },
//     rules: 'required',
//   },
//   {
//     component: 'Input',
//     componentProps: {
//       placeholder: 'appId',
//     },
//     fieldName: 'appId',
//     label: '应用id',
//     dependencies: {
//       if(values) {
//         return values.messageType == 1;
//       },
//       triggerFields: ['messageType'],
//     },
//     rules: 'required',
//   },
//   {
//     component: 'Input',
//     componentProps: {
//       placeholder: 'secretId',
//     },
//     fieldName: 'secretId',
//     label: '访问密钥id',
//     dependencies: {
//       if(values) {
//         return values.messageType == 1;
//       },
//       triggerFields: ['messageType'],
//     },
//     rules: 'required',
//   },
//   {
//     component: 'Input',
//     componentProps: {
//       placeholder: 'secretKey',
//     },
//     fieldName: 'secretKey',
//     label: '访问密钥值或签名密钥',
//     dependencies: {
//       if(values) {
//         return values.messageType == 1 || values.messageType == 3;
//       },
//       triggerFields: ['messageType'],
//     },
//     rules: 'required',
//   },
//   {
//     component: 'Input',
//     componentProps: {
//       placeholder: '签名原文',
//     },
//     fieldName: 'signName',
//     label: '签名原文',
//     dependencies: {
//       if(values) {
//         return values.messageType == 1;
//       },
//       triggerFields: ['messageType'],
//     },
//   },

//   {
//     component: 'Input',
//     componentProps: {
//       placeholder: '处理器(handler)名',
//     },
//     fieldName: 'handlerName',
//     label: '处理器(handler)名',
//     rules: 'required',
//   },
//   {
//     component: 'Input',
//     componentProps: {
//       placeholder: '限流key',
//     },
//     fieldName: 'rateLimitKey',
//     label: '限流key',
//   },
//   {
//     component: 'InputNumber',
//     componentProps: {

//       placeholder: '限流大小',
//       min: 0,
//       step: 1,
//       'step-strictly': true,
//     },
//     fieldName: 'rateLimitScale',
//     label: '限流大小',
//     help: '当限流key不为空时,必填',

//   },
//   {
//     component: 'InputNumber',
//     componentProps: {
//       placeholder: '限流间隔',
//       min: 0,
//       step: 1,
//       'step-strictly': true,
//     },
//     fieldName: 'rateLimitInterval',
//     label: '限流间隔',
//     suffix: () => '单位：秒',
//     help: '当限流key不为空时,必填',
//   },
  
// ]
const emailFormSchemas: FormSchema[] = [
  {
    component: 'Input',
    componentProps: {
      placeholder: '请求地址',
    },
    fieldName: 'host',
    label: '请求地址',
    rules: 'required',
  },
  {
    component: 'InputNumber',
    componentProps: {
      placeholder: '请求端口',
      min: 0,
      step: 1,
      'step-strictly': true,
    },
    fieldName: 'port',
    label: '请求端口',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: '处理器(handler)名',
    },
    fieldName: 'handlerName',
    label: '处理器(handler)名',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: '限流key',
    },
    fieldName: 'rateLimitKey',
    label: '限流key',
  },
  {
    component: 'InputNumber',
    componentProps: {

      placeholder: '限流大小',
      min: 0,
      step: 1,
      'step-strictly': true,
    },
    fieldName: 'rateLimitScale',
    label: '限流大小',
    help: '当限流key不为空时,必填',

  },
  {
    component: 'InputNumber',
    componentProps: {
      placeholder: '限流间隔',
      min: 0,
      step: 1,
      'step-strictly': true,
    },
    fieldName: 'rateLimitInterval',
    label: '限流间隔',
    suffix: () => '单位：秒',
    help: '当限流key不为空时,必填',
  },
  
]
const smsFormSchemas: FormSchema[] = [
  {
    component: 'Input',
    // 对应组件的参数
    componentProps: {
      placeholder: '平台名',
    },
    // 字段名
    fieldName: 'platformName',
    // 界面显示的label
    label: '平台名',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: '请求地址',
    },
    fieldName: 'host',
    label: '请求地址',
    rules: 'required',
  },
  {
    component: 'InputNumber',
    componentProps: {
      placeholder: '请求端口',
      min: 0,
      step: 1,
      'step-strictly': true,
    },
    fieldName: 'port',
    label: '请求端口',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: 'appId',
    },
    fieldName: 'appId',
    label: '应用id',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: 'secretId',
    },
    fieldName: 'secretId',
    label: '访问密钥id',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: 'secretKey',
    },
    fieldName: 'secretKey',
    label: '访问密钥值或签名密钥',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: '签名原文',
    },
    fieldName: 'signName',
    label: '签名原文',
  },

  {
    component: 'Input',
    componentProps: {
      placeholder: '处理器(handler)名',
    },
    fieldName: 'handlerName',
    label: '处理器(handler)名',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: '限流key',
    },
    fieldName: 'rateLimitKey',
    label: '限流key',
  },
  {
    component: 'InputNumber',
    componentProps: {

      placeholder: '限流大小',
      min: 0,
      step: 1,
      'step-strictly': true,
    },
    fieldName: 'rateLimitScale',
    label: '限流大小',
    help: '当限流key不为空时,必填',

  },
  {
    component: 'InputNumber',
    componentProps: {
      placeholder: '限流间隔',
      min: 0,
      step: 1,
      'step-strictly': true,
    },
    fieldName: 'rateLimitInterval',
    label: '限流间隔',
    suffix: () => '单位：秒',
    help: '当限流key不为空时,必填',
  },
  
]
const robotFormSchemas: FormSchema[] = [
  {
    component: 'Input',
    // 对应组件的参数
    componentProps: {
      placeholder: '平台名',
    },
    // 字段名
    fieldName: 'platformName',
    // 界面显示的label
    label: '平台名',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: '请求地址',
    },
    fieldName: 'host',
    label: '请求地址',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: 'secretKey',
    },
    fieldName: 'secretKey',
    label: '访问密钥值或签名密钥',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: '处理器(handler)名',
    },
    fieldName: 'handlerName',
    label: '处理器(handler)名',
    rules: 'required',
  },
  {
    component: 'Input',
    componentProps: {
      placeholder: '限流key',
    },
    fieldName: 'rateLimitKey',
    label: '限流key',
  },
  {
    component: 'InputNumber',
    componentProps: {

      placeholder: '限流大小',
      min: 0,
      step: 1,
      'step-strictly': true,
    },
    fieldName: 'rateLimitScale',
    label: '限流大小',
    help: '当限流key不为空时,必填',

  },
  {
    component: 'InputNumber',
    componentProps: {
      placeholder: '限流间隔',
      min: 0,
      step: 1,
      'step-strictly': true,
    },
    fieldName: 'rateLimitInterval',
    label: '限流间隔',
    suffix: () => '单位：秒',
    help: '当限流key不为空时,必填',
  },
  
]

export const FormSchemaMap = new Map([
  [2, emailFormSchemas],
  [1, smsFormSchemas],
  [3, robotFormSchemas],
])
