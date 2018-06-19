package com.zuzseb.learning.repository;

import com.zuzseb.learning.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<File, Long> {
}
