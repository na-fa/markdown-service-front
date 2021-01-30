package com.example.persistence;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import javax.persistence.PersistenceUnit;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.NoResultException;

import com.example.model.Article;
import com.example.model.User;
import com.example.model.Tag;
import com.example.model.Bookmark;
import com.example.model.view.ArticleView;
import com.example.model.view.ArticleDetailView;

@Service
public class ArticlePersistence {
  @PersistenceUnit
  private EntityManagerFactory emf;

  /**
   * 更新日時順に記事を取得
   * @param loginUserId ログインユーザーID
   * @param pageable ページネーション情報
   * @return Page<ArticleView> 記事情報
   */
  public Page<ArticleView> latestList(Long loginUserId, Pageable pageable) {
    EntityManager em = emf.createEntityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<ArticleView> query = cb.createQuery(ArticleView.class);
    Root<Article> root = query.from(Article.class);

    Join<Article, User> userJoin = root.join("user", JoinType.INNER);
    Join<Article, Bookmark> bookmarkJoin = root.join("bookmarks", JoinType.LEFT);
    bookmarkJoin.on(cb.equal(bookmarkJoin.get("user").get("id"), loginUserId));
    query.select(cb.construct(ArticleView.class,
      root.get("id"),
      root.get("hash"),
      root.get("title"),
      root.get("updatedAt"),
      root.get("user").get("name"),
      cb.selectCase() // ブックマークされていればtrue、されていなければfalse
        .when(cb.isNotNull(bookmarkJoin.get("id")), true)
        .otherwise(false)
    ));

    List<Predicate> preList = new ArrayList<>();
    preList.add(cb.equal(root.get("status"), 1));
    preList.add(cb.isNull(root.get("deletedAt")));
    preList.add(cb.isNull(userJoin.get("deletedAt")));
    Predicate[] pre = new Predicate[preList.size()];
    Predicate finalPredicate = cb.and(preList.toArray(pre));
    query.where(finalPredicate).orderBy(cb.desc(root.get("updatedAt")));

    int offset = (int)pageable.getOffset();
    int pageSize = pageable.getPageSize();
    List<ArticleView> resultList = em.createQuery(query).setFirstResult(offset).setMaxResults(pageSize).getResultList();

    // Count Query
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<Article> rootCount = countQuery.from(Article.class);
    Join<Article, User> countUserJoin = rootCount.join("user", JoinType.INNER);

    List<Predicate> countPreList = new ArrayList<>();
    countPreList.add(cb.equal(rootCount.get("status"), 1));
    countPreList.add(cb.isNull(rootCount.get("deletedAt")));
    countPreList.add(cb.isNull(countUserJoin.get("deletedAt")));
    Predicate[] countPre = new Predicate[countPreList.size()];
    Predicate countFinalPredicate = cb.and(countPreList.toArray(countPre));

    countQuery.select(cb.count(rootCount)).where(countFinalPredicate);
    Long count = em.createQuery(countQuery).getSingleResult();
    // End Count Query

    em.close();
    return new PageImpl<>(resultList, pageable, count);
  }

  /**
   * ユーザー名から更新日時順に記事を取得
   * @param loginUserId ログインユーザーID
   * @param loginUserId 記事ユーザー名
   * @param pageable ページネーション情報
   * @return Page<ArticleView> 記事情報
   */
  public Page<ArticleView> latestListByUserName(Long loginUserId, String userName, Pageable pageable) {
    EntityManager em = emf.createEntityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<ArticleView> query = cb.createQuery(ArticleView.class);
    Root<Article> root = query.from(Article.class);

    Join<Article, User> userJoin = root.join("user", JoinType.INNER);
    Join<Article, Bookmark> bookmarkJoin = root.join("bookmarks", JoinType.LEFT);
    bookmarkJoin.on(cb.equal(bookmarkJoin.get("user").get("id"), loginUserId));
    query.select(cb.construct(ArticleView.class,
      root.get("id"),
      root.get("hash"),
      root.get("title"),
      root.get("updatedAt"),
      root.get("user").get("name"),
      cb.selectCase() // ブックマークされていればtrue、されていなければfalse
        .when(cb.isNotNull(bookmarkJoin.get("id")), true)
        .otherwise(false)
    ));

    List<Predicate> preList = new ArrayList<>();
    preList.add(cb.equal(userJoin.get("name"), userName));
    preList.add(cb.equal(root.get("status"), 1));
    preList.add(cb.isNull(root.get("deletedAt")));
    preList.add(cb.isNull(userJoin.get("deletedAt")));
    Predicate[] pre = new Predicate[preList.size()];
    Predicate finalPredicate = cb.and(preList.toArray(pre));
    query.where(finalPredicate).orderBy(cb.desc(root.get("updatedAt")));

    int offset = (int)pageable.getOffset();
    int pageSize = pageable.getPageSize();
    List<ArticleView> resultList = em.createQuery(query).setFirstResult(offset).setMaxResults(pageSize).getResultList();

    // Count Query
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<Article> rootCount = countQuery.from(Article.class);
    Join<Article, User> countUserJoin = rootCount.join("user", JoinType.INNER);

    List<Predicate> countPreList = new ArrayList<>();
    countPreList.add(cb.equal(countUserJoin.get("name"), userName));
    countPreList.add(cb.equal(rootCount.get("status"), 1));
    countPreList.add(cb.isNull(rootCount.get("deletedAt")));
    countPreList.add(cb.isNull(countUserJoin.get("deletedAt")));
    Predicate[] countPre = new Predicate[countPreList.size()];
    Predicate countFinalPredicate = cb.and(countPreList.toArray(countPre));

    countQuery.select(cb.count(rootCount)).where(countFinalPredicate);
    Long count = em.createQuery(countQuery).getSingleResult();
    // End Count Query

    em.close();
    return new PageImpl<>(resultList, pageable, count);
  }

