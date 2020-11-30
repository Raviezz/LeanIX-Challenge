package com.raviteja.leanix.todo.controllers;


import com.raviteja.leanix.todo.GraphQLProvider;
import graphql.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/lists/")
public class ListController {
    @Autowired
    private GraphQLProvider graphQLService;

    @PostMapping
    public ResponseEntity<Object> getAllLists(@RequestBody String query) {
        ExecutionResult execute = graphQLService.graphQL().execute(query);

        return new ResponseEntity<>(execute, HttpStatus.OK);
    }
}
