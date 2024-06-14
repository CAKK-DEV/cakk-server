package com.cakk.domain.mysql.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeHeart;
import com.cakk.domain.mysql.entity.user.User;

public interface CakeHeartJpaRepository extends JpaRepository<CakeHeart, Long> {

	List<CakeHeart> findAllByUser(User user);

	Optional<CakeHeart> findByUserAndCake(User user, Cake cake);
}
