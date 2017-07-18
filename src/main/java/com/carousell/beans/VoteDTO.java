package com.carousell.beans;

/**
 * Created by a1dmiuxe(gopesh.tulsyan) on 16/07/17.
 */

public class VoteDTO {
    private String topicId;
    private String uid;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VoteDTO voteDTO = (VoteDTO) o;

        if (!topicId.equals(voteDTO.topicId)) return false;
        return uid.equals(voteDTO.uid);
    }

    @Override
    public int hashCode() {
        int result = topicId.hashCode();
        result = 31 * result + uid.hashCode();
        return result;
    }
}
