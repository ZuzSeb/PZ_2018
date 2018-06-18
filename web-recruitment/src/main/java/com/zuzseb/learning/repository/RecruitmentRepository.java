package com.zuzseb.learning.repository;

import com.zuzseb.learning.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Post, String> {
}
