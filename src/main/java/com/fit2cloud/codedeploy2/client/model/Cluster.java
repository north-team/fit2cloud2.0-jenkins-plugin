package com.fit2cloud.codedeploy2.client.model;

import java.io.Serializable;

public class Cluster implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_cluster.id
     *
     *
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_cluster.name
     *
     *
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_cluster.workspace_id
     *
     *
     */
    private String workspaceId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_cluster.created_time
     *
     *
     */
    private Long createdTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_cluster.description
     *
     *
     */
    private String description;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table devops_cluster
     *
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_cluster.id
     *
     * @return the value of devops_cluster.id
     *
     *
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_cluster.id
     *
     * @param id the value for devops_cluster.id
     *
     *
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_cluster.name
     *
     * @return the value of devops_cluster.name
     *
     *
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_cluster.name
     *
     * @param name the value for devops_cluster.name
     *
     *
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_cluster.workspace_id
     *
     * @return the value of devops_cluster.workspace_id
     *
     *
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_cluster.workspace_id
     *
     * @param workspaceId the value for devops_cluster.workspace_id
     *
     *
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId == null ? null : workspaceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_cluster.created_time
     *
     * @return the value of devops_cluster.created_time
     *
     *
     */
    public Long getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_cluster.created_time
     *
     * @param createdTime the value for devops_cluster.created_time
     *
     *
     */
    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_cluster.description
     *
     * @return the value of devops_cluster.description
     *
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_cluster.description
     *
     * @param description the value for devops_cluster.description
     *
     *
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}
