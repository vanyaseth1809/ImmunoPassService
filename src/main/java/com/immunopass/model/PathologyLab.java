package com.immunopass.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PathologyLab {
    private final Long id;
    private final String name;
    private final String status; // TODO: update from String to enum
}
