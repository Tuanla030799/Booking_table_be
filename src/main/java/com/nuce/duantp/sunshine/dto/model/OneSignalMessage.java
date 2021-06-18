package com.nuce.duantp.sunshine.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class OneSignalMessage {

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("include_player_ids")
    private List<String> includePlayerIds;

    private Map<String, String> headings = new HashMap<>();

    private Map<String, String> contents = new HashMap<>();

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("template_id")
    private String templateId;

    private Object data;

    private String url;
}
