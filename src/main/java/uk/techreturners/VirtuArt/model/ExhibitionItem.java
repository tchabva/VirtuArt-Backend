package uk.techreturners.VirtuArt.model;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    // TODO Consider making this a ManyToMany Relationship
}
