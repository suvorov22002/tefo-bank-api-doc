package com.tefo.api.documentation.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApiUtils {

    public static final String COMPONENTS = "components";
    public static final String SCHEMAS = "schemas";
    public static final String PATHS = "paths";
    public static final String SERVERS = "servers";
    public static final String URL = "url";
    private final ApiProperties swaggerProperties;

    @Value("${gateway.url}")
    private String gatewayDomain;

    @Value("${ingress.url}")
    private String ingressUrl;

    public ApiUtils(ApiProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    public String getAPI(String service, String env) {
        String result = getAPIJson(service);
        return replaceValueInJson(result, env);
    }

    public String getCombinedAPI(String env) {
        JSONObject api = null;
        for (String name : getNames()) {
            try {
                api = Objects.isNull(api) ? new JSONObject(getAPIJson(name)) : extendWith(api, new JSONObject(getAPIJson(name)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (api != null) {
            return replaceValueInJson(api.toString(), env);
        } else {
            return "{}";
        }
    }

    private String getAPIJson(String service) {
        String url = gatewayDomain + service + "/v3/api-docs";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    private String replaceValueInJson(String json, String env) {
        JSONObject jObject = new JSONObject(json);
        jObject.remove(SERVERS);
        JSONObject jsonUrlObject = new JSONObject();
        jsonUrlObject.put(URL, String.format(ingressUrl, env));
        jObject.put(SERVERS, List.of(jsonUrlObject));
        return jObject.toString();
    }

    public Set<String> getNames() {
        return swaggerProperties.getUrls()
                .stream()
                .map(ApiUrl::getServiceName).collect(Collectors.toSet());
    }

    private JSONObject extendWith(JSONObject api, JSONObject newData) {
        if (api == null) {
            return newData;
        }

        JSONObject components = api.getJSONObject(COMPONENTS);
        JSONObject schemas = components.getJSONObject(SCHEMAS);
        JSONObject paths = api.getJSONObject(PATHS);

        JSONObject newComponents = newData.getJSONObject(COMPONENTS);
        JSONObject newSchemas = newComponents.getJSONObject(SCHEMAS);
        JSONObject newPaths = newData.getJSONObject(PATHS);

        newPaths.keySet().forEach(key -> paths.put(key, newPaths.get(key)));
        newSchemas.keySet().forEach(key -> schemas.put(key, newSchemas.get(key)));
        components.put(SCHEMAS, schemas);
        api.put(COMPONENTS, components);
        api.put(PATHS, paths);

        return api;
    }
}
