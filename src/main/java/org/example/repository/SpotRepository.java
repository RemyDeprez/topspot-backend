package org.example.repository;

import org.example.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findByCreatedBy(String username);
    List<Spot> findByNameContainingIgnoreCase(String name);
}

