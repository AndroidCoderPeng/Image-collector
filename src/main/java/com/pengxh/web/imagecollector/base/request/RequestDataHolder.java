package com.pengxh.web.imagecollector.base.request;

/**
 * @author Administrator
 */
public class RequestDataHolder {
    private static ThreadLocal<RequestData> holder = new ThreadLocal<>();

    public static void put(RequestData s) {
        if (holder.get() == null) {
            holder.set(s);
        }
    }

    public static RequestData get() {
        return holder.get();
    }

    public static void remove() {
        holder.remove();
    }
}
