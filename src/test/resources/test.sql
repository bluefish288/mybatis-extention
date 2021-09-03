
CREATE TABLE users(
  id INT AUTO_INCREMENT,
  name varchar(32) DEFAULT '' COMMENT'',
  state int default 0,
  created      TIMESTAMP DEFAULT now()                   NOT NULL,
  updated      TIMESTAMP DEFAULT now() ON UPDATE now()   NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT = 23568 DEFAULT CHARSET=utf8mb4 COMMENT'测试';