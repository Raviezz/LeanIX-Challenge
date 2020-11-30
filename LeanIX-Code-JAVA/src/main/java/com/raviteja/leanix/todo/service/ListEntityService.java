package com.raviteja.leanix.todo.service;

import com.raviteja.leanix.todo.model.db.ListEntity;
import com.raviteja.leanix.todo.repository.ListEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListEntityService {

    @Autowired
    private ListEntityRepository listEntityRepository;

    @Transactional
    public List<ListEntity> getAllListEntities(){
        return this.listEntityRepository.getListEntityByDeletedFalseOrderByPositionAsc();
    }

    @Transactional
    public ListEntity getListEntityById(Long id) throws Exception{
        assert(id != null);
        ListEntity listEntity = this.listEntityRepository.getListEntityByIdAndDeletedFalse(id);
        if(listEntity == null){
            throw new Exception("List Not Found");
        }
        return listEntity;
    }

    @Transactional
    public synchronized ListEntity createListEntity(String name,String description){
        assert(name != null);
        ListEntity listEntity = new ListEntity();
        listEntity.setName(name);
        listEntity.setDescription(description);
        listEntity.setDeleted(false);
        listEntity.setCreatedOn(new Date());
        listEntity.setPosition(getListEntityCount().intValue());
        return saveOrUpdate(listEntity);
    }

    @Transactional
    public ListEntity updateListEntity(ListEntity listEntity,String name,String description){
        assert(listEntity != null && name != null);
        listEntity.setName(name);
        listEntity.setDescription(description);
        return saveOrUpdate(listEntity);
    }

    @Transactional
    public void deleteListEntity(ListEntity listEntity){
        assert(listEntity != null);
        List<ListEntity> toBeShiftedLists = getAllListEntities().stream()
                .filter(i -> i.getPosition() > listEntity.getPosition())
                .collect(Collectors.toList());
        shiftPosition(toBeShiftedLists,-1);
        listEntity.setDeleted(true);
        saveOrUpdate(listEntity);
    }

    @Transactional
    private void shiftPosition(List<ListEntity> listEntities, int step){
        assert(listEntities != null);
        listEntities.stream().forEach(list-> list.setPosition(list.getPosition()+step));
        saveOrUpdate(listEntities);
    }

    @Transactional
    private Long getListEntityCount(){
        return this.listEntityRepository.countListEntityByDeletedFalse();
    }


    @Transactional
    private ListEntity saveOrUpdate(ListEntity listEntity){
        assert(listEntity != null);
        listEntity.setModifiedOn(new Date());
        return this.listEntityRepository.save(listEntity);
    }

    @Transactional
    private void saveOrUpdate(List<ListEntity> listEntities){
        assert(listEntities != null);
        listEntities.stream().forEach(list -> list.setModifiedOn(new Date()));
        this.listEntityRepository.saveAll(listEntities);
    }
}
