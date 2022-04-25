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
  image TEXT,

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
  attachment TEXT NOT NULL,
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
INSERT INTO refund_db.users (name, email, password, image) --password: admin
VALUES ('admin', 'admin@dbccompany.com.br', '$2a$12$R7zbqGcvuqVhvMKsQqQAXOH.goaNseEInEF2NwmuVM5acRzlQZLJO', 'data:image/jpeg;base64,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
');

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

