package com.raviteja.leanix.todo.model.request;

import lombok.Data;

import java.util.Map;

@Data
public class CreateItemRequest {
    private String name;
    private Long listId;
    private String description;

    public static CreateItemRequest getCreateItemRequest(Map map){
        CreateItemRequest request = new CreateItemRequest();
        request.setName((String) map.get("name"));
        request.setDescription((String) map.get("description"));
        request.setListId(Long.valueOf((Integer)map.get("listId")));
        return request;
    }
}
