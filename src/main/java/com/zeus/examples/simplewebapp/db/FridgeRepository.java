package com.zeus.examples.simplewebapp.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, UUID> {
}