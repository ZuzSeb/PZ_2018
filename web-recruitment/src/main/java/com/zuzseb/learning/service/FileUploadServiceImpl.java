package com.zuzseb.learning.service;

import com.zuzseb.learning.model.File;
import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.model.User;
import com.zuzseb.learning.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class FileUploadServiceImpl implements FileUploadService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Override
    public File save(File file) {
        return (File) fileUploadRepository.save(file);
    }

    @Override
    public List<File> findByPost(Post post) {
        return em.createNamedQuery("File.getByPost", File.class)
                .setParameter("post", post)
                .getResultList();
    }

    @Override
    public List<File> findByUser(User user) {
        return em.createNamedQuery("File.getByUser", File.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public void deleteFiles(List<File> files) {
        files.forEach(f -> fileUploadRepository.delete(f));
    }

    @Override
    public void deleteFileByUserAndPost(User user, Post post) {
        em.createNamedQuery("File.deleteFileByUserAndPost")
                .setParameter("user", user)
                .setParameter("post", post);
    }
}
