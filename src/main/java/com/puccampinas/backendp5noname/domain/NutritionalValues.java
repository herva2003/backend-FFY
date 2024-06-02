package com.puccampinas.backendp5noname.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesDoubleDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesStringDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesUserDoubleDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesUserStringDTO;
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
    private Double Energy_kcal;
    private Double Protein_g;
    private Double Saturated_fats_g;
    private Double Fat_g;
    private Double Carb_g;
    private Double Fiber_g;
    private Double Sugar_g;
    private Double Calcium_mg;
    private Double Iron_mg;
    private Double Magnesium_mg;
    private Double Phosphorus_mg;
    private Double Potassium_mg;
    private Double Sodium_mg;
    private Double Zinc_mg;
    private Double Copper_mcg;
    private Double Manganese_mg;
    private Double Selenium_mcg;
    private Double VitC_mg;
    private Double Thiamin_mg;
    private Double Riboflavin_mg;
    private Double Niacin_mg;
    private Double VitB6_mg;
    private Double Folate_mcg;
    private Double VitB12_mcg;
    private Double VitA_mcg;
    private Double VitE_mg;
    private Double VitD2_mcg;

    public NutritionalValues(NutritionalValuesDoubleDTO data) {
        this.Energy_kcal = data.Energy_kcal();
        this.Protein_g = data.Protein_g();
        this.Saturated_fats_g = data.Saturated_fats_g();
        this.Fat_g = data.Fat_g();
        this.Carb_g = data.Carb_g();
        this.Fiber_g = data.Fiber_g();
        this.Sugar_g = data.Sugar_g();
        this.Calcium_mg = data.Calcium_mg();
        this.Iron_mg = data.Iron_mg();
        this.Magnesium_mg = data.Magnesium_mg();
        this.Phosphorus_mg = data.Phosphorus_mg();
        this.Potassium_mg = data.Potassium_mg();
        this.Sodium_mg = data.Sodium_mg();
        this.Zinc_mg = data.Zinc_mg();
        this.Copper_mcg = data.Copper_mcg();
        this.Manganese_mg = data.Manganese_mg();
        this.Selenium_mcg = data.Selenium_mcg();
        this.VitC_mg = data.VitC_mg();
        this.Thiamin_mg = data.Thiamin_mg();
        this.Riboflavin_mg = data.Riboflavin_mg();
        this.Niacin_mg = data.Niacin_mg();
        this.VitB6_mg = data.VitB6_mg();
        this.Folate_mcg = data.Folate_mcg();
        this.VitB12_mcg = data.VitB12_mcg();
        this.VitA_mcg = data.VitA_mcg();
        this.VitE_mg = data.VitE_mg();
        this.VitD2_mcg = data.VitD2_mcg();
    }


}
