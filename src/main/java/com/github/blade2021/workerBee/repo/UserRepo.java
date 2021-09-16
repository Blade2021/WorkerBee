package com.github.blade2021.workerBee.repo;

import com.github.blade2021.workerBee.domain.UserObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserObject, Long> {

    UserObject findUserBydUID(Long dUID);
    UserObject findUserById(Long id);

    void deleteUserById(Long id);
    void deleteUserBydUID(Long dUID);

}
