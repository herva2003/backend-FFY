package com.puccampinas.backendp5noname.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Document(collection="nutrition_dataset")
public class Nutrient {
    @Id
    private String id;
    private String name;
    private String serving_size;
    private double calories;
    private String total_fat;
    private String cholesterol;
    private String sodium;
    private String choline;
    private String folate;
    private String folic_acid;
    private String niacin;
    private String pantothenic_acid;
    private String riboflavin;
    private String thiamin;
    private String vitamin_a;
    private String vitamin_a_rae;
    private String carotene_alpha;
    private String carotene_beta;
    private String cryptoxanthin_beta;
    private String lutein_zeaxanthin;
    private String lucopene;
    private String vitamin_b12;
    private String vitamin_b6;
    private String vitamin_c;
    private String vitamin_d;
    private String vitamin_e;
    private String tocopherol_alpha;
    private String vitamin_k;
    private String calcium;
    private String copper;
    private String iron;
    private String magnesium;
    private String manganese;
    private String phosphorus;
    private String potassium;
    private String selenium;
    private String zinc;
    private String protein;
    private String alanine;
    private String arginine;
    private String aspartic_acid;
    private String cystine;
    private String glutamic_acid;
    private String glycine;
    private String histidine;
    private String hydroxyproline;
    private String isoleucine;
    private String leucine;
    private String lysine;
    private String methionine;
    private String phenylalanine;
    private String proline;
    private String serine;
    private String threonine;
    private String tryptophan;
    private String tyrosine;
    private String valine;
    private String carbohydrate;
    private String fiber;
    private String sugars;
    private String fructose;
    private String galactose;
    private String glucose;
    private String lactose;
    private String maltose;
    private String sucrose;
    private String fat;
    private String saturated_fatty_acids;
    private String monounsaturated_fatty_acids;
    private String polyunsaturated_fatty_acids;
    private String fatty_acids_total_trans;
    private String alcohol;
    private String ash;
    private String caffeine;
    private String theobromine;
    private String water;

}
