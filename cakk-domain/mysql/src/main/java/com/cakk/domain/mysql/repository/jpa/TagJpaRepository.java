package com.cakk.domain.mysql.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cakk.domain.mysql.entity.cake.Tag;

@Repository
public interface TagJpaRepository extends JpaRepository<Tag, Long> {

	Optional<Tag> findTagByTagName(String tagName);
}
