package com.example.springTest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecord(@NotBlank String name, @NotNull BigDecimal value) {
}
