package com.pandalisme.basabasi.ui.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.mindorks.placeholderview.InfinitePlaceHolderView;
import com.onesignal.OSNotification;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.pandalisme.basabasi.R;
import com.pandalisme.basabasi.data.local.model.ChatML;
import com.pandalisme.basabasi.data.local.utils.AppDataBase;
import com.pandalisme.basabasi.data.remote.ChatMR;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.alexbykov.nopaginate.paginate.Paginate;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    InfinitePlaceHolderView recyclerView;
    @BindView(R.id.ibSend)
    ImageButton ibSend;
    @BindView(R.id.etMessage)
    EditText etMessage;

    private AppDataBase mDb;
    Paginate paginate;
    int i;
    String message, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("GLOBAL ROOM");

        SharedPreferences sP = getSharedPreferences("profile", Context.MODE_PRIVATE);
        i = sP.getInt("lastid", 10);

        OneSignal.startInit(this)
                .autoPromptLocation(false)
                .setNotificationReceivedHandler(new CustomOnReceivedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        mDb = AppDataBase.getDataDatabase(getApplicationContext());
        ibSend.setOnClickListener(view -> validation());

        fetchData();
    }

    private void validation(){
        message = etMessage.getText().toString();
        if (!message.isEmpty()){
            etMessage.setText("");
           sendMessage();
        }else {
            Toast.makeText(getApplicationContext(), "GABOLEH KOSONG COK!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void sendMessage(){
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(0, "All");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sP = getSharedPreferences("profile", Context.MODE_PRIVATE);
        name = sP.getString("name", "DEFAULT");

        try {
            jsonObject2.put("name", name);
            jsonObject2.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObject1.put("en", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            jsonObject.put("app_id", "APP_IDNYA");
            jsonObject.put("included_segments", jsonArray);
            jsonObject.put("data",jsonObject2);
            jsonObject.put("contents", jsonObject1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("JSON JADI", String.valueOf(jsonObject));

        Rx2AndroidNetworking.post("https://onesignal.com/api/v1/notifications")
                .addHeaders("Authorization", "Basic APIKEYNYA")
                .addJSONObjectBody(jsonObject)
                .build()
                .getJSONObjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        Log.d("TAG", String.valueOf(jsonObject));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ANError){
                            ANError anError = (ANError) e;
                            Log.d("TAG", anError.getErrorBody());
                            Log.d("TAG", anError.getErrorDetail());
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public class CustomOnReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String customKey;

            if (data != null) {
                try {
                    int id = i++;
                    addChat(id, data.getString("name"), data.getString("message"));
                    SharedPreferences sP = getSharedPreferences("profile", Context.MODE_PRIVATE);
                    sP.edit().putInt("lastid", id).apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addChat(long id,String name, String message){
        Log.d("TAG", message);
        ChatML chatML = new ChatML();
        chatML.id = id;
        chatML.message = message;
        chatML.name = name;
        mDb.chatDao().insertChat(chatML);
        fetchData();
    }

    public void fetchData(){
        recyclerView.removeAllViews();
        List<ChatML> chatMLS = mDb.chatDao().getChat();
        for (ChatML chatML : chatMLS){
            ChatMR chatMR = new ChatMR();
            chatMR.setId(chatML.id);
            chatMR.setName(chatML.name);
            chatMR.setMessage(chatML.message);
            recyclerView.addView(new ChatAdapter(chatMR, getApplicationContext()));
        }
    }

}
