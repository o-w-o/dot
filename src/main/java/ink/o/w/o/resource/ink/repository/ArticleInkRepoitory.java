package ink.o.w.o.resource.ink.repository;

import ink.o.w.o.resource.ink.domain.ext.ArticleInk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleInkRepoitory extends JpaRepository<ArticleInk, String> {
}
