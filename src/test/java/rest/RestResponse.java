package rest;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.function.Function;

public class RestResponse<T> {
    private Response response;
    private Function<Response, T> extractor;

    public RestResponse(Response response, Function<Response, T> extractor){
        this.response = response;
        this.extractor = extractor;
    }

    public void validate(String schemaPath){
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }

    public T extract(){
        return extractor.apply(response);
    }
}
