package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.domain.Ingredient;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.domain.vo.IngredientVO;
import com.puccampinas.backendp5noname.repositories.RefreshTokenRepository;
import com.puccampinas.backendp5noname.repositories.UserRepository;
import com.puccampinas.backendp5noname.services.auth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IngredientService ingredientService;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("username not found") );
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("user id not found")  );
    }


    public User existUser(User user) {
        return userRepository.findByLogin(user.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<IngredientVO> addIngredientsToUser(User user, List<IngredientVO> ingredientsVO) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> ingredientIds = ingredientsVO.stream()
                .map(IngredientVO::getId)
                .collect(Collectors.toList());

        List<Ingredient> ingredients = ingredientService.allIngredientsById(ingredientIds);

        existingUser.getIngredients().addAll(ingredients);

        userRepository.save(existingUser);

        return ingredientsVO;
    }

    public List<Ingredient> ingredientsFromUser(User user) {
        return user.getIngredients();
    }

    public void deleteIngredientFromUser(User user, String ingredientId) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Ingredient> userIngredients = existingUser.getIngredients();
        boolean removed = userIngredients.removeIf(ingredient -> ingredient.getId().equals(ingredientId));

        if (!removed) {
            throw new RuntimeException("Ingredient not found in user");
        }

        userRepository.save(existingUser);
    }

}
