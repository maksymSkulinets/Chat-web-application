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

        if (!uri.equals(that.uri)) return false;
        return method.equals(that.method);

    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }
}