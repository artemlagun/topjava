package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDAOMemoryImpl implements MealDAO {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private Map<Integer, Meal> mapDAO = new ConcurrentHashMap<>();
    {
        MealsUtil.MEALS.forEach(this::addMeal);
    }

    @Override
    public void addMeal(Meal meal) {
        if (meal.isNew()) {
            meal.setId(COUNTER.incrementAndGet());
        }
        mapDAO.put(meal.getId(), meal);
    }

    @Override
    public Collection<Meal> getAllMeals() {
        return mapDAO.values();
    }

    @Override
    public Meal getMealById(Integer id) {
        return mapDAO.get(id);
    }

    @Override
    public void deleteMeal(Integer id) {
        mapDAO.remove(id);
    }
}
