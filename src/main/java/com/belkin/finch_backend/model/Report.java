package com.belkin.finch_backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "guide_report")
@Getter @Setter
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "guide_id", columnDefinition = "VARCHAR")
    private String guideId;

    @Column(columnDefinition = "VARCHAR")
    private String reporter;

    @Column(columnDefinition = "VARCHAR")
    private String reason;

    private ReportStatus status;

    public Report(String guideId, String reporter, String reason) {
        this.guideId = guideId;
        this.reporter = reporter;
        this.reason = reason;
        this.status = ReportStatus.QUEUED;
    }

    enum ReportStatus {
        QUEUED,
        ACCEPTED,
        DECLINED,
    }
}
