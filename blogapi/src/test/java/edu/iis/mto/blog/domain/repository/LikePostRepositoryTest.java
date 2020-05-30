package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private LikePostRepository repository;
    private User poster;
    private User liker;

    private LikePost posterLike;
    private BlogPost posterPost;

    private LikePost likerLike;
    private BlogPost likerPost;

    @Before
    public void setUp() {
        poster = new User();
        poster.setFirstName("Jan");
        poster.setLastName("Kowalski");
        poster.setEmail("john@domain.com");
        poster.setAccountStatus(AccountStatus.CONFIRMED);
        entityManager.persist(poster);

        liker = new User();
        liker.setFirstName("Kamil");
        liker.setLastName("Komornik");
        liker.setEmail("kamil@domain.com");
        liker.setAccountStatus(AccountStatus.CONFIRMED);
        entityManager.persist(liker);

        posterPost = new BlogPost();
        posterPost.setUser(poster);
        posterPost.setEntry("test entry");
        entityManager.persist(posterPost);

        likerLike = new LikePost();
        likerLike.setPost(posterPost);
        likerLike.setUser(liker);

        likerPost = new BlogPost();
        likerPost.setUser(liker);
        likerPost.setEntry("test entry");
        entityManager.persist(likerPost);

        posterLike = new LikePost();
        posterLike.setPost(likerPost);
        posterLike.setUser(poster);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        List<LikePost> likePosts = repository.findAll();
        assertThat(likePosts, hasSize(0));
    }

    @Test
    public void shouldFindOneLikePost() {
        LikePost persistedLike = entityManager.persist(posterLike);
        List<LikePost> likePosts = repository.findAll();

        assertThat(persistedLike, notNullValue());
    }

    @Test
    public void shouldFindLikePostByUserPostingAndBlogPost() {
        entityManager.persist(likerLike);
        entityManager.persist(posterLike);
        Optional<LikePost> foundPost = repository.findByUserAndPost(likerLike.getUser(), likerLike.getPost());
        assertTrue(foundPost.isPresent());

        foundPost = repository.findByUserAndPost(posterLike.getUser(), posterLike.getPost());
        assertTrue(foundPost.isPresent());
    }

    @Test
    public void shouldReturnEmptyWhenLikePostIsAssociatedWithWrongUser() {
        entityManager.persist(likerLike);
        Optional<LikePost> foundPost = repository.findByUserAndPost(posterLike.getUser(), likerLike.getPost());
        assertFalse(foundPost.isPresent());
    }

    @Test
    public void shouldReturnEmptyWhenBlogPostIsAssociatedWithWrongBlogPost() {
        entityManager.persist(likerLike);
        Optional<LikePost> foundPost = repository.findByUserAndPost(likerLike.getUser(), posterLike.getPost());
        assertFalse(foundPost.isPresent());
    }

    @Test
    public void afterUserModificationLikePostShouldNowBeAssociatedWithOtherUser() {
        entityManager.persist(likerLike);

        User newUser = new User();
        newUser.setFirstName("Tester");
        newUser.setLastName("Testowski");
        newUser.setEmail("teste@testmail.com");
        newUser.setAccountStatus(AccountStatus.CONFIRMED);
        entityManager.persist(newUser);

        likerLike.setUser(newUser);
        entityManager.persist(likerLike);

        List<LikePost> all = repository.findAll();

        for (LikePost post : all) {
            System.out.print(post.getUser().getFirstName());
            System.out.println(post.getPost().getEntry());
        }

        Optional<LikePost> foundPost = repository.findByUserAndPost(likerLike.getUser(), likerLike.getPost());
        assertTrue(foundPost.isPresent());
    }

    @Test
    public void afterBlogPostModificationLikePostShouldNowBeAssociatedWithOtherBlogPost() {
        entityManager.persist(likerLike);

        BlogPost newPost = new BlogPost();
        newPost.setUser(liker);
        newPost.setEntry("another test entry");
        entityManager.persist(newPost);

        likerLike.setPost(newPost);
        entityManager.persist(likerLike);

        Optional<LikePost> foundPost = repository.findByUserAndPost(likerLike.getUser(), likerLike.getPost());
        assertTrue(foundPost.isPresent());
    }
}