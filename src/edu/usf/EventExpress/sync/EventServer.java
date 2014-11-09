package edu.usf.EventExpress.sync;

import java.util.List;

import edu.usf.EventExpress.provider.EventProvider;

import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.user.UserColumns;
import edu.usf.EventExpress.provider.user.UserCursor;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Micah on 11/7/2014.
 */

public interface EventServer {

    /**
     * Change the IP to the address of your server
     */
    // Server-app uses no prefixes in the URL
    public static final String API_URL = "https://www.eventexpress.me/api";

    public static class EventItems {
        String latestTimestamp;
        List<EventColumns> items;
    }

    public static class RegId {
        public String regid;
    }

    public static class Dummy {
        // Methods must have return type
    }

    @GET("/people/{remote_id}")
    UserColumns getUser(@Header("Authorization") String token,
                        @Path("id") Integer remote_id);

    @POST("/people")
    UserColumns addUser(@Header("Authorization") String token,
                        @Body UserCursor item);

    @GET("/events/{id}")
    EventColumns getEvent(@Header("Authorization") String token,
                         @Path("id") String id);

    @GET("/events")
    EventItems getEvents(@Header("Authorization") String token,
                          @Query("timestampMin") String timestampMin);

    @POST("/registergcm")
    Dummy registerGCM(@Header("Authorization") String token,
                      @Body RegId regid);
}