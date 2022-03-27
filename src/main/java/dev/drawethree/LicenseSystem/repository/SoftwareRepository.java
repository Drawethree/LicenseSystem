package dev.drawethree.licensesystem.repository;


import dev.drawethree.licensesystem.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, Integer> {

}