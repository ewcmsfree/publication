--错误日志序列
CREATE SEQUENCE seq_pub_error_id
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

--发布任务
--DROP TABLE pub_task;

--id:任务编号
--remark:任务描述
--publish_count:发布任务数
--finish_count:完成任务数
--error_count:错误任务数
--parent_id:所属任务编号
--create_time:创建时间
--start_time:开始时间
--finish_time:完成时间
--use_time:用时（毫秒）
--status:状态(0:等待发布,1:正在发布,2:完成,3:错误中断)
CREATE TABLE pub_task(
    id VARCHAR(50) NOT NULL PRIMARY KEY,
    site_id INTEGER NOT NULL,
    remark VARCHAR(200),
    publish_count INTEGER DEFAULT 0,
    finish_count INTEGER DEFAULT 0,
    error_count INTEGER DEFAULT 0,
    parent_id VARCHAR(50),
    create_time TIMESTAMP DEFAULT NOW(),
    start_time TIMESTAMP,
    finish_time TIMESTAMP,
    use_time INTEGER DEFAULT -1,
    status INTEGER DEFAULT 0
);

CREATE INDEX pub_task_site_id_idx ON pub_task (site_id);

--发布错误信息
--DROP TABLE pub_error;

--id:错误信息编号
--task_id:发布任务编号
--remark:错误描述
--error:错误异常
--create_time:创建时间
CREATE TABLE pub_error(
    id BIGINT DEFAULT NEXTVAL('seq_pub_error_id') PRIMARY KEY,
    task_id VARCHAR(50) NOT NULL,
    remark VARCHAR(200),
    error  TEXT,
    create_time TIMESTAMP DEFAULT NOW()
);

CREATE INDEX pub_error_task_id_idx ON pub_error (task_id);