package com.practice.library.dto;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookDto {
    private long id;
    private String name;
    private BookAuthorDto author;
    private BookGenreDto genre;
    private List<CommentDto> comments = new ArrayList<>();
    private String imageName;
    private String imagePath;
    private String ipAddress = getLocalHost();

    private String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }
}
