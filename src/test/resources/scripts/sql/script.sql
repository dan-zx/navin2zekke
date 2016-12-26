DROP TABLE IF EXISTS test_user;

CREATE TABLE test_user (
    -- Column
    name /* a 100 character length name */ VARCHAR(100) NOT NULL
);

INSERT INTO test_user VALUES ('John') -- John Doe
;
-- Jane Doe
INSERT INTO test_user VALUES ('Jane');

/* A well commented line */
INSERT INTO test_user VALUES ('Dan');
