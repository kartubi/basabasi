package com.pandalisme.basabasi.ui.chat;

import android.content.Context;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.pandalisme.basabasi.R;
import com.pandalisme.basabasi.data.remote.ChatMR;

/**
 * Created by kartubi on 21/12/2017.
 */

@Layout(R.layout.list_chat)
public class ChatAdapter {
    @View(R.id.tvName)
    private TextView tvName;
    @View(R.id.tvMessage)
    private TextView tvMessage;

    Context mContext;
    ChatMR mChatMR;

    public ChatAdapter(ChatMR chatMR, Context context){
        mChatMR = chatMR;
        mContext = context;
    }

    @Resolve
    private void onResolved(){
        tvMessage.setText(mChatMR.getMessage());
        tvName.setText(mChatMR.getName());
    }
}
