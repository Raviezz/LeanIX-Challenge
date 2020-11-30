package com.raviteja.leanix.todo.service;

import com.raviteja.leanix.todo.model.db.Item;
import com.raviteja.leanix.todo.model.db.ListEntity;
import com.raviteja.leanix.todo.model.request.CreateItemRequest;
import com.raviteja.leanix.todo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public List<Item> getItemsByListEntity(ListEntity listEntity){
        assert(listEntity != null);
        return this.itemRepository.getItemByListEntityAndDeletedFalseOrderByPositionAsc(listEntity);
    }

    @Transactional
    public Item getItemById(Long id) throws Exception{
        assert(id != null);
        Item item = this.itemRepository.getItemByIdAndDeletedFalse(id);
        if(item == null){
            throw new Exception("Item Not Found");
        }
        return item;
    }

    @Transactional
    public synchronized Item createItem(CreateItemRequest createItemRequest,ListEntity listEntity){
        assert(listEntity != null);
        Item item = new Item();
        item.setName(createItemRequest.getName());
        item.setDescription(createItemRequest.getDescription());
        item.setListEntity(listEntity);
        item.setDeleted(false);
        item.setCreatedOn(new Date());
        item.setPosition(getItemCountByListEntity(listEntity).intValue());
        return saveOrUpdate(item);
    }

    @Transactional
    public void deleteItem(Item item){
        assert(item != null);
        List<Item> toBeShiftedItems = getItemsByListEntity(item.getListEntity()).stream()
                                        .filter(i -> i.getPosition() > item.getPosition())
                                        .collect(Collectors.toList());
        shiftPosition(toBeShiftedItems,-1);
        item.setDeleted(true);
        saveOrUpdate(item);
    }

    @Transactional
    public void deleteItemsByListEntity(ListEntity listEntity){
        assert(listEntity != null);
        List<Item> itemList = getItemsByListEntity(listEntity);
        itemList.stream().forEach(item -> item.setDeleted(true));
        saveOrUpdate(itemList);
    }

    @Transactional
    private void shiftPosition(List<Item> itemList, int step){
        assert(itemList != null);
        itemList.stream().forEach(item -> item.setPosition(item.getPosition()+step));
        saveOrUpdate(itemList);
    }

    @Transactional
    private Long getItemCountByListEntity(ListEntity listEntity){
        assert(listEntity != null);
        return this.itemRepository.countByListEntityAndDeletedFalse(listEntity);
    }

    @Transactional
    private Item saveOrUpdate(Item item){
        assert(item != null);
        item.setModifiedOn(new Date());
        return this.itemRepository.save(item);
    }

    @Transactional
    private void saveOrUpdate(List<Item> itemList){
        assert(itemList != null);
        itemList.stream().forEach(item -> item.setModifiedOn(new Date()));
        this.itemRepository.saveAll(itemList);
    }
}
