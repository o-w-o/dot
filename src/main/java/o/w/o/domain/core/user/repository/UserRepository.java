package o.w.o.domain.core.user.repository;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import o.w.o.domain.core.user.domain.QUser;
import o.w.o.domain.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author symbols@dingtalk.com
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {

  boolean existsByName(String name);

  boolean existsByNameAndIdIsNot(String username, Integer id);

  Optional<User> findUserByName(String name);

  Optional<User> findUserById(Integer id);

  @Override
  void deleteById(Integer id);

  @Modifying(clearAutomatically = true)
  @Query(value = "update User u set u.password=?1 where u.id=?2")
  int modifyUserPassword(String password, Integer id);

  @Override
  default void customize(QuerydslBindings bindings, QUser user) {
    bindings.bind(user.name)
        .first(StringExpression::contains);

    bindings.bind(String.class)
        .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);

    bindings.excluding(user.password);
  }
}
