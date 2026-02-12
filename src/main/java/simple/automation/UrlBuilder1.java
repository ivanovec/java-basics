package simple.automation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class UrlBuilder1 {
    private String baseUrl;
    private Map<String, String> queryParams;
    private String fragment;

    public UrlBuilder1(String baseUrl) {
        this.baseUrl = baseUrl;
        this.queryParams = new LinkedHashMap<>();
    }

    public UrlBuilder1 addQueryParam(String key, String value) {
        queryParams.put(key, value);
        return this;
    }

    public UrlBuilder1 addQueryParam(String key, int value) {
        return addQueryParam(key, String.valueOf(value));
    }

    public UrlBuilder1 addQueryParam(String key, boolean value) {
        return addQueryParam(key, String.valueOf(value));
    }

    public UrlBuilder1 removeQueryParam(String key) {
        queryParams.remove(key);
        return this;
    }

    public UrlBuilder1 setFragment(String fragment) {
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

    public static UrlBuilder1 from(String baseUrl) {
        return new UrlBuilder1(baseUrl);
    }
}
