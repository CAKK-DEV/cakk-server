package com.cakk.domain.mysql.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeLike;
import com.cakk.domain.mysql.entity.user.User;

public interface CakeLikeJpaRepository extends JpaRepository<CakeLike, Long> {

	List<CakeLike> findAllByUser(User user);

	Optional<CakeLike> findByUserAndCake(User user, Cake cake);
}
