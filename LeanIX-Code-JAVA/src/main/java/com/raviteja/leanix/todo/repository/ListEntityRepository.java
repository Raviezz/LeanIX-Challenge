package com.raviteja.leanix.todo.repository;

import com.raviteja.leanix.todo.model.db.ListEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListEntityRepository extends CrudRepository<ListEntity,Long> {
    List<ListEntity> getListEntityByDeletedFalseOrderByPositionAsc();
    Long countListEntityByDeletedFalse();
    ListEntity getListEntityByIdAndDeletedFalse(Long id);
}
