package com.github.blade2021.workerBee.service;

import com.github.blade2021.workerBee.domain.UserObject;
import org.apache.catalina.User;

public interface UserObjectService {


    UserObject saveUserObject(UserObject userObject);

    UserObject getUserObject(Long dUID);
    UserObject getUserObject(String dUID);

    void deleteUserObjectBydUID(Long dUID);
    void deleteUserObjectById(Long id);

}
