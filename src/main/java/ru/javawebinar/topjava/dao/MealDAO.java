package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealDAO {

    void addMeal(Meal meal);

    Collection<Meal> getAllMeals();

    Meal getMealById(Integer id);

    void deleteMeal(Integer id);
}
