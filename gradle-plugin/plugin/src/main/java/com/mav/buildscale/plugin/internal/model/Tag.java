package com.mav.buildscale.plugin.internal.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tag {

    private String key;
    private String value;

    public static Tag of(final String key, final String value) {
        return new Tag(key, value);
    }
}
