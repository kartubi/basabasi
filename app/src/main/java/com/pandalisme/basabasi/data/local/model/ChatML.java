package com.pandalisme.basabasi.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by kartubi on 20/12/2017.
 */

@Entity
public class ChatML {

    @PrimaryKey
    @NonNull
    public long id;

    public String name;
    public String message;

}
