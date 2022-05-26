package organizesports.controllers;


import com.app.organizesports.models.NotificationResponseModel;
import com.app.organizesports.models.SendNotificationModel;
import com.app.organizesports.network.APIClient;
import com.app.organizesports.network.UserAPIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIController {

    private UserAPIInterface userAPIInterface;
    public void sendNotification(SendNotificationModel data){

        userAPIInterface = APIClient.getClient().create(UserAPIInterface.class);

        Call<NotificationResponseModel> userCall = userAPIInterface.sendNotification(data);
        userCall.enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                if(response.isSuccessful()){
                    NotificationResponseModel user1 = response.body();

                }else{
                }

            }

            @Override
            public void onFailure(Call<NotificationResponseModel> call, Throwable t) {
            }
        });

    }

}
