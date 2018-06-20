package com.zuzseb.learning.service;

import com.zuzseb.learning.model.File;
import com.zuzseb.learning.model.Post;

import java.util.List;

public interface FileUploadService {

    File save(File file);
    List<File> findByPost(Post post);
    void deleteFiles(List<File> files);
 }
