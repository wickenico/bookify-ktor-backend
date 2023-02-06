/*
 * com_nw_bookify_ktor_backend API
 * com_nw_bookify_ktor_backend API
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import java.math.BigDecimal;
import org.openapitools.client.model.Book;
import org.openapitools.client.model.Tag;
import org.openapitools.client.model.User;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DefaultApi
 */
@Ignore
public class DefaultApiTest {

    private final DefaultApi api = new DefaultApi();

    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1BooksGetTest() throws ApiException {
        List<Book> response = api.apiV1BooksGet();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1BooksIdDeleteTest() throws ApiException {
        Integer id = null;
        String response = api.apiV1BooksIdDelete(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1BooksIdGetTest() throws ApiException {
        String id = null;
        Book response = api.apiV1BooksIdGet(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1RegistrationPostTest() throws ApiException {
        User response = api.apiV1RegistrationPost();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1SearchGetTest() throws ApiException {
        BigDecimal isbn = null;
        String response = api.apiV1SearchGet(isbn);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1TagsGetTest() throws ApiException {
        List<Tag> response = api.apiV1TagsGet();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1TagsIdDeleteTest() throws ApiException {
        Integer id = null;
        String response = api.apiV1TagsIdDelete(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1TagsIdGetTest() throws ApiException {
        String id = null;
        Tag response = api.apiV1TagsIdGet(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1TagsPostTest() throws ApiException {
        Tag response = api.apiV1TagsPost();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1UsersGetTest() throws ApiException {
        List<User> response = api.apiV1UsersGet();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1UsersIdDeleteTest() throws ApiException {
        Integer id = null;
        String response = api.apiV1UsersIdDelete(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1UsersIdGetTest() throws ApiException {
        String id = null;
        User response = api.apiV1UsersIdGet(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void jsonJacksonGetTest() throws ApiException {
        java.util.Map response = api.jsonJacksonGet();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void jsonKotlinxSerializationGetTest() throws ApiException {
        java.util.Map response = api.jsonKotlinxSerializationGet();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void rootGetTest() throws ApiException {
        String response = api.rootGet();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void sessionIncrementGetTest() throws ApiException {
        String response = api.sessionIncrementGet();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Delete user
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void usersIdDeleteTest() throws ApiException {
        Integer id = null;
        Object response = api.usersIdDelete(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Read user
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void usersIdGetTest() throws ApiException {
        Integer id = null;
        User response = api.usersIdGet(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Update user
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void usersIdPutTest() throws ApiException {
        Integer id = null;
        Object response = api.usersIdPut(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Create user
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void usersPostTest() throws ApiException {
        Integer response = api.usersPost();

        // TODO: test validations
    }
    
}
