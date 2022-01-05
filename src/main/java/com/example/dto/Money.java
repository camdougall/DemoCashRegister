package com.example.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Money {

        private String denomination;
        private Integer numberOfDenomination;

    }