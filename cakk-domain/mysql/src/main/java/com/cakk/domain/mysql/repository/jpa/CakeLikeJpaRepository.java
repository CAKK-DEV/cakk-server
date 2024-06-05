package com.cakk.domain.mysql.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.cake.CakeLike;
import com.cakk.domain.mysql.entity.user.User;

public interface CakeLikeJpaRepository extends JpaRepository<CakeLike, Long> {

	List<CakeLike> findAllByUser(User user);
}