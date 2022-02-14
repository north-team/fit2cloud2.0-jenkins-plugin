package com.fit2cloud.codedeploy2.client.model;

public class CheckVersionAndTagDTO {
    private String versionId;
    private boolean tag;

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }
}
