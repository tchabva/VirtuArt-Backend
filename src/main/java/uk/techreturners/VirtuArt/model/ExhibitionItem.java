package uk.techreturners.VirtuArt.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "exhibition_items")
public class ExhibitionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "api_id")
    private String apiId;

    private String title;

    @Column(name = "artist_title")
    private String artistTitle;

    private String date;

    @Column(name = "image_url")
    private String imageUrl;

    private String source;

    @ManyToMany(mappedBy = "exhibitionItems", fetch = FetchType.LAZY)
    private List<Exhibition> exhibitions = new ArrayList<>();
}