package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL_ID = START_SEQ;
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;

    public static final Meal userBreakfast = new Meal(100000,
            LocalDateTime.of(2022, Month.JANUARY, 30, 10, 0),"Завтрак", 500);
    public static final Meal userLunch = new Meal(100001,
            LocalDateTime.of(2022, Month.JANUARY, 30, 13, 0),"Обед", 1000);
    public static final Meal userDinner = new Meal(100002,
            LocalDateTime.of(2022, Month.JANUARY, 30, 20, 0),"Ужин", 500);
    public static final Meal adminMealForBoundaryValue = new Meal(100003,
            LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0),"Еда на граничное значение", 100);
    public static final Meal adminBreakfast = new Meal(100004,
            LocalDateTime.of(2022, Month.JANUARY, 31, 10, 0),"Завтрак", 1000);
    public static final Meal adminLunch = new Meal(100005,
            LocalDateTime.of(2022, Month.JANUARY, 30, 13, 0),"Обед", 500);
    public static final Meal adminDinner = new Meal(100006,
            LocalDateTime.of(2022, Month.JANUARY, 30, 20, 0),"Ужин", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now(), "New", 1000);
    }

    public static Meal getUpdate() {
        Meal updated = new Meal(userBreakfast);
        updated.setDateTime(LocalDateTime.of(2023, Month.JUNE, 10, 12, 0));
        updated.setDescription("UpdatedMeal");
        updated.setCalories(550);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
            assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal...expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
