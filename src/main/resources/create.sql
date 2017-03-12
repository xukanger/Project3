DROP TABLE IF EXISTS `EXAMINEE`;
CREATE TABLE `EXAMINEE`(
	`ID`							BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
	`GMT_CREATE`			DATETIME COMMENT '创建时间',
	`GMT_MODIFIED`		DATETIME COMMENT '最后修改时间',
	`IDENTITY`				VARCHAR(32) UNIQUE COMMENT '身份证号码',
	`NAME`						VARCHAR(32) COMMENT '姓名',
	`POSITION` 			VARCHAR(32) COMMENT '报考职位',
	`END`							TINYINT COMMENT '是否已结束 0已结束，1未结束',
  `BATCH`				  VARCHAR(16) COMMENT '考试批次',
	PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
ALTER TABLE `EXAMINEE` ADD INDEX `IND_IDENTITY` (`IDENTITY` ASC);

DROP TABLE IF EXISTS `TEST_MARK`;
CREATE TABLE `TEST_MARK`(
	`ID`							BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
	`GMT_CREATE`			DATETIME COMMENT '创建时间',
	`GMT_MODIFIED`		DATETIME COMMENT '最后修改时间',
	`START` 					BIGINT COMMENT '考试开始时间(毫秒)',
	`IDENTITY`				VARCHAR(32) COMMENT '身份证号码（冗余）',
	`NAME`						VARCHAR(32) COMMENT '姓名（冗余）',
	`TYPE`						VARCHAR(4) COMMENT '考试类型A/B',
	`NUM`							INT COMMENT '题号',
	`CONTENT`				VARCHAR(4096) COMMENT '考生答题内容',
	`RATE`					DOUBLE(9,6) COMMENT '成绩(准确率)',
	`CONFIRM`					INT COMMENT '0确认，1未确认',
	PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
ALTER TABLE `TEST_MARK` ADD INDEX `IND_IDENTITY` (`IDENTITY` ASC);

DROP TABLE IF EXISTS `TEST_BANK`;
CREATE TABLE `TEST_BANK`(
	`ID`				BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
	`GMT_CREATE`		DATETIME COMMENT '创建时间',
	`GMT_MODIFIED`		DATETIME COMMENT '最后修改时间',
	`STATUS`			DATETIME NOT NULL COMMENT '可用0,不可用1',
	`TYPE`				VARCHAR(4) COMMENT '试卷类型:A(初试),B(复试)',
	`MP3_ADD`			VARCHAR(128) COMMENT '音频文件地址',
	`CONTENT`			VARCHAR(4096) COMMENT '文本答案',
	PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `TEST_BATCH`;
CREATE TABLE `TEST_BATCH`(
	`ID`				BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
	`GMT_CREATE`		DATETIME COMMENT '创建时间',
	`BATCH`				  VARCHAR(16) COMMENT '考试批次',
	`TEST_TIME`			DATETIME COMMENT '考试时间',
	PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;