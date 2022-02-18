package co.copper.test.storage;

import java.util.List;
import java.util.Map;

import co.copper.test.datamodel.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sbuslab.utils.db.JacksonBeanRowMapper;

import co.copper.test.datamodel.Test;


@Repository
public class TestJavaRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JacksonBeanRowMapper<Test> rowMapper;


    @Autowired
    public TestJavaRepository(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new JacksonBeanRowMapper<>(Test.class, mapper);
    }

    public List<Test> getById(Long id) {
        return jdbcTemplate.query("SELECT * FROM test WHERE id = :id", new MapSqlParameterSource("id", id), rowMapper);
    }


    public void saveUser(User user) {
        String query = "INSERT INTO person (first_name,second_name,email,password) " +
                "VALUES (:firstname,:lastname,:email,:password)";

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(
                Map.of(
                        "firstname",user.getFirstName(),
                        "lastname",user.getLastName(),
                        "email",user.getEmail(),
                        "password",user.getPassword()
                )
        );

        jdbcTemplate.update(query,sqlParameterSource);
    }
}
