package com.zuzseb.learning.service;

import com.zuzseb.learning.model.File;
import com.zuzseb.learning.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Override
    public File save(File file) {
        return (File) fileUploadRepository.save(file);
    }
}
