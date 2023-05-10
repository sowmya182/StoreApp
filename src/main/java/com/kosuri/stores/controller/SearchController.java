package com.kosuri.stores.controller;

import com.kosuri.stores.handler.SearchHandler;
import com.kosuri.stores.model.response.SearchResponse;
import com.kosuri.stores.model.search.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    SearchHandler searchHandler;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<SearchResponse> search(@RequestParam("medicine") String medicine, @RequestParam("location") String location,
                                                 @RequestParam(value = "category") String category) {
        List<SearchResult> searchResultList = searchHandler.search(medicine, location, category);
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setSearchResultList(searchResultList);
        return ResponseEntity.status(HttpStatus.OK).body(searchResponse);
    }
}


