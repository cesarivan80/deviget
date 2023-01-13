import Utils.ApiUtil;
import api.ResponseApi;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import pojos.Photos;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class NasaTests {

    private String api_key = "8BXbdYMMh7fcumbRCqGZt0Lafd2biA6ZIowgkoCJ";
    private String resource = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";

    @Test(description = "Retrieve the first 10 Mars photos made by 'Curiosity' on 1000 Martian sol.")
    public void test1(){
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("api_key", api_key);
        params.add("sol", "1000");
        params.add("name", "Curiosity");
        params.add("page", "1");

        ResponseApi responseApi = ApiUtil.get(resource, params, 10);

        assertEquals(responseApi.getHttpsStatus(), 200);

        System.out.println(responseApi.getResponse());
    }

    @Test(description = "Retrieve the first 10 Mars photos made by 'Curiosity' on Earth date equal to 1000 Martian sol.")
    public void test2(){
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("api_key", api_key);
        params.add("sol", "1000");
        params.add("name", "Curiosity");
        params.add("page", "1");
        params.add("earth_date", "2015-05-30");

        ResponseApi responseApi = ApiUtil.get(resource, params, 10);

        assertEquals(responseApi.getHttpsStatus(), 200);

        System.out.println(responseApi.getResponse());
    }

    @Test(description = "Retrieve and compare the first 10 Mars photos made by 'Curiosity' on 1000 sol and on Earth date equal to 1000 Martian sol.")
    public void test3(){
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("api_key", api_key);
        params.add("sol", "1000");
        params.add("name", "Curiosity");
        params.add("earth_date", "2015-05-30");
        params.add("page", "1");

        ResponseApi responseApi = ApiUtil.get(resource, params);

        assertEquals(responseApi.getHttpsStatus(), 200);

        System.out.println(responseApi.getResponse());

        for(Photos photo : responseApi.getResponseObject().getPhotos()){
            assertEquals(photo.getRover().getName(), "Curiosity");
            assertEquals(photo.getSol(), 1000);
            assertEquals(photo.getEarth_date(), "2015-05-30");
        }
    }

    @Test(description = "Validate that the amounts of pictures that each 'Curiosity' camera took on 1000 Mars sol is not greater than 10 times the amount taken by other cameras on the same date.")
    public void test4(){
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("api_key", api_key);
        params.add("sol", "1000");
        params.add("name", "Curiosity");
        params.add("page", "1");
        params.add("earth_date", "2015-05-30");
        params.add("camera", "");

        List<String> cameraAbbreviations = Lists.newArrayList("FHAZ", "RHAZ", "CHEMCAM", "NAVCAM", "MAST", "MAHLI", "MARDI");

        for(String cameraAbbreviation : cameraAbbreviations){
            params.remove("camera");
            params.add("camera", cameraAbbreviation);

            ResponseApi responseApi = ApiUtil.get(resource, params);

            assertEquals(responseApi.getHttpsStatus(), 200);

            System.out.println("----------------------------------------------------");
            System.out.println(cameraAbbreviation + " contains: " + responseApi.getResponseObject().getPhotos().size());
            System.out.println("----------------------------------------------------");

            assertTrue(responseApi.getResponseObject().getPhotos().size() <= 10, cameraAbbreviation + " " + "get more than 10 registers");

            System.out.println(responseApi.getResponse());}
    }
}
