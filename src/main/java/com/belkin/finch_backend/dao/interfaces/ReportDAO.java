package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("database_guide_report")
public interface ReportDAO extends CrudRepository<Report, Long> {
}
