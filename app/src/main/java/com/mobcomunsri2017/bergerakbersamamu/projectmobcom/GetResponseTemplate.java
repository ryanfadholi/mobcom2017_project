package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by rynfd on 12/2/2017.
 */

public abstract class GetResponseTemplate {
    protected boolean error;
    protected String errorMessage;
    protected JsonNode data;

    public boolean isError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
