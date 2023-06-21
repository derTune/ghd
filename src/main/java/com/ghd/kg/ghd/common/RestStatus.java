package com.ghd.kg.ghd.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PUBLIC)
public class RestStatus {
    final static int SUCCESS = 0;
    final static int FAILED_REQUEST = 11;
}
