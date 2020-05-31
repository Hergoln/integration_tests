--this script initiates db for h2 db (used in test profile)

-- confirmed users
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'kamil@jbzd.com', 'Kamil', 'Dzida')
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'ronnie@jbzd.com', 'Ronnie', 'OSullivan')
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'koniu@jbzd.com', 'Konrad', 'Wonny')

-- new users
insert into user (account_status, email, first_name, last_name) values ('NEW', 'brian@kwejk.com', 'Brian', 'Zywot')
insert into user (account_status, email, first_name, last_name) values ('NEW', 'johny@kwejk.com', 'Johny', 'Bravo')
insert into user (account_status, email, first_name, last_name) values ('NEW', 'foster@kwejk.com', 'Pani', 'Foster')

-- deleted users
insert into user (account_status, email, first_name, last_name) values ('REMOVED', 'lody@demotywatory.com', 'Lody', 'Coral')
insert into user (account_status, email, first_name, last_name) values ('REMOVED', 'john@demotywatory.com', 'Johny', 'Wick')
insert into user (account_status, email, first_name, last_name) values ('REMOVED', 'elon@demotywatory.com', 'Elon', 'Musk')

-- blog posts
insert into blog_post (user_id, entry) values (1, 'initiated testing post')