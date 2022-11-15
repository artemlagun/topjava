package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ResultSetExtractor<List<User>> resultSetExtractor = rs -> {
        Map<Integer, User> map = new LinkedHashMap<>();
        AtomicInteger counter = new AtomicInteger(0);
        while (rs.next()) {
            Integer id = rs.getInt("id");
            User user = map.get(id);
            if (user == null) {
                user = ROW_MAPPER.mapRow(rs, counter.incrementAndGet());
                map.put(id, user);
            } else {
                counter.incrementAndGet();
            }
            String role = rs.getString("role");
            if (!(role == null) && user != null) {
                EnumSet<Role> roles = EnumSet.of(Enum.valueOf(Role.class, role));
                if (!user.getRoles().isEmpty()) {
                    roles.addAll(user.getRoles());
                    user.setRoles(roles);
                }
            }
        }

        return new ArrayList<>(map.values());
    };

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(@NotNull User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    final List<Role> roles = new ArrayList<>(user.getRoles());

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, Objects.requireNonNull(user.getId()));
                        ps.setString(2, roles.get(i).toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                });

        return user;
    }

    @Override
    @Transactional
    public boolean delete(@NotNull int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(@NotNull int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, ur.role roles FROM users u LEFT JOIN user_roles ur " +
                "ON u.id = ur.user_id WHERE id=?", resultSetExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(@NotBlank String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT u.*, ur.role roles FROM users u LEFT JOIN user_roles ur ON " +
                "u.id = ur.user_id WHERE email=?", resultSetExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.*, ur.role roles FROM users u LEFT JOIN user_roles ur " +
                "ON u.id = ur.user_id ORDER BY name, email", resultSetExtractor);
    }
}
