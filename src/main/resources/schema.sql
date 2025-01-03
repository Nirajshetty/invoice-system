CREATE TABLE IF NOT EXISTS `invoice` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `amount` DECIMAL(15, 2) NOT NULL,
  `paid_amount` DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
  `due_date` DATE NOT NULL,
  `status` ENUM('PENDING', 'PAID', 'VOID') NOT NULL DEFAULT 'PENDING'
);