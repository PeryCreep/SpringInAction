package sia.tacocloud.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sia.tacocloud.domain.Ingredient;
import sia.tacocloud.domain.ports.out.IngredientRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {


    private JdbcTemplate template;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return template.query("select id, name, type from Ingredient", this::mapRowToIngredient);
    }

    @Override
    public Optional<Ingredient> findById(String id) {
        List<Ingredient> results =
                template.query("select id, name, type from Ingredient where id =?", this::mapRowToIngredient, id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
         template.update("insert into Ingredient (id, name, type) values (?, ?, ?)",
                ingredient.getId(), ingredient.getName(), ingredient.getType());
         return ingredient;
    }

    private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
       return new Ingredient(
               row.getString("id"),
               row.getString("name"),
               Ingredient.Type.valueOf(row.getString("type"))
       );
    }
}
