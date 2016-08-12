package OnPostExecuteHandlers;

/**
 * Created by Sebastian on 04/08/2016.
 */
public interface OnPostExecute {
    void onSucced(String json);
    void onSucced();
    void onFail();
}
