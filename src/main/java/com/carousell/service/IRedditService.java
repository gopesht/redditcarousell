package com.carousell.service;

import com.carousell.entities.Topic;
import com.carousell.entities.User;

import java.util.Iterator;

/**
 * Created by a1dmiuxe(gopesh.tulsyan) on 18/07/17.
 */
public interface IRedditService {
    int upVote(String topicId, String uid) throws IllegalArgumentException, CloneNotSupportedException;

    int downVote(String topicId, String uid) throws IllegalArgumentException, CloneNotSupportedException;

    Object[] trendingTopics();


    boolean createTopic(String content, String user);

    User createUser(String userName);

    Iterator<Topic> allTopics();
}
