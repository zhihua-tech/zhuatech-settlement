/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.repository;
import cn.zhuatech.settlement.model.ControlDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ControlDocumentRepository extends JpaRepository<ControlDocument,Long>{
    List<ControlDocument> findByControlIdOrderByCreatedAtDesc(Long controlId);
}
