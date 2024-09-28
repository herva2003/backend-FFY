package com.puccampinas.backendp5noname.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Document(collection="ingredientes")
public class Ingredient {
    @Id
    private String id;
    private double quantity;
    private int NDB_No;
    private String Descrip;
    private double Energy_kcal;
    private double Protein_g;
    private double Saturated_fats_g;
    private double Fat_g;
    private double Carb_g;
    private double Fiber_g;
    private double Sugar_g;
    private double Calcium_mg;
    private double Iron_mg;
    private double Magnesium_mg;
    private double Phosphorus_mg;
    private double Potassium_mg;
    private double Sodium_mg;
    private double Zinc_mg;
    private double Copper_mcg;
    private double Manganese_mg;
    private double Selenium_mcg;
    private double VitC_mg;
    private double Thiamin_mg;
    private double Riboflavin_mg;
    private double Niacin_mg;
    private double VitB6_mg;
    private double Folate_mcg;
    private double VitB12_mcg;
    private double VitA_mcg;
    private double VitE_mg;
    private double VitD2_mcg;

}
