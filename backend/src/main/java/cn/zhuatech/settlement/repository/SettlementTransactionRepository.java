/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.repository;
import cn.zhuatech.settlement.model.SettlementTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
public interface SettlementTransactionRepository extends JpaRepository<SettlementTransaction,Long>{
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    List<SettlementTransaction> findByBatchIdOrderByExternalRefAsc(Long batchId);
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    boolean existsByBatchIdAndSideAndExternalRef(Long batchId,String side,String externalRef);
}
