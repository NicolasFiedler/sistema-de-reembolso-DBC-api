CREATE SCHEMA refund_db;

-- -----------------------------------------------------
-- Table refund_db.roles
-- -----------------------------------------------------
CREATE TABLE refund_db.roles (
  id_role SERIAL,
  role TEXT NOT NULL UNIQUE,

  PRIMARY KEY (id_role)
);


-- -----------------------------------------------------
-- Table refund_db.users
-- -----------------------------------------------------
CREATE TABLE refund_db.users (
  id_user SERIAL,
  name TEXT NOT NULL,
  email TEXT NOT NULL UNIQUE,
  password TEXT NOT NULL,
  image BYTEA,

  PRIMARY KEY (id_user)
);

-- -----------------------------------------------------
-- Table refund_db.users_roles
-- -----------------------------------------------------
CREATE TABLE refund_db.users_roles (
  id_role INTEGER NOT NULL,
  id_user INTEGER NOT NULL,

  PRIMARY KEY (id_role, id_user),
  CONSTRAINT fk_roles_has_users_role
    FOREIGN KEY (id_role)
    REFERENCES refund_db.roles (id_role),
  CONSTRAINT fk_roles_has_users_users
    FOREIGN KEY (id_user)
    REFERENCES refund_db.users (id_user)
);

-- -----------------------------------------------------
-- Table refund_db.refund
-- -----------------------------------------------------
CREATE TABLE refund_db.refund (
  id_refund SERIAL,
  title TEXT NOT NULL,
  date TIMESTAMP NOT NULL,
  value NUMERIC(9, 2),
  status NUMERIC NOT NULL,
  id_user INTEGER NOT NULL,

  PRIMARY KEY (id_refund),
  CONSTRAINT fk_refund_users
    FOREIGN KEY (id_user)
    REFERENCES refund_db.users (id_user)
);


-- -----------------------------------------------------
-- Table refund_db.item
-- -----------------------------------------------------
CREATE TABLE refund_db.item (
  id_item SERIAL,
  name TEXT NOT NULL,
  date DATE NOT NULL,
  value NUMERIC(9, 2) NOT NULL,
  attachment BYTEA NOT NULL,
  id_refund INTEGER NOT NULL,

  PRIMARY KEY (id_item),
  CONSTRAINT fk_item_refund
    FOREIGN KEY (id_refund)
    REFERENCES refund_db.refund (id_refund)
);

-- -----------------------------------------------------
-- Insert refund_db.roles
-- -----------------------------------------------------
INSERT INTO refund_db.roles (role) VALUES ('ROLE_ADMIN');
INSERT INTO refund_db.roles (role) VALUES ('ROLE_FINANCEIRO');
INSERT INTO refund_db.roles (role) VALUES ('ROLE_GESTOR');
INSERT INTO refund_db.roles (role) VALUES ('ROLE_COLABORADOR');

-- -----------------------------------------------------
-- Insert refund_db.users              =Bcrypt=
-- -----------------------------------------------------
INSERT INTO refund_db.users (name, email, password) --password: admin
VALUES ('admin', 'admin@dbccompany.com.br', '$2a$12$R7zbqGcvuqVhvMKsQqQAXOH.goaNseEInEF2NwmuVM5acRzlQZLJO');

INSERT INTO refund_db.users (name, email, password) --password: financeiro
VALUES ('jonas', 'financeiro@dbccompany.com.br', '$2a$12$AANgLSu/127rSwlsodfrh.ZOL61Yzeg6c0wvtFs8n2oy3yLR7DAnO');

INSERT INTO refund_db.users (name, email, password) --password: gestor
VALUES ('jaqueline', 'gestor@dbccompany.com.br', '$2a$12$Yx5jlNcOfLeWG3MMNdDfquM9wN4ShEgHFdYjP/Rdiw3ZHXW/T9zl6');

INSERT INTO refund_db.users (name, email, password) --password: 123
VALUES ('marcos', 'marcos.alves@dbccompany.com.br', '$2a$12$U.0QlYm2JSuWAt.C4.nP.O3Oy9qgFHYW7BIvfplH2Hz61z1DE1iJO');

-- -----------------------------------------------------
-- Insert refund_db.users            =noBcritp=
-- -----------------------------------------------------
-- INSERT INTO refund_db.users (name, email, password)
-- VALUES ('admin', 'admin', 'admin');

-- INSERT INTO refund_db.users (name, email, password)
-- VALUES ('jonas', 'financeiro@dbccompany.com.br', 'financeiro');

-- INSERT INTO refund_db.users (name, email, password)
-- VALUES ('jaqueline', 'gestor@dbccompany.com.br', 'gestor');

-- INSERT INTO refund_db.users (name, email, password)
-- VALUES ('marcos', 'marcos.alves@dbccompany.com.br', '123');

-- -----------------------------------------------------
-- Insert refund_db.users_roles
-- -----------------------------------------------------
INSERT INTO refund_db.users_roles (id_user, id_role) --admin
VALUES (1, 1);
INSERT INTO refund_db.users_roles (id_user, id_role) --financeiro
VALUES (2, 2);
INSERT INTO refund_db.users_roles (id_user, id_role) --gestor
VALUES (3, 3);
INSERT INTO refund_db.users_roles (id_user, id_role) --colaborador
VALUES (4, 4);

-- -----------------------------------------------------
-- Insert refund_db.refund
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Insert refund_db.item
-- -----------------------------------------------------

