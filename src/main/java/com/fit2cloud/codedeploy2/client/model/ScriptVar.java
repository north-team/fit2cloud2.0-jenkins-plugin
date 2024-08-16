package com.fit2cloud.codedeploy2.client.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author makai
 * @desc:
 * @date 2024/8/15 下午5:49
 */
@Data
public class ScriptVar implements Serializable {
    private String name;
    private String key;
    private boolean refEnv = false;
    private String defaultValue;
    private boolean isRequired;
}
