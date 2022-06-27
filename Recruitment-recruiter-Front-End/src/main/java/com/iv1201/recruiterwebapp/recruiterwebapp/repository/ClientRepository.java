package com.iv1201.recruiterwebapp.recruiterwebapp.repository;

import com.iv1201.recruiterwebapp.recruiterwebapp.model.ClientTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class handles all the active cookie string tokens assigned to
 * each current user upon login or after expiration or logout and removal
 * of previous token.
 */
@Repository
public class ClientRepository {
    private static Map<String, ClientTemplate> userMap = new ConcurrentHashMap<>();

    /**
     * Finds a user based on userId/userToken.
     * @param id The token a user is identified with, the userToken from REST api.
     * @return the found user, or null if no found user.
     */
    public ClientTemplate find(String id)  {
        return userMap.get(id);
    }

    /**
     * Saves a user in the client hashmap.
     * @param id The token a user is identified with, the userToken from REST api.
     * @param object A client template
     * @return the client template user was put into
     */
    public ClientTemplate save(String id, ClientTemplate object)  {
        return userMap.put(id,object);
    }

    /**
     * Deletes a user from the client hashmap.
     * @param id The token a user is identified with, the userToken from REST api.
     * @return True if the user was successfully removed, false if user was not found therefore cannot be removed
     */
    public boolean delete(String id)  {
        if(exists(id)){
            userMap.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Checks if user exists in client hashmap.
     * @param id The token a user is identified with, the userToken from REST api.
     * @return True if user exists, false if not.
     */
    public boolean exists(String id)  {
        return userMap.containsKey(id);
    }



}
