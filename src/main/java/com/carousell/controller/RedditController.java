package com.carousell.controller;

import com.carousell.beans.TopicDTO;
import com.carousell.beans.UserDTO;
import com.carousell.beans.VoteDTO;
import com.carousell.entities.Topic;
import com.carousell.entities.User;
import com.carousell.service.IRedditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a1dmiuxe(gopesh.tulsyan) on 16/07/17.
 */

@RestController
public class RedditController {

    @Autowired
    private IRedditService redditService;


    @RequestMapping("/health")
    String heath(){
        return "Hello World";
    }

    @RequestMapping(value = "/topic", method = RequestMethod.POST)
    boolean createTopic(@RequestBody TopicDTO topicDTO){
        boolean status = false;
        try{
            status = redditService.createTopic(topicDTO.getContent());
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }



    @RequestMapping(value = "/home")
    List<Topic> trendingTopics(){
        List<Topic> trendingTopics = new ArrayList<>();
        try {
            Iterator<Topic> it = redditService.trendingTopics();
            while (it.hasNext())
                trendingTopics.add(it.next());
        }catch (Exception e){
            e.printStackTrace();
        }
        return trendingTopics;
    }

    @RequestMapping(value = "/upVote", method = RequestMethod.POST)
    int upVote(@RequestBody VoteDTO voteDTO){

        int upVoteCount=0;
        try {
            upVoteCount = redditService.upVote(voteDTO.getTopicId(),voteDTO.getUid());
        }catch (Exception e){
            e.printStackTrace();
        }
        return upVoteCount;
    }

    @RequestMapping(value = "/downVote", method = RequestMethod.POST)
    int downVote(@RequestBody VoteDTO voteDTO) {
        int downVoteCount = 0;
        try {
            downVoteCount = redditService.downVote(voteDTO.getTopicId(), voteDTO.getUid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downVoteCount;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    User user(@RequestBody UserDTO userDTO){
        User user = null;
        try{
            user = redditService.createUser(userDTO.getUserName());
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @RequestMapping(value = "/topics/all", method = RequestMethod.GET)
    List<Topic> allTopics(){
        List<Topic> allTopics = new ArrayList<>();
        try{
            Iterator<Topic> it = redditService.allTopics();
            while (it.hasNext())
                allTopics.add(it.next());
        }catch (Exception e){
            e.printStackTrace();
        }
        return allTopics;
    }
}
