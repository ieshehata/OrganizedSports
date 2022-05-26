package organizesports.callbacks;

public interface BooleanCallback {
    void onSuccess(boolean bool);

    void onFail(String error);
}
