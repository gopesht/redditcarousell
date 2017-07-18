package com.carousell.service.impl;

import com.carousell.entities.Topic;
import com.carousell.entities.User;
import com.carousell.service.IRedditService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by a1dmiuxe(gopesh.tulsyan) on 18/07/17.
 */

@Service("redditService")
public class RedditServiceImpl implements IRedditService {

    private static Map<String, User> users = new ConcurrentHashMap<>();
    private static Map<String, Topic> topics = new ConcurrentHashMap<>();

    private static final int TRENDING_TOPICS = 3;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static NavigableSet<Topic> trendingTopics = Collections.synchronizedNavigableSet(new TreeSet<>());


    @Override
    public int upVote(String topicId, String uid) throws IllegalArgumentException{
        User user = users.get(uid);
        Topic topic = topics.get(topicId);
        if (null == user)
            throw new IllegalArgumentException("User doesn't exists");
        if (null == topic)
            throw new IllegalArgumentException("Topic doesn't exists");

        topic.getUpvoters().add(user.getUid());
        UpdateTrendingTopic updateTrendingTopic = new UpdateTrendingTopic(topic);
        executorService.submit(updateTrendingTopic);

        user.getUpvotes().add(topicId);

        return topic.getUpvoters().size();
    }



    @Override
    public int downVote(String topicId, String uid) throws IllegalArgumentException{
        User user = users.get(uid);
        Topic topic = topics.get(topicId);
        if (null == user)
            throw new IllegalArgumentException("User doesn't exists");
        if (null == topic)
            throw new IllegalArgumentException("Topic doesn't exists");

        topic.getDownvoters().add(user.getUid());

        user.getDownvotes().add(topicId);

        return topic.getDownvoters().size();
    }


    @Override
    public Iterator<Topic> trendingTopics() {
        return trendingTopics.iterator();
    }


    @Override
    public boolean createTopic(String content) {
        Topic topic = new Topic();
        topic.setContent(content);
        topic.setTimestamp(System.currentTimeMillis());
        topic.setUid(UUID.randomUUID().toString());
        topics.put(topic.getUid(), topic);
        UpdateTrendingTopic updateTrendingTopic = new UpdateTrendingTopic(topic);
        executorService.submit(updateTrendingTopic);
        return true;
    }

    @Override
    public User createUser(String userName) {
        User user = users.get(userName.toLowerCase().trim());
        if (null == user) {
            user = new User();
            user.setUserName(userName);
            user.setUid(UUID.randomUUID().toString());
            users.put(user.getUserName(),user);
        }
        return user;
    }

    @Override
    public Iterator<Topic> allTopics() {
        return topics.values().iterator();
    }

    private class UpdateTrendingTopic implements Runnable{
        private Topic topic;

        public UpdateTrendingTopic(Topic topic) {
            this.topic = topic;
        }
        @Override
        public void run() {
            if (trendingTopics.contains(topic))
                trendingTopics.remove(topic);
            if (trendingTopics.size() < TRENDING_TOPICS) {
                trendingTopics.add(topic);
            } else {
                Topic last = trendingTopics.last();
                if (last.getUpvoters().size() <= topic.getUpvoters().size()) {
                    trendingTopics.pollLast();
                    trendingTopics.add(topic);
                }
            }

        }
    }
}
