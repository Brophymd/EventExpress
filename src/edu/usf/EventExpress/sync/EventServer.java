package edu.usf.EventExpress.sync;

import java.util.Date;
import java.util.List;

import android.util.Log;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.event.EventType;
import edu.usf.EventExpress.provider.eventmembers.EventMembersCursor;
import edu.usf.EventExpress.provider.eventmembers.RSVPStatus;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusCursor;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusType;
import edu.usf.EventExpress.provider.user.UserCursor;

import retrofit.http.*;

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
        String google_id;
        String user_email;
        String name;
        Date timestamp;
        int deleted;

        public UserItem(UserCursor cursor) {
            google_id = cursor.getGoogleId();
            user_email = cursor.getUserEmail();
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
            this.remote_id = cursor.getEventRemoteId();
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

    public static class EventMembersItem {
        long event_id;
        String user_id;
        RSVPStatus rsvp_status;
        Date timestamp;
        int deleted;

        public EventMembersItem(EventMembersCursor cursor) {
            // should rename the remote ids to be table specific
            // e.g., getRemoteId here would be better named getEventRemoteId
            this.event_id = cursor.getAttendeesRemoteId();
            this.user_id = cursor.getUserId();
            this.rsvp_status = cursor.getRsvpStatus();
            this.timestamp = cursor.getEventMembersTimestamp();
            this.deleted = cursor.getEventMembersDeleted();
        }
    }

    public static class FriendStatusItem {
        long remote_id;
        String from_user_id;
        String to_user_id;
        FriendStatusType status;
        Date sent_time;
        Date response_time;
        Date timestamp;
        int deleted;

        public FriendStatusItem(FriendStatusCursor cursor) {
            this.remote_id = cursor.getFriendsRemoteId();
            this.from_user_id = cursor.getFromUserId();
            this.to_user_id = cursor.getToUserId();
            this.status = cursor.getStatus();
            this.sent_time = cursor.getSentTime();
            this.response_time = cursor.getResponseTime();
            this.timestamp = cursor.getFriendStatusTimestamp();
            this.deleted = cursor.getFriendStatusDeleted();
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

    public static class EventMembersItems {
        String latestTimestamp;
        List<EventMembersItem> items;
    }

    public static class FriendStatusItems {
        String latestTimestamp;
        List<FriendStatusItem> items;
    }

    public static class RegId {
        public String reg_id;
    }

    public static class Dummy {
        // Methods must have return type
    }

    @GET("/people/{id}")
    UserItem getUser(@Header("Authorization") String token,
                     @Path("id") String google_id);

    @GET("/people")
    UserItems getUsers(@Header("Authorization") String token,
                       @Query("user_email") String user_email);

    @POST("/people")
    UserItem addUser(@Header("Authorization") String token,
                     @Body UserItem item);

    @GET("/events/{remote_id}")
    EventItem getEvent(@Header("Authorization") String token,
                       @Path("remote_id") String id);

    @GET("/events")
    EventItems getEvents(@Header("Authorization") String token,
                         @Query("timestampMin") String timestampMin);

    @POST("/events")
    EventItem addEvent(@Header("Authorization") String token,
                       @Body EventItem item);

    @DELETE("/events/{remote_id}")
    Dummy deleteEvent(@Header("Authorization") String token,
                      @Path("remote_id") Long remote_id);

    @GET("/attendees/{event_id}")
    EventMembersItem getAttendee(@Header("Authorization") String token,
                                 @Path("event_id") Long remote_id);

    @GET("/attendees")
    EventMembersItems getAttendees(@Header("Authorization") String token,
                                   @Query("timestampMin") String timestampMin);

    @POST("/attendees")
    EventMembersItem addAttendee(@Header("Authorization") String token,
                                  @Body EventMembersItem item);

    @PATCH("/attendees/{event_id}")
    EventMembersItem updateAttendee(@Header("Authorization") String token,
                                    @Path("event_id") Long remote_id,
                                    @Body EventMembersItem item);

    @GET("/friends/{remote_id}")
    FriendStatusItem getFriend(@Header("Authorization") String token,
                                 @Path("remote_id") Long remote_id);

    @GET("/friends")
    FriendStatusItems getFriends(@Header("Authorization") String token,
                                   @Query("timestampMin") String timestampMin);

    @POST("/friends")
    FriendStatusItem addFriend(@Header("Authorization") String token,
                                 @Body FriendStatusItem item);

    @PATCH("/friends/{remote_id}")
    FriendStatusItem updateFriend(@Header("Authorization") String token,
                                    @Path("remote_id") Long remote_id,
                                    @Body FriendStatusItem item);

    @POST("/registergcm")
    Dummy registerGCM(@Header("Authorization") String token,
                      @Body RegId regid);
}