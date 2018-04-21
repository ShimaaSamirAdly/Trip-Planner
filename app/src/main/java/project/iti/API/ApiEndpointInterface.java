package project.iti.API;

/**
 * Created by asmaa on 02/26/2018.
 */

import project.iti.Data.Model.Map.MapModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * interface to all retrofit requests , type of method request and parameters or request body
 * and handle this with model classes to parse json response to pojo classes
 */
public interface ApiEndpointInterface {

//    @GET(APIUrls.TEST)
//    Call<Data> getCountries();

    @GET(APIUrls.DIRECTION_MAP)
    Call<MapModel> getMapData(@Query("origin") String origin,
                              @Query("destination") String destination,
                              @Query("key") String key);

}
