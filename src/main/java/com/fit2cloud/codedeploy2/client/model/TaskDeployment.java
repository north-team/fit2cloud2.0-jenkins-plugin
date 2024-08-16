package com.fit2cloud.codedeploy2.client.model;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TaskDeployment implements Serializable {

    private String runtimeEnvId;
    private String taskId;
    private List<ScriptVar> scriptVars = new ArrayList<>();

    public TaskDeployment(String runtimeEnvId, String taskId) {
        this.runtimeEnvId = runtimeEnvId;
        this.taskId = taskId;
    }

}