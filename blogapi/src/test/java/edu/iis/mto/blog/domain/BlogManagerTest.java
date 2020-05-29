package edu.iis.mto.blog.domain;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BlogPostRepository blogPostRepository;
    @MockBean
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogDataMapper dataMapper;

    @Autowired
    private BlogService blogService;

    @Captor
    private ArgumentCaptor<User> userParam;
    @Captor
    private ArgumentCaptor<LikePost> likeParam;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test(expected = DomainError.class)
    public void whenUserWithStatusOtherThanCONFIRMEDPostsLikePostThrowDomainError() {
        User poster = new User();
        poster.setFirstName("Janusz");
        poster.setLastName("Janusz");
        poster.setEmail("janusz@grazyna.pl");
        poster.setId(1L);
        poster.setAccountStatus(AccountStatus.NEW);
        when(userRepository.findById(poster.getId())).thenReturn(Optional.of(poster));

        User liker = new User();
        liker.setFirstName("Grazyna");
        liker.setLastName("Grazyna");
        liker.setEmail("grazyna@janusz.pl");
        liker.setId(2L);
        liker.setAccountStatus(AccountStatus.NEW);
        when(userRepository.findById(liker.getId())).thenReturn(Optional.of(liker));

        BlogPost post = new BlogPost();
        post.setUser(poster);
        post.setEntry("test entry");
        when(blogPostRepository.findById(post.getId())).thenReturn(Optional.of(post));

        blogService.addLikeToPost(liker.getId(), post.getId());
    }

    @Test
    public void userWithStatusCONFIRMEDShouldBeAbleToAddLikePost() {
        User poster = new User();
        poster.setFirstName("Janusz");
        poster.setLastName("Janusz");
        poster.setEmail("janusz@grazyna.pl");
        poster.setId(1L);
        poster.setAccountStatus(AccountStatus.CONFIRMED);
        when(userRepository.findById(poster.getId())).thenReturn(Optional.of(poster));

        User liker = new User();
        liker.setFirstName("Grazyna");
        liker.setLastName("Grazyna");
        liker.setEmail("grazyna@janusz.pl");
        liker.setId(2L);
        liker.setAccountStatus(AccountStatus.CONFIRMED);
        when(userRepository.findById(liker.getId())).thenReturn(Optional.of(liker));

        BlogPost post = new BlogPost();
        post.setUser(poster);
        post.setEntry("test entry");
        when(blogPostRepository.findById(post.getId())).thenReturn(Optional.of(post));

        assertTrue(blogService.addLikeToPost(liker.getId(), post.getId()));
        verify(likePostRepository).save(likeParam.capture());
        LikePost likePost = likeParam.getValue();
        assertThat(likePost.getUser(), Matchers.equalTo(liker));
        assertThat(likePost.getPost(), Matchers.equalTo(post));
    }
}
