package com.carousell.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1dmiuxe(gopesh.tulsyan) on 15/07/17.
 */

public class Topic implements Comparable<Topic>{

    private String uid;
    private String content;
    private List<String> upvoters = new ArrayList<>();
    private List<String> downvoters = new ArrayList<>();
    private Long timestamp;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String id) {
        this.uid = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getUpvoters() {
        return upvoters;
    }

    public void setUpvoters(List<String> upvoters) {
        this.upvoters = upvoters;
    }

    public List<String> getDownvoters() {
        return downvoters;
    }

    public void setDownvoters(List<String> downvoters) {
        this.downvoters = downvoters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic topic = (Topic) o;

        if (!uid.equals(topic.uid)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + upvoters.hashCode();
        result = 31 * result + downvoters.hashCode();
        result = 31 * result + timestamp.hashCode();
        return result;
    }


    @Override
    public int compareTo(Topic o) {
        if (upvoters.size()>o.getUpvoters().size())
            return -1;
        else if (upvoters.size() == o.getUpvoters().size())
            if (timestamp > o.getTimestamp())
                return -1;
            else if (timestamp.equals(o.getTimestamp()))
                return 0;
            else
                return 1;
        else
            return 1;
    }
}
