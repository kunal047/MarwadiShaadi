package com.sid.marwadishaadi.Chat;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by hp pc on 05-06-2017.
 */

public class Author implements IUser {
    String id, name, avatar;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}

