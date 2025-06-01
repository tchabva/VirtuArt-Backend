package uk.techreturners.VirtuArt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.techreturners.VirtuArt.model.ExhibitionItem;

import java.util.Optional;

@Repository
public interface ExhibitionItemRepository extends JpaRepository<ExhibitionItem, String> {
    Optional<ExhibitionItem> findByApiIdAndSource(String apiId, String source);
}