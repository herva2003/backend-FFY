package com.puccampinas.backendp5noname.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.puccampinas.backendp5noname.dtos.SignupDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document(collection="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    private String id;
    @Indexed(unique=true)
    @NonNull
    private String login;
    @JsonIgnore
    @NonNull
    private String password;
    @NonNull
    private String fullName;
    @NonNull
    private Double height;
    @NonNull
    private Double weight;
    @NonNull
    private StatusUser status;
    @DocumentReference
    private List<Ingredient> ingredients;
    @DocumentReference
    private List<NutritionalValuesUser> nutritionalValuesUser;
    @DocumentReference
    private List<Recipe> recipes;
    private List<String> diets;
    private List<String> allergies;
    private List<String> intolerances;
    private List<String> reviewsId = new ArrayList<>();
    private List<String> likedRecipes = new ArrayList<>();
    private List<String> recipesDone = new ArrayList<>();

    @DocumentReference
    private List<Ingredient> shoppingList;

    @JsonFormat(pattern="dd/MM/yyyy 'as' HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern="dd/MM/yyyy 'as' HH:mm:ss")
    private LocalDateTime updatedAt;

    public User(SignupDTO userDTO, String password) {
        this.login = userDTO.getEmail();
        this.password = password;
        this.height = Double.parseDouble(userDTO.getHeight());
        this.weight = Double.parseDouble(userDTO.getWeight());
        this.fullName = userDTO.getFullName();
        this.status = StatusUser.ATIVO;
        this.ingredients = new ArrayList<>();
        this.recipes = new ArrayList<>();
        this.recipesDone = new ArrayList<>();
        this.nutritionalValuesUser = new ArrayList<>();
        this.diets = new ArrayList<>();
        this.allergies = new ArrayList<>();
        this.intolerances = new ArrayList<>();
        this.likedRecipes = new ArrayList<>();
        this.reviewsId = new ArrayList<>();
        this.shoppingList = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addReviewId(String reviewId) {
        this.reviewsId.add(reviewId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}