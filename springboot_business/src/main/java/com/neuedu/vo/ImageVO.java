package com.neuedu.vo;

/**
 * @author jyw
 * @date 2019/10/26-10:24
 */

public class ImageVO {
    private String uri;
    private String url;

    public ImageVO() {
    }

    public ImageVO(String uri, String url) {
        this.uri = uri;
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
