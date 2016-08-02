package com.teamdev.javaclasses;

/**
 * Holder of request data
 */
class RequestContext {

    private final String uri;
    private final String method;

    RequestContext(String uri, String method) {
        this.uri = uri;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestContext that = (RequestContext) o;

        if (uri != null ? !uri.equals(that.uri) : that.uri != null) return false;
        return method != null ? method.equals(that.method) : that.method == null;

    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }
}