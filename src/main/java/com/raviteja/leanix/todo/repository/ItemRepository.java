package com.raviteja.leanix.todo.repository;

import com.raviteja.leanix.todo.model.db.Item;
import com.raviteja.leanix.todo.model.db.ListEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item,Long> {
    List<Item> getItemByListEntityAndDeletedFalseOrderByPositionAsc(ListEntity listEntity);
    Long countByListEntityAndDeletedFalse(ListEntity listEntity);
    Item getItemByIdAndDeletedFalse(Long id);
}
