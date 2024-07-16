package com.jongas124.javaapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jongas124.javaapi.models.Profile;
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {}
