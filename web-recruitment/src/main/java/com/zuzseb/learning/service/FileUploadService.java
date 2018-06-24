package com.zuzseb.learning.service;

import com.zuzseb.learning.model.File;
import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.model.User;

import java.util.List;

public interface FileUploadService {

    void save(File file);
    List<File> findByPost(Post post);
    List<File> findByUser(User user);
    void deleteFiles(List<File> files);
    void deleteFileByUserAndPost(User user, Post post);
    boolean applyForPodst(User user, Post post);
}
