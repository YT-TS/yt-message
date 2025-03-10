
import { ref } from 'vue'
import { dic, type Dic } from '#/api/dic'
import { defineStore } from 'pinia';

export const useDicStore = defineStore('dic', {
  state: () => ({
    dict: new Array()
  }),
  actions: {

    // 获取字典
    getDic(_key: string) {
      const dict = ref<Dic[]>([]);
      for (let i = 0; i < this.dict.length; i++) {
        if (this.dict[i].key == _key) {
          dict.value = this.dict[i].value;
        }
      }
      if (dict.value.length == 0) {
        dic(_key).then(res => {
          dict.value = res;
          this.setDic(_key, res);
        });
      }
      return dict;
    },
    // 设置字典
    setDic(_key: string, value: Dic[]) {
      if (_key !== null && _key !== "") {
        this.dict.push({
          key: _key,
          value: value
        });
      }
    },
    // 删除字典
    removeDic(_key: string) {
      for (let i = 0; i < this.dict.length; i++) {
        if (this.dict[i].key == _key) {
          this.dict.splice(i, 1);
        }
      }
    },
  }
})
