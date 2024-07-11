package com.example.springTest.model;

import com.example.springTest.dtos.ProductRecord;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_PPRODUCTS")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idProduct;
    private String name;
    private BigDecimal value;

    public ProductModel(@Valid ProductRecord productRecord) {
        this.name = productRecord.name();
        this.value = productRecord.value();
    }




    public UUID getIdProduct() {
        return idProduct;
    }
}
