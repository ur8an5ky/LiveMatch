package pl.ur8an5ky.livematch.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents the football team playing in the matches.
 */
@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(nullable = false, name = "short_name", length = 10)
    private String shortName;

    @Column(nullable = false, name = "country", length = 50)
    private String country;
}