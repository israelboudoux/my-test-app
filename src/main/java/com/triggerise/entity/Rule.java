package com.triggerise.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Rule {
    private String code;
    private String name;
    private String description;
    private String parameters;
}
