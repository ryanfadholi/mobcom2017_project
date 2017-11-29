package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Azhary Arliansyah on 29/11/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertVoteResponse {
    private boolean error;
    private String error_message;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
