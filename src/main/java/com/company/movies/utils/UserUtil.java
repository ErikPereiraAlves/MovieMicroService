package com.company.movies.utils;

import com.company.movies.datatases.Database;
import com.company.movies.datatases.impl.InMemoryDatabase;
import com.company.movies.transferobjects.impl.UserCredentialsDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;


public class UserUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserUtil.class);

    public static UserCredentialsDTO getUser(int loggedInUserId) {

        Database database = InMemoryDatabase.getInstance();

        ConcurrentHashMap<Integer, UserCredentialsDTO> userData = database.getUserData();

        return userData.get(loggedInUserId);

    }

    public static UserCredentialsDTO validateUserHttpRequest(String[] basicAuth) {

        if (null == basicAuth || basicAuth.length != 2 || UserUtil.userCredentialsMissing(basicAuth[0].trim(), basicAuth[1].trim())) {
            return null;

        } else {
            return UserUtil.validateUserCredentials(basicAuth[0].trim(), basicAuth[1].trim());
        }


    }


    private static UserCredentialsDTO validateUserCredentials(String loggedInUser, String loggedInUserPwd) {

        UserCredentialsDTO user = InMemoryDatabase.getInstance().getUserData(loggedInUser, loggedInUserPwd);

        //probably not needed.
        if (null != user && user.getUserName().equals(loggedInUser) && user.getUserPassword().equals(loggedInUserPwd)) {
            return user;
        }

        return null;

    }

    private static boolean userCredentialsMissing(String loggedInUserId, String loggedInUserPwd) {

        return StringUtils.isAnyBlank(loggedInUserId, loggedInUserPwd);
    }

    public enum UserRoles {
        ADMIN,
        USER

    }

}
