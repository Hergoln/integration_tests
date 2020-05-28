package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    private static String EMPTY_STRING = "";
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository repository;
    private List<User> testingUsers = new ArrayList<>();

    @Before
    public void setUp() {
        User user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        testingUsers.add(user);

        user = new User();
        user.setFirstName("Kamil");
        user.setEmail("kamil@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        testingUsers.add(user);

        user = new User();
        user.setFirstName("Jakub");
        user.setEmail("jacob@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        testingUsers.add(user);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(testingUsers.get(0));
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0)
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {
        User persistedUser = entityManager.persist(testingUsers.get(0));

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test
    public void shouldFindUserUsingFirstName() {
        User persistedUser = persistMultipleReturnOne();
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(persistedUser.getFirstName(), persistedUser.getFirstName(), persistedUser.getFirstName());

        assertThat(users, hasSize(1));
        assertThat(users.get(0).getFirstName(), equalTo(persistedUser.getFirstName()));
    }

    @Test
    public void shouldFindUserUsingLastName() {
        User persistedUser = persistMultipleReturnOne();
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(persistedUser.getLastName(), persistedUser.getLastName(), persistedUser.getLastName());

        assertThat(users, hasSize(1));
        assertThat(users.get(0).getFirstName(), equalTo(persistedUser.getFirstName()));
    }

    @Test
    public void shouldFindUserUsingEmail() {
        User persistedUser = persistMultipleReturnOne();
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(persistedUser.getEmail(), persistedUser.getEmail(), persistedUser.getEmail());

        assertThat(users, hasSize(1));
        assertThat(users.get(0).getFirstName(), equalTo(persistedUser.getFirstName()));
    }

    @Test
    public void shouldNotFindUserByNotExistingInRepositoryFirstName() {
        User persistedUser = persistMultipleReturnOne();
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("wrong first name", "wrong last name", "wrong email address");
        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldNotFindUserByNotExistingInRepositoryLastName() {
        User persistedUser = persistMultipleReturnOne();
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("wrong first name", "wrong last name", "wrong email address");
        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldNotFindUserByNotExistingInRepositoryEmail() {
        User persistedUser = persistMultipleReturnOne();
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("wrong first name", "wrong last name", "wrong email address");
        assertThat(users, hasSize(0));
    }

    private User persistMultipleReturnOne() {
        entityManager.persist(testingUsers.get(2));
        entityManager.persist(testingUsers.get(1));
        return entityManager.persist(testingUsers.get(0));
    }
}
