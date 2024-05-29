package com.puccampinas.backendp5noname.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesDoubleDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesStringDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="nutritional-values-recipes")
public class NutritionalValues {
    @Id
    private String id;
    private Double Calcium_mg;
    private Double Copper_mcg;
    private Double Iron_mg;
    private Double Magnesium_mg;
    private Double Manganese_mg;
    private Double Niacin_mg;
    private Double Phosphorus_mg;
    private Double Potassium_mg;
    private Double Riboflavin_mg;
    private Double Selenium_mcg;
    private Double Sodium_mg;
    private Double Thiamin_mg;
    private Double VitB6_mg;
    private Double Zinc_mg;
    private Double VitE_mg;
    @JsonFormat(pattern="dd/MM/yyyy 'as' HH:mm:ss")
    private LocalDateTime doneAt;


    public NutritionalValues(NutritionalValuesDoubleDTO data) {
        this.Calcium_mg = data.Calcium_mg();
        this.Copper_mcg = data.Copper_mcg();
        this.Iron_mg = data.Iron_mg();
        this.Magnesium_mg = data.Magnesium_mg();
        this.Manganese_mg = data.Manganese_mg();
        this.Niacin_mg = data.Niacin_mg();
        this.Phosphorus_mg = data.Phosphorus_mg();
        this.Potassium_mg = data.Potassium_mg();
        this.Riboflavin_mg = data.Riboflavin_mg();
        this.Selenium_mcg = data.Selenium_mcg();
        this.Sodium_mg = data.Sodium_mg();
        this.Thiamin_mg = data.Thiamin_mg();
        this.VitB6_mg = data.VitB6_mg();
        this.Zinc_mg = data.Zinc_mg();
        this.VitE_mg = data.VitE_mg();
    }

    public NutritionalValues(NutritionalValuesStringDTO data) {
        this.Calcium_mg = parseDouble(data.Calcium_mg());
        this.Copper_mcg = parseDouble(data.Copper_mcg());
        this.Iron_mg = parseDouble(data.Iron_mg());
        this.Magnesium_mg = parseDouble(data.Magnesium_mg());
        this.Manganese_mg = parseDouble(data.Manganese_mg());
        this.Niacin_mg = parseDouble(data.Niacin_mg());
        this.Phosphorus_mg = parseDouble(data.Phosphorus_mg());
        this.Potassium_mg = parseDouble(data.Potassium_mg());
        this.Riboflavin_mg = parseDouble(data.Riboflavin_mg());
        this.Selenium_mcg = parseDouble(data.Selenium_mcg());
        this.Sodium_mg = parseDouble(data.Sodium_mg());
        this.Thiamin_mg = parseDouble(data.Thiamin_mg());
        this.VitB6_mg = parseDouble(data.VitB6_mg());
        this.Zinc_mg = parseDouble(data.Zinc_mg());
        this.VitE_mg = parseDouble(data.VitE_mg());
        this.doneAt = LocalDateTime.now();
    }

    private Double parseDouble(String value) {
        if (value == null) return null;
        return Double.parseDouble(value);
    }
}
