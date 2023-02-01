package org.example;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface FeignClient{
    @RequestLine("GET /{id}")
    SingleUser findById(@Param("id") int id);
    @RequestLine("GET ?page={page}")
    AllUsers findAll(@Param("page") int page);
    @RequestLine("POST")
    @Headers(("Content-Type: application/json"))
    CreateDataResponse create(CreateData createData);
    @RequestLine("PUT /{id}")
    @Headers(("Content-Type: application/json"))
    UpdateDataResponse update(@Param("id") int id, CreateData createData);
    @RequestLine("DELETE /{id}")
    void delete(@Param("id") int id);
}

