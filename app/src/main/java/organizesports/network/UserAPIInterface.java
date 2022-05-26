package organizesports.network;

import com.app.organizesports.models.NotificationResponseModel;
import com.app.organizesports.models.SendNotificationModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPIInterface {

    @POST("send")
    Call<NotificationResponseModel> sendNotification(@Body SendNotificationModel notification);
}
