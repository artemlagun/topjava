package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(MEAL_ID, USER_ID);
        assertMatch(meal, userBreakfast);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> all = mealService.getBetweenInclusive(mealService.get(MEAL_ID, USER_ID).getDate(),
                mealService.get(MEAL_ID, USER_ID).getDate(), USER_ID);
        Collections.reverse(all);
        assertMatch(all, userBreakfast, userLunch, userDinner);
    }

    @Test
    public void getAllUser() {
        List<Meal> all = mealService.getAll(USER_ID);
        Collections.reverse(all);
        assertMatch(all, userBreakfast, userLunch, userDinner);
    }

    @Test
    public void getAllAdmin() {
        List<Meal> all = mealService.getAll(ADMIN_ID);
        Collections.reverse(all);
        assertMatch(all, adminMealForBoundaryValue, adminBreakfast, adminLunch, adminDinner);
    }

    @Test
    public void update() {
        Meal updated = getUpdate();
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(MEAL_ID, USER_ID), getUpdate());
    }

    @Test
    public void updateNotFound() {
        Meal updated = getUpdate();
        mealService.update(updated, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class,
                () -> mealService.create(new Meal(null, LocalDateTime.of(2022, Month.JANUARY,
                        30, 10, 0), "Duplicate", 500), USER_ID));
    }
}