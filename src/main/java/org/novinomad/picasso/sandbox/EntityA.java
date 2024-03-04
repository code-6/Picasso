package org.novinomad.picasso.sandbox;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EntityA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ElementCollection
    @JoinTable(name = "a_b_values_jt",
            joinColumns = {@JoinColumn(name = "a_id", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "b_id")
    @Column(name = "pkg_val")
    private Map<EntityB, String> entityBWithValues = new LinkedHashMap<>();
}
