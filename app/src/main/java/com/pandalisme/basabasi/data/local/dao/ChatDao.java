package com.pandalisme.basabasi.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.pandalisme.basabasi.data.local.model.ChatML;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by kartubi on 20/12/2017.
 */

@Dao
public interface ChatDao {

    @Query("select * from ChatML ORDER BY id desc")
    List<ChatML> getChat();

    @Insert(onConflict = IGNORE)
    void insertChat(ChatML chatML);
}
