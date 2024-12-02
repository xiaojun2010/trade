package imooc8.mall.disruptor.handler.first;

import com.lmax.disruptor.EventHandler;
import imooc8.mall.disruptor.entity.MallOrderEvent;
import imooc8.mall.service.Item.ItemService;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:库存扣减
 */
public class InventoryReduceHandler implements EventHandler<MallOrderEvent> {

    @Override
    public void onEvent(MallOrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        //调用商品相关的service扣库存
        boolean success = ItemService.inventoryReduce(event.getItemId());
        event.setSuccess(success);
    }
}
