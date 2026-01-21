package simple.automation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class UrlBuilder {
    private String baseUrl;
    private Map<String, String> queryParams;
    private String fragment;

    public UrlBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        this.queryParams = new LinkedHashMap<>();
    }

    public UrlBuilder addQueryParam(String key, String value) {
        queryParams.put(key, value);
        return this;
    }

    public UrlBuilder addQueryParam(String key, int value) {
        return addQueryParam(key, String.valueOf(value));
    }

    public UrlBuilder addQueryParam(String key, boolean value) {
        return addQueryParam(key, String.valueOf(value));
    }

    public UrlBuilder removeQueryParam(String key) {
        queryParams.remove(key);
        return this;
    }

    public UrlBuilder setFragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    public String build() {
        StringBuilder url = new StringBuilder(baseUrl);

        if (!queryParams.isEmpty()) {
            url.append(baseUrl.contains("?") ? "&" : "?");
            boolean first = true;

            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (!first) {
                    url.append("&");
                }
                url.append(encodeParam(entry.getKey()));
                url.append("=");
                url.append(encodeParam(entry.getValue()));
                first = false;
            }
        }

        if (fragment != null && !fragment.isEmpty()) {
            url.append("#").append(fragment);
        }

        return url.toString();
    }

    private String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return param;
        }
    }

    public static UrlBuilder from(String baseUrl) {
        return new UrlBuilder(baseUrl);
    }
}