  /**
   * タグIDから更新日時順に記事を取得
   * @param loginUserId ログインユーザーID
   * @param tagId タグID
   * @param pageable ページネーション情報
   * @return Page<ArticleView> 記事情報
   */
  public Page<ArticleView> latestListByTag(Long loginUserId, int tagId, Pageable pageable) {
    EntityManager em = emf.createEntityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<ArticleView> query = cb.createQuery(ArticleView.class);
    Root<Article> root = query.from(Article.class);

    Join<Article, User> userJoin = root.join("user", JoinType.INNER);
    Join<Article, Tag> tagJoin = root.join("articleTags", JoinType.INNER);
    Join<Article, Bookmark> bookmarkJoin = root.join("bookmarks", JoinType.LEFT);
    bookmarkJoin.on(cb.equal(bookmarkJoin.get("user").get("id"), loginUserId));
    query.select(cb.construct(ArticleView.class,
      root.get("id"),
      root.get("hash"),
      root.get("title"),
      root.get("updatedAt"),
      root.get("user").get("name"),
      cb.selectCase() // ブックマークされていればtrue、されていなければfalse
        .when(cb.isNotNull(bookmarkJoin.get("id")), true)
        .otherwise(false)
    ));

    List<Predicate> preList = new ArrayList<>();
    preList.add(cb.equal(tagJoin.get("tag").get("id"), tagId));
    preList.add(cb.equal(root.get("status"), 1));
    preList.add(cb.isNull(root.get("deletedAt")));
    preList.add(cb.isNull(userJoin.get("deletedAt")));
    Predicate[] pre = new Predicate[preList.size()];
    Predicate finalPredicate = cb.and(preList.toArray(pre));
    query.where(finalPredicate).orderBy(cb.desc(root.get("updatedAt")));

    int offset = (int)pageable.getOffset();
    int pageSize = pageable.getPageSize();
    List<ArticleView> resultList = em.createQuery(query).setFirstResult(offset).setMaxResults(pageSize).getResultList();

    // Count Query
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<Article> rootCount = countQuery.from(Article.class);
    Join<Article, User> countUserJoin = rootCount.join("user", JoinType.INNER);
    Join<Article, Tag> countTagJoin = rootCount.join("articleTags", JoinType.INNER);

    List<Predicate> countPreList = new ArrayList<>();
    countPreList.add(cb.equal(countTagJoin.get("tag").get("id"), tagId));
    countPreList.add(cb.equal(rootCount.get("status"), 1));
    countPreList.add(cb.isNull(rootCount.get("deletedAt")));
    countPreList.add(cb.isNull(countUserJoin.get("deletedAt")));
    Predicate[] countPre = new Predicate[countPreList.size()];
    Predicate countFinalPredicate = cb.and(countPreList.toArray(countPre));

    countQuery.select(cb.count(rootCount)).where(countFinalPredicate);
    Long count = em.createQuery(countQuery).getSingleResult();
    // End Count Query

    em.close();
    return new PageImpl<>(resultList, pageable, count);
  }

  /**
   * 記事詳細を取得
   * @param loginUserId ログインユーザーID
   * @param userName 記事ユーザー名
   * @param articleHash 記事ハッシュ
   * @return result 記事情報
   */
  public ArticleDetailView articleDetail(Long loginUserId, String userName, String articleHash) {
    EntityManager em = emf.createEntityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<ArticleDetailView> query = cb.createQuery(ArticleDetailView.class);
    Root<Article> root = query.from(Article.class);

    Join<Article, User> userJoin = root.join("user", JoinType.INNER);
    Join<Article, Bookmark> bookmarkJoin = root.join("bookmarks", JoinType.LEFT);
    bookmarkJoin.on(cb.equal(bookmarkJoin.get("user").get("id"), loginUserId));
    query.select(cb.construct(ArticleDetailView.class,
      root.get("id"),
      root.get("title"),
      root.get("detail"),
      root.get("updatedAt"),
      root.get("user").get("name"),
      cb.selectCase() // ブックマークされていればtrue、されていなければfalse
        .when(cb.isNotNull(bookmarkJoin.get("id")), true)
        .otherwise(false)
    ));

    List<Predicate> preList = new ArrayList<>();
    preList.add(cb.equal(root.get("hash"), articleHash));
    preList.add(cb.equal(userJoin.get("name"), userName));
    preList.add(cb.equal(root.get("status"), 1));
    preList.add(cb.isNull(root.get("deletedAt")));
    preList.add(cb.isNull(userJoin.get("deletedAt")));
    Predicate[] pre = new Predicate[preList.size()];
    Predicate finalPredicate = cb.and(preList.toArray(pre));
    query.where(finalPredicate);

    ArticleDetailView result = null;
    try {
      TypedQuery<ArticleDetailView> tq = em.createQuery(query);  
      result = tq.getSingleResult();
    } catch (NoResultException nre){
    }

    em.close();
    return result;
  }
}
