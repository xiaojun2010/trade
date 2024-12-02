package imooc8.mall.service.Item.entity;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Alfred
 * @ModuleOwner: Alfred
 * @Description:
 */
public class Item {
    /**
     * 商品id
     */
    private Long id;
    /**
     * 库存
     */
    private AtomicLong inventory;
    /**
     * 商品名称
     */
    private String itemName;

    public Item(Long id, AtomicLong inventory, String itemName) {
        this.id = id;
        this.inventory = inventory;
        this.itemName = itemName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AtomicLong getInventory() {
        return inventory;
    }

    public void setInventory(AtomicLong inventory) {
        this.inventory = inventory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
