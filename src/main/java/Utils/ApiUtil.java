package Utils;

import api.ResponseApi;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MultivaluedMap;

public class ApiUtil {

    public static ResponseApi get(String resource, MultivaluedMap<String, String> queryParams, int maxElements) {
        ResponseApi responseApi = null;
        try {
            Client client = Client.create();

            WebResource webResource = client.resource(resource);
            ClientResponse response = webResource
                    .queryParams(queryParams)
                    .accept("application/json")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            if(maxElements > 0)
                responseApi = new ResponseApi(response.getEntity(String.class), response.getStatus(), maxElements);
            else
                responseApi = new ResponseApi(response.getEntity(String.class), response.getStatus(), 0);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            return responseApi;
        }
    }

    public static ResponseApi get(String resource, MultivaluedMap<String, String> queryParams) {
        return get(resource, queryParams, 0);
    }
}
