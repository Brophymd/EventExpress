package edu.usf.EventExpress.sync;

import java.util.Date;
import java.util.List;

import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.event.EventType;
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

    public static class UserItem {
        long remote_id;
        String google_id;
        String name;
        Date timestamp;
        int deleted;

        public UserItem(UserCursor cursor) {
            google_id = cursor.getGoogleId();
            name = cursor.getUserName();
            timestamp = cursor.getUserTimestamp();
            deleted = cursor.getUserDeleted();
        }
    }

    public static class EventItem {
        long remote_id;
        String event_owner;
        EventType event_type;
        String event_title;
        String event_description;
        Date event_date;
        String event_address;
        Float event_latitude;
        Float event_longitude;
        Date timestamp;
        int deleted;

        public EventItem(EventCursor cursor) {
            this.event_owner = cursor.getEventOwner();
            this.event_type = cursor.getEventType();
            this.event_title = cursor.getEventTitle();
            this.event_description = cursor.getEventDescription();
            this.event_date = cursor.getEventDate();
            this.event_address = cursor.getEventAddress();
            this.event_latitude = cursor.getEventLatitude();
            this.event_longitude = cursor.getEventLongitude();
            this.timestamp = cursor.getEventTimestamp();
            this.deleted = cursor.getEventDeleted();
        }
    }

    public static class UserItems {
        String latestTimestamp;
        List<UserItem> items;
    }

    public static class EventItems {
        String latestTimestamp;
        List<EventItem> items;
    }

    public static class RegId {
        public String reg_id;
    }

    public static class Dummy {
        // Methods must have return type
    }

    @GET("/people/{id}")
    UserItem getUser(@Header("Authorization") String token,
                     @Path("id") Long remote_id);

    @GET("/people")
    UserItems getUsers(@Header("Authorization") String token,
                       @Query("timestampMin") String timestampMin);

    @POST("/people")
    UserItem addUser(@Header("Authorization") String token,
                     @Body UserItem item);

    @GET("/events/{id}")
    EventItem getEvent(@Header("Authorization") String token,
                       @Path("id") String id);

    @GET("/events")
    EventItems getEvents(@Header("Authorization") String token,
                         @Query("timestampMin") String timestampMin);

    @POST("/events")
    EventItem addEvent(@Header("Authorization") String token,
                       @Body EventItem item);

    @DELETE("/events/{id}")
    Dummy deleteEvent(@Header("Authorization") String token,
                      @Path("id") Long remote_id);

    @POST("/registergcm")
    Dummy registerGCM(@Header("Authorization") String token,
                      @Body RegId regid);
}