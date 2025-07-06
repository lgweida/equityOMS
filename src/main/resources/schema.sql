-- Create orders table
-- CREATE TABLE IF NOT EXISTS orders (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     cl_ord_id VARCHAR(255) NOT NULL,
--     orig_cl_ord_id VARCHAR(255),
--     symbol VARCHAR(10) NOT NULL,
--     side CHAR(1) NOT NULL,
--     order_type CHAR(1) NOT NULL,
--     price DOUBLE,
--     quantity DOUBLE NOT NULL,
--     status VARCHAR(20) NOT NULL,
--     created_at TIMESTAMP NOT NULL,
--     updated_at TIMESTAMP NOT NULL
-- );

-- -- Create executions table
-- CREATE TABLE IF NOT EXISTS executions (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     order_id BIGINT NOT NULL,
--     exec_id VARCHAR(255) NOT NULL,
--     exec_type CHAR(1) NOT NULL,
--     last_qty DOUBLE NOT NULL,
--     last_px DOUBLE NOT NULL,
--     cum_qty DOUBLE NOT NULL,
--     avg_px DOUBLE NOT NULL,
--     leaves_qty DOUBLE NOT NULL,
--     created_at TIMESTAMP NOT NULL,
--     FOREIGN KEY (order_id) REFERENCES orders(id)
-- );

CREATE TABLE IF NOT EXISTS fix_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id VARCHAR(255),
    message_type VARCHAR(50),
    sender_comp_id VARCHAR(255),
    target_comp_id VARCHAR(255),
    message_content TEXT,
    direction VARCHAR(10),
    timestamp TIMESTAMP
);


CREATE TABLE IF NOT EXISTS sessions (
  beginstring CHAR(8) NOT NULL,
  sendercompid VARCHAR(64) NOT NULL,
  targetcompid VARCHAR(64) NOT NULL,
  session_qualifier VARCHAR(64) NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  incoming_seqnum INT NOT NULL,
  outgoing_seqnum INT NOT NULL,
  PRIMARY KEY (beginstring, sendercompid, targetcompid, session_qualifier)
);

CREATE TABLE IF NOT EXISTS messages (
  beginstring CHAR(8) NOT NULL,
  sendercompid VARCHAR(64) NOT NULL,
  targetcompid VARCHAR(64) NOT NULL,
  session_qualifier VARCHAR(64) NOT NULL,
  msgseqnum INT NOT NULL,
  message TEXT NOT NULL,
  PRIMARY KEY (beginstring, sendercompid, targetcompid, session_qualifier, msgseqnum)
);