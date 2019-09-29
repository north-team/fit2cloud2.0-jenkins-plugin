package com.fit2cloud.codedeploy2.client.model;

import java.io.Serializable;

public class Application implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application.id
     *
     *
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application.name
     *
     *
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application.created_time
     *
     *
     */
    private Long createdTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application.description
     *
     *
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application.organization_id
     *
     *
     */
    private String organizationId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application.scope
     *
     *
     */
    private String scope;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application.workspace_id
     *
     *
     */
    private String workspaceId;
    private String ApplicationRepositoryId;

    public String getApplicationRepositoryId() {
        return ApplicationRepositoryId;
    }

    public void setApplicationRepositoryId(String applicationRepositoryId) {
        ApplicationRepositoryId = applicationRepositoryId;
    }

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table devops_application
     *
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application.id
     *
     * @return the value of devops_application.id
     *
     *
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application.id
     *
     * @param id the value for devops_application.id
     *
     *
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application.name
     *
     * @return the value of devops_application.name
     *
     *
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application.name
     *
     * @param name the value for devops_application.name
     *
     *
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application.created_time
     *
     * @return the value of devops_application.created_time
     *
     *
     */
    public Long getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application.created_time
     *
     * @param createdTime the value for devops_application.created_time
     *
     *
     */
    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application.description
     *
     * @return the value of devops_application.description
     *
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application.description
     *
     * @param description the value for devops_application.description
     *
     *
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application.organization_id
     *
     * @return the value of devops_application.organization_id
     *
     *
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application.organization_id
     *
     * @param organizationId the value for devops_application.organization_id
     *
     *
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId == null ? null : organizationId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application.scope
     *
     * @return the value of devops_application.scope
     *
     *
     */
    public String getScope() {
        return scope;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application.scope
     *
     * @param scope the value for devops_application.scope
     *
     *
     */
    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application.workspace_id
     *
     * @return the value of devops_application.workspace_id
     *
     *
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application.workspace_id
     *
     * @param workspaceId the value for devops_application.workspace_id
     *
     *
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId == null ? null : workspaceId.trim();
    }
}
