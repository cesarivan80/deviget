package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import pojos.Response;

public class ResponseApi {
    private String response;
    private int httpsStatus;
    private Response responseObject;

    public ResponseApi(String response, int httpStatus, int maxElements) throws JsonProcessingException {
        this.httpsStatus = httpStatus;
        if(response != null) {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
            this.responseObject = objectMapper.readValue(response, Response.class);
            if(maxElements > 0){
                this.responseObject.getPhotos().subList(maxElements, this.responseObject.getPhotos().size()).clear();

            }
            String indentResponse = objectMapper.writeValueAsString(this.responseObject);
            this.response = indentResponse;
        }
    }

    public ResponseApi(String response, int httpStatus) throws JsonProcessingException {
        this(response, httpStatus, 0);
    }

    public String getResponse() {
        return response;
    }

    public int getHttpsStatus() {
        return httpsStatus;
    }

    public Response getResponseObject() {
        return responseObject;
    }
}
