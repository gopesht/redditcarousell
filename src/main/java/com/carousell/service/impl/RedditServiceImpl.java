package com.carousell.service.impl;

import com.carousell.entities.Topic;
import com.carousell.entities.User;
import com.carousell.service.IRedditService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by a1dmiuxe(gopesh.tulsyan) on 18/07/17.
 */

@Service("redditService")
public class RedditServiceImpl implements IRedditService {

    private static Map<String, User> users = new ConcurrentHashMap<>();
    private static Map<String, Topic> topics = new ConcurrentHashMap<>();

    private static final int TRENDING_TOPICS = 20;

    private static AtomicInteger userCounter = new AtomicInteger(0);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static NavigableSet<Topic> trendingTopics = Collections.synchronizedNavigableSet(new TreeSet<>());


    @Override
    public int upVote(String topicId, String uid) throws IllegalArgumentException, CloneNotSupportedException {
        User user = users.get(uid);
        Topic topic = topics.get(topicId);
        validate(user, topic);

        Topic oldTopic = (Topic) topic.clone();
        topic.getUpvoters().add(user.getUid());

        UpdateTrendingTopic updateTrendingTopic = new UpdateTrendingTopic(topic, oldTopic);
        executorService.submit(updateTrendingTopic);

        user.getUpvotes().add(topicId);

        return topic.getUpvoters().size();
    }


    private void validate(User user, Topic topic){
        if (null == user)
            throw new IllegalArgumentException("User doesn't exists");
        if (null == topic)
            throw new IllegalArgumentException("Topic doesn't exists");
    }


    @Override
    public int downVote(String topicId, String uid) throws IllegalArgumentException, CloneNotSupportedException {
        User user = users.get(uid);
        Topic topic = topics.get(topicId);
        validate(user, topic);

        Topic oldTopic = (Topic) topic.clone();
        topic.getUpvoters().add(user.getUid());

        topic.getDownvoters().add(user.getUid());

        user.getDownvotes().add(topicId);

        UpdateTrendingTopic updateTrendingTopic = new UpdateTrendingTopic(topic, oldTopic);
        executorService.submit(updateTrendingTopic);

        return topic.getDownvoters().size();
    }


    @Override
    public Object[] trendingTopics() {
        return trendingTopics.toArray();
    }


    @Override
    public boolean createTopic(String content, String user) throws IllegalArgumentException{
        if(StringUtils.isEmpty(content) || StringUtils.isEmpty(user))
            throw new IllegalArgumentException("Content cannot be blank");
        Topic topic = new Topic();
        topic.setContent(content);
        topic.setTimestamp(System.currentTimeMillis());
        topic.setUid(UUID.randomUUID().toString());
        topic.setPostedBy(user);
        topics.put(topic.getUid(), topic);
        UpdateTrendingTopic updateTrendingTopic = new UpdateTrendingTopic(topic, topic);
        executorService.submit(updateTrendingTopic);
        return true;
    }

    @Override
    public User createUser(String userName) {
        if (StringUtils.isEmpty(userName))
            throw new IllegalArgumentException("Username cannot be blank");

        User user = users.get(userName.toLowerCase().trim());
        if (null == user) {
            user = new User();
            user.setUserName(userName.concat(Integer.toString(userCounter.incrementAndGet())));
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
        private Topic oldTopic;

        public UpdateTrendingTopic(Topic topic, Topic oldTopic) {
            this.topic = topic;
            this.oldTopic = oldTopic;
        }
        @Override
        public void run() {
            //removing old object
            if (trendingTopics.contains(oldTopic))
                trendingTopics.remove(oldTopic);

            if (trendingTopics.size() >= TRENDING_TOPICS) {
                Topic last = trendingTopics.last();
                if (last.getUpvoters().size() <= topic.getUpvoters().size())
                    trendingTopics.pollLast();
            }
            Topic newTopic;
            try {
                newTopic = (Topic) topic.clone();
                trendingTopics.add(newTopic);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }

    }
}
