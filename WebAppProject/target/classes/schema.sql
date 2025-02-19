DROP TABLE IF EXISTS USER_CHECKOUT;

CREATE TABLE USER_CHECKOUT (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fname VARCHAR(60) NOT NULL,
    lname VARCHAR(60) NOT NULL,
    company VARCHAR(120),
    address VARCHAR(60) NOT NULL,
    city VARCHAR(60) NOT NULL,
    country VARCHAR(60) NOT NULL,
    phone INT NOT NULL

);