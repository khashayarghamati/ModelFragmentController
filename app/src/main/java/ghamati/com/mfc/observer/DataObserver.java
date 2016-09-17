package ghamati.com.mfc.observer;

/**
 * Created by khashayar on 9/12/16.
 */
public interface DataObserver<T> {
    void response(T data);
    void error(String error);
}
