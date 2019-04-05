package org.debugroom.sample.continuous.integration.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.debugroom.sample.continuous.integration.domain.model.entity.User;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class TestCoverageRule {


    public String testCoverage(User user, Integer[] testArrayInteger){
        if(Objects.nonNull(user)){
            if(Objects.nonNull(user.getId())){
                if(user.getId() == 0){
                    log.info("The id of this user is 0.");
                }else if(user.getId() == 1){
                    log.info("The id of this user is 1.");
                }else {
                    log.info("The id of this user is " + user.getId());
                }
            }
        }

        for(Integer i : Arrays.asList(testArrayInteger)){
            switch (i){
                case 0 : log.info("The number is 0."); break;
                case 1 : log.info("The number is 1."); break;
                case 2 : log.info("The number is 2."); break;
                case 3 : log.info("The number is 3."); break;
                case 4 : log.info("The number is 4."); break;
                default: log.info("The number is " + i); break;
            }
        }

        Arrays.stream(testArrayInteger).forEach(integer -> {
            if(integer == 0){
                log.info("The number is 0.");
            }else if(integer == 1){
                log.info("The number is 1.");
            }else if(integer == 2){
                log.info("The number is 2.");
            }else if(integer == 3){
                log.info("The number is 3.");
            }else if(integer == 4){
                log.info("The number is 4.");
            }else {
                log.info("The number is " + integer);
            }
        });

        return "";

    }
}
