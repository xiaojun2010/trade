package imooc8.mall.service.Item;


import imooc8.mall.service.Item.entity.Item;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class ItemService {
    private static Item item = new Item(1L,new AtomicLong(10),"iphone");

    /**
     * 库存扣减
     * @param itemId
     * @return
     */
    public static boolean inventoryReduce(Long itemId){
        if(Objects.isNull(itemId)||itemId<0){
            return false;
        }
        if(item.getId().equals(itemId)){
            AtomicLong inventory = item.getInventory();
            while (true){
              long value = inventory.get();
              if(value>0){
                  //使用cas扣减库存
                  if(inventory.compareAndSet(value,value-1)){
                      return true;//库存减少成功
                  }else{
                      //cas 失败，需要重试
                  }
              }else{
                  return false; //库存不足
              }
            }
        }else{
            return false;//商品id不匹配
        }
    }
}