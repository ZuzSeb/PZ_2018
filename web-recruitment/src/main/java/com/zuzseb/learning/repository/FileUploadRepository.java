package com.zuzseb.learning.repository;

import com.zuzseb.learning.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadRepository extends JpaRepository<File, String> {
}
