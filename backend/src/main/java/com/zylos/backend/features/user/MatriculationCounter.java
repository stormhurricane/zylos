package com.zylos.backend.features.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "id_sequences")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class MatriculationCounter {

    @Id
    @Column(name = "sequence_name")
    private String sequenceName;

    @Column(name = "next_val", nullable = false)
    private Long nextVal;

}