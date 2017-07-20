package com.carousell.controller;

import com.carousell.beans.BaseResponse;
import com.carousell.beans.TopicDTO;
import com.carousell.beans.UserDTO;
import com.carousell.beans.VoteDTO;
import com.carousell.entities.Topic;
import com.carousell.entities.User;
import com.carousell.service.IRedditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a1dmiuxe(gopesh.tulsyan) on 16/07/17.
 */

@Controller
public class RedditController {

    @Autowired
    private IRedditService redditService;


    @RequestMapping("/health")
    String heath(){
        return "Hello World";
    }

    @RequestMapping("/")
    public String welcome(Model model) {
        Object[] allTopics = new Object[0];
        try{
            allTopics = redditService.trendingTopics();
        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("topics", allTopics);
        return "index";
    }

    @RequestMapping(value = "/topic", method = RequestMethod.POST,consumes = "application/json", produces = "application/json")
    @ResponseBody
    BaseResponse createTopic(@RequestBody TopicDTO topicDTO){
        BaseResponse baseResponse = new BaseResponse();
        try{
            boolean status = redditService.createTopic(topicDTO.getContent(), topicDTO.getUser());
            baseResponse.setResponse(status);
            baseResponse.setMessage("Topic created successfully");
        }catch (Exception e){
            e.printStackTrace();
            baseResponse.setMessage(e.getMessage());
        }
        return baseResponse;
    }



    @RequestMapping(value = "/home")
    @ResponseBody Object[] trendingTopics(){
        Object[] allTopics = new Object[0];
        try{
            allTopics = redditService.trendingTopics();
        }catch (Exception e){
            e.printStackTrace();
        }
        return allTopics;
    }

    @RequestMapping(value = "/upVote", method = RequestMethod.POST)
    @ResponseBody int upVote(@RequestBody VoteDTO voteDTO){

        int upVoteCount=0;
        try {
            upVoteCount = redditService.upVote(voteDTO.getTopicId(),voteDTO.getUid());
        }catch (Exception e){
            e.printStackTrace();
        }
        return upVoteCount;
    }

    @RequestMapping(value = "/downVote", method = RequestMethod.POST)
    @ResponseBody int downVote(@RequestBody VoteDTO voteDTO) {
        int downVoteCount = 0;
        try {
            downVoteCount = redditService.downVote(voteDTO.getTopicId(), voteDTO.getUid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downVoteCount;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody User user(@RequestBody UserDTO userDTO){
        User user = null;
        try{
            user = redditService.createUser(userDTO.getUserName());
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @RequestMapping(value = "/topics/all", method = RequestMethod.GET)
    @ResponseBody List<Topic> allTopics(){
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
