package tweets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Endpoint
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides available Twitter API v2 endpoints.
 **/
public class Endpoint {

    HashMap<String, String> searchRecent =new HashMap<>(){{put("searchRecent","https://api.twitter.com/2/tweets/search/recent?query=%s");}};
    HashMap<String, String> searchFullArchive = new HashMap<>(){{put("searchFullArchive","https://api.twitter.com/2/tweets/search/all?query=%s");}};
    HashMap<String, String> userLookupById = new HashMap<>(){{put("userLookupById","https://api.twitter.com/2/users%s");}};
    HashMap<String, String> userLookupByName = new HashMap<>(){{put("userLookupByName","https://api.twitter.com/2/users/by/username/%s");}};
    HashMap<String, String> usersLookupByName = new HashMap<>(){{put("usersLookupByName","https://api.twitter.com/2/users/by?usernames=%s");}};
    HashMap<String, String> userTweetTimelineById = new HashMap<>(){{put("userTweetTimelineById","https://api.twitter.com/2/users/%s/tweets");}};
    HashMap<String, String> userMentionTimelineById = new HashMap<>(){{put("userMentionTimelineById","https://api.twitter.com/2/users/%s/mentions");}};
    HashMap<String, String> tweetLookup = new HashMap<>(){{put("tweetLookup","https://api.twitter.com/2/tweets%s");}};
    HashMap<String, String> countRecent = new HashMap<>(){{put("countRecent","https://api.twitter.com/2/tweets/counts/recent?query=%s");}};
    HashMap<String, String> countFullArchive = new HashMap<>(){{put("countFullArchive","https://api.twitter.com/2/tweets/counts/all?query=%s");}};
    HashMap<String, String> followersByUserId = new HashMap<>(){{put("followersByUserId","https://api.twitter.com/2/users/%s/followers");}};
    HashMap<String, String> followingByUserId = new HashMap<>(){{put("followingByUserId","https://api.twitter.com/2/users/%s/following");}};
    HashMap<String, String> liked = new HashMap<>(){{put("liked","https://api.twitter.com/2/users/%s/liked_tweets");}};
    HashMap<String, String> liking = new HashMap<>(){{put("liking","https://api.twitter.com/2/tweets/%s/liking_users");}};
    HashMap<String, String> retweetedByTweetId = new HashMap<>(){{put("retweetedByTweetId","https://api.twitter.com/2/tweets/%s/retweeted_by");}};

    public Endpoint(){}
    public String getEndp() {
        return endp;
    }

    public void setEndp(String endp) {
        this.endp = endp;
    }

    String endp;
    /**
     * List of all endpoints.
     */
    public List<HashMap<String,String>> endpointList = new ArrayList() {{
        add(searchRecent);
        add(searchFullArchive);
        add(userLookupById);
        add(userLookupByName);
        add(usersLookupByName);
        add(userTweetTimelineById);
        add(userMentionTimelineById);
        add(tweetLookup);
        add(countRecent);
        add(countFullArchive);
        add(followersByUserId);
        add(followingByUserId);
        add(liked);
        add(liking);
        add(retweetedByTweetId);

    }};
    /**
     * Find relevant endpoint.
     * @return String
     */
    public static String getEndpoint(String endpointName, List<HashMap<String,String>> list)
    {

        for (var name : list)
        {
            if (name.containsKey(endpointName))
            {
                return name.get(endpointName);
            }
        }
        return null;
    }
}
