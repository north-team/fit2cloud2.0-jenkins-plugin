package com.fit2cloud.codedeploy.client.model;

import java.io.Serializable;

public class ApplicationRepository implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.id
     *
     *
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.name
     *
     *
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.type
     *
     *
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.created_time
     *
     *
     */
    private Long createdTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.repository
     *
     *
     */
    private String repository;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.access_id
     *
     *
     */
    private String accessId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.access_password
     *
     *
     */
    private String accessPassword;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.status
     *
     *
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.scope
     *
     *
     */
    private String scope;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devops_application_repository.organization_id
     *
     *
     */
    private String organizationId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table devops_application_repository
     *
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.id
     *
     * @return the value of devops_application_repository.id
     *
     *
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.id
     *
     * @param id the value for devops_application_repository.id
     *
     *
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.name
     *
     * @return the value of devops_application_repository.name
     *
     *
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.name
     *
     * @param name the value for devops_application_repository.name
     *
     *
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.type
     *
     * @return the value of devops_application_repository.type
     *
     *
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.type
     *
     * @param type the value for devops_application_repository.type
     *
     *
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.created_time
     *
     * @return the value of devops_application_repository.created_time
     *
     *
     */
    public Long getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.created_time
     *
     * @param createdTime the value for devops_application_repository.created_time
     *
     *
     */
    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.repository
     *
     * @return the value of devops_application_repository.repository
     *
     *
     */
    public String getRepository() {
        return repository;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.repository
     *
     * @param repository the value for devops_application_repository.repository
     *
     *
     */
    public void setRepository(String repository) {
        this.repository = repository == null ? null : repository.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.access_id
     *
     * @return the value of devops_application_repository.access_id
     *
     *
     */
    public String getAccessId() {
        return accessId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.access_id
     *
     * @param accessId the value for devops_application_repository.access_id
     *
     *
     */
    public void setAccessId(String accessId) {
        this.accessId = accessId == null ? null : accessId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.access_password
     *
     * @return the value of devops_application_repository.access_password
     *
     *
     */
    public String getAccessPassword() {
        return accessPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.access_password
     *
     * @param accessPassword the value for devops_application_repository.access_password
     *
     *
     */
    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword == null ? null : accessPassword.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.status
     *
     * @return the value of devops_application_repository.status
     *
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.status
     *
     * @param status the value for devops_application_repository.status
     *
     *
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.scope
     *
     * @return the value of devops_application_repository.scope
     *
     *
     */
    public String getScope() {
        return scope;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.scope
     *
     * @param scope the value for devops_application_repository.scope
     *
     *
     */
    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devops_application_repository.organization_id
     *
     * @return the value of devops_application_repository.organization_id
     *
     *
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devops_application_repository.organization_id
     *
     * @param organizationId the value for devops_application_repository.organization_id
     *
     *
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId == null ? null : organizationId.trim();
    }
}