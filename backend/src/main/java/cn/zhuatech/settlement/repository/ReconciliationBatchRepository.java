/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.repository;

import cn.zhuatech.settlement.model.ReconciliationBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ReconciliationBatchRepository extends JpaRepository<ReconciliationBatch,Long> {
    Optional<ReconciliationBatch> findByBatchNo(String batchNo);
    List<ReconciliationBatch> findAllByOrderByUpdatedAtDesc();
    long countByState(String state);
}
