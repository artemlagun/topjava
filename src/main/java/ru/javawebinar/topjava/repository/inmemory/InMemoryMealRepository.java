package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    public InMemoryMealRepository() {
        MealsUtil.meals.forEach(meal -> {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
            }
            repository.put(meal.getId(), meal);
        });
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        } else if (repository.get(meal.getId()).getUserId() == userId) {
            repository.put(meal.getId(), meal);
            return meal;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return (get(id, userId) != null) && (repository.remove(id) != null);
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() == userId) {
            return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(user -> user.getUserId() == userId)
                .sorted(((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime())))
                .collect(Collectors.toList());
    }
}

