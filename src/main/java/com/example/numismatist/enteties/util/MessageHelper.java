package com.example.numismatist.enteties.util;

import com.example.numismatist.enteties.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }
}
