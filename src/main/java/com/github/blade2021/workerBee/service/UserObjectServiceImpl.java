package com.github.blade2021.workerBee.service;

import com.github.blade2021.workerBee.domain.UserObject;
import com.github.blade2021.workerBee.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class UserObjectServiceImpl implements UserObjectService{

    private final UserRepo userRepo;

    @Override
    public UserObject saveUserObject(UserObject userObject) {
        return userRepo.save(userObject);
    }

    @Override
    public UserObject getUserObject(Long dUID) {
        return userRepo.findUserBydUID(dUID);
    }

    public UserObject getUserObject(String dUID) {
        return userRepo.findUserBydUID(Long.parseLong(dUID));
    }

    @Override
    public void deleteUserObjectBydUID(Long dUID) {
        userRepo.deleteUserBydUID(dUID);
    }

    @Override
    public void deleteUserObjectById(Long id) {
        userRepo.deleteUserById(id);
    }
}
