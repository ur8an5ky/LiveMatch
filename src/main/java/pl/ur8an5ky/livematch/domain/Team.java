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

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "short_name", length = 10)
    private String shortName;

    @Column(name = "country", length = 50)
    private String country;
}