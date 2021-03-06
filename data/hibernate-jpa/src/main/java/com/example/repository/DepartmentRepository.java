package com.example.repository;

import com.example.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 李磊
 * @datetime 2020/1/20 12:39
 * @description
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}