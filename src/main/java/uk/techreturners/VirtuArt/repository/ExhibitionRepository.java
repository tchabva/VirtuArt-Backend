package uk.techreturners.VirtuArt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.techreturners.VirtuArt.model.Exhibition;
import uk.techreturners.VirtuArt.model.User;

import java.util.List;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, String> {
    List<Exhibition> findByUser(User user);
}
